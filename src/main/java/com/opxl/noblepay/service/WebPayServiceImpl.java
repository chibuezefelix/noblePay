package com.opxl.noblepay.service;


import com.google.gson.Gson;
import com.interswitch.techquest.auth.Interswitch;
import com.opxl.noblepay.dto.request.AuthorizeInswRequest;
import com.opxl.noblepay.dto.request.InitiateWebPayRequest;
import com.opxl.noblepay.dto.request.InswPurchaseRequest;
import com.opxl.noblepay.dto.request.InswResendOtpRequest;
import com.opxl.noblepay.dto.response.InswAuthWebResponse;
import com.opxl.noblepay.dto.response.InswAuthorizeResponse;
import com.opxl.noblepay.dto.response.InswPurchaseResponse;
import com.opxl.noblepay.http.HttpClient;
import com.opxl.noblepay.model.WebPayRequest;
import com.opxl.noblepay.repository.WebPayRequestRepository;
import com.opxl.noblepay.utils.DataMaskingUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebPayServiceImpl implements WebPayService {
    private static String AUTH_TOKEN = "";
    private final Environment environment;
    private final HttpClient httpClient;
    private final OkHttpClient okHttpClient;
    private final WebPayRequestRepository webPayRequestRepository;

    private final Gson gson;
    private final ModelMapper mapper;
    Interswitch interswitch;

    @PostConstruct
    public void ini() {
        this.interswitch = new Interswitch(environment.getProperty(""), environment.getProperty(""));
    }

    public String reverseExpiryDate(String expDate) {
        if (expDate.length() < 6) {
            expDate = expDate.replace("/", "/20");
        } else {
            expDate = expDate.replace("/", "");
        }
        String firstTwo = expDate.substring(0, 2);
        String lastTwo = expDate.substring(4, 6);
        log.info(lastTwo, firstTwo);
        return lastTwo + firstTwo;

    }

    /// buildEncryptedAuthData
    public String getAuthData(String version, String pan, String pin, String expiryDate, String cvv2) throws Exception {
        String authData = "";
        String authDataPlain = version + "Z" + pan + "Z" + pin + "Z" + expiryDate + "Z" + cvv2;
        String modulus = Objects.requireNonNull(environment.getProperty("INSW_MODULUS")).trim();
        String publicExponent = Objects.requireNonNull(environment.getProperty("INSW_PUBLIC_EXPONENT")).trim();
        Security.addProvider(new BouncyCastleProvider());
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(publicKeySpec);
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] authDataBytes = encryptCipher.doFinal(authDataPlain.getBytes(StandardCharsets.UTF_8));
        authData = Base64.getEncoder().encodeToString(authDataBytes).replaceAll("\\r|\\n", "");

        return authData;

    }

    private InswAuthWebResponse getAuthToken() {
        try {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

            RequestBody body = RequestBody.create(mediaType, "");
            Request req = new Request.Builder()
                    .url(Objects.requireNonNull(environment.getProperty("INSW_AUTH_URL")).trim())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", Objects.requireNonNull(environment.getProperty("INSW_TEMPAUTH")).trim())
                    .build();
            Response tokenResponse = okHttpClient.newCall(req).execute();
            assert tokenResponse.body() != null;
            String resBody = tokenResponse.body().string();
            String resMessage = tokenResponse.message();
            String resCode = String.valueOf(tokenResponse.code());

            log.debug("Auth Response:{} | {} | {} | {}", environment.getProperty("ISW_AUTHURL"), resCode, resMessage, resBody);
            if (tokenResponse.isSuccessful()) {
                InswAuthWebResponse inswAuthWebResponse = gson.fromJson(resBody, InswAuthWebResponse.class);
                AUTH_TOKEN = inswAuthWebResponse.getAccessToken();
                return inswAuthWebResponse;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object initiatePayment(InitiateWebPayRequest initiateWebPayRequest) {
        InswPurchaseResponse inswPurchaseResponse = new InswPurchaseResponse();
        InswAuthWebResponse inswAuthWebResponse = getAuthToken();
        if (Objects.isNull(inswAuthWebResponse)) {
            return null;
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", "Bearer" + " " + inswAuthWebResponse.getAccessToken());

        InswPurchaseRequest inswPurchaseRequest = new InswPurchaseRequest();
        inswPurchaseRequest.setAmount(initiateWebPayRequest.getAmount());
        inswPurchaseRequest.setCustomerId(initiateWebPayRequest.getCustomerId());
        inswPurchaseRequest.setCurrency(initiateWebPayRequest.getCurrency());

        try {
            inswPurchaseRequest.setAuthData(getAuthData("1", initiateWebPayRequest.getPan(), initiateWebPayRequest.getPin(),
                    reverseExpiryDate(initiateWebPayRequest.getExpiryDate()), initiateWebPayRequest.getCvv2()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        inswPurchaseRequest.setTransactionRef("NP" + RandomStringUtils.randomAlphabetic(10));
        inswPurchaseRequest.setCallBackUrl(Objects.toString(initiateWebPayRequest.getCallBackUrl(), Objects.requireNonNull(environment.getProperty("WEBPAY_CALLBACK_URL")).trim()));
        WebPayRequest initiateWebRequest = mapper.map(inswPurchaseRequest, WebPayRequest.class);
        initiateWebRequest.setDeviceInformation(gson.toJson(inswPurchaseRequest.getDeviceInformation()));
        WebPayRequest webPayRequest = webPayRequestRepository.save(initiateWebRequest);
        String requestBody = gson.toJson(inswPurchaseRequest);
        String maskedRequestBody = DataMaskingUtil.maskCardWebPayData(initiateWebPayRequest);
        String url = environment.getProperty("ISW_PURCHASEURL");
        String rspBody = "";
        String msg = "";
        String code = "";
        try {
            Response response = httpClient.postAndMaskData(headers, requestBody, url, maskedRequestBody);
            rspBody = response.body().string();
            msg = response.message();
            code = String.valueOf(response.code());

            log.info("Purchase Response:{} | {} | {}", code, msg, rspBody);
            inswPurchaseResponse = gson.fromJson(rspBody, InswPurchaseResponse.class);
            webPayRequest.setPaymentId(inswPurchaseResponse.getPaymentId());
            webPayRequest.setResponseCode(inswPurchaseResponse.getRespondCode());
            webPayRequest.setMd(StringUtils.isEmpty(inswPurchaseResponse.getMD()) ? "N/A" : inswPurchaseResponse.getMD());
            webPayRequest.setTransactionId(StringUtils.isEmpty(inswPurchaseResponse.getTransactionId()) ? "N/A" : inswPurchaseResponse.getTransactionId());
            webPayRequestRepository.save(webPayRequest);
            return rspBody;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Object authorizeTrans(AuthorizeInswRequest authorizeInswRequest) {
        return null;
    }

    @Override
    public Object statusVerify(String transactionRef, String amount) {
        return null;
    }

    @Override
    public Object resendOtp(InswResendOtpRequest resendOtpRequest) {

        return null;
    }


}
