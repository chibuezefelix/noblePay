package com.opxl.noblepay.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HttpClientImplementation implements HttpClient {
    private OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;


    @Override
    public Response post(Map<String, String> headerLIst, String jsonPayload, String url) throws IOException {
        log.info("Making POST request with header{}, with payload {}, to {} ", headerLIst, jsonPayload, url);

        Request request = new Request.Builder().post(
                RequestBody.create(jsonPayload, MediaType.parse("application/json"))
        ).headers(Headers.of(headerLIst)).url(url).build();
        return okHttpClient.newCall(request).execute();
    }

    @Override
    public Response postAndMaskData(Map<String, String> headerLIst, String jsonPayload, String url, String maskedDate) throws IOException {
        log.info("Making masked POST request with header{}, with payload {}, to {} ", headerLIst, jsonPayload, url);

        Request request = new Request.Builder().post(
                RequestBody.create(jsonPayload, MediaType.parse("application/json"))
        ).headers(Headers.of(headerLIst)).url(url).build();

        return okHttpClient.newCall(request).execute();
    }

    @Override
    public Response get(Map<String, String> headerLIst, Map<String, Object> params, String url) throws IOException {
        log.info("Making GET request with header {}, params {} and url {}", headerLIst, params, url);
        String queryString = params
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String fullUrl = url + "?" + queryString;
        URL httpUrl = new URL( fullUrl);
        Request request = new Request.Builder()
                .get()
                .headers(Headers.of(headerLIst))
                .url(httpUrl)
                .build();

        return okHttpClient.newCall(request).execute();
    }

    @Override
    public Response getNoParam(Map<String, String> headerLIst, String url) throws IOException {
        log.info("Making GET request with header {} and url {}", headerLIst, url);
        Request request = new Request.Builder()
                .get()
                .headers(Headers.of(headerLIst))
                .url(url)
                .build();
        return okHttpClient.newCall(request).execute();
    }

    @Override
    public <T> T toPojo(String obj, Class<T> type) {
        try {
            return objectMapper.readValue(obj, type);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("--> conversion of json  to object error {} ", e.getMessage());
            throw new RuntimeException("Error while parsing response");
        }
    }

    @Override
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("conversion to json string error :: {}", e);
            return "{}";
        }
    }

    @Override
    @SneakyThrows
    public <T> T post(Map<String, String> headerList, String jsonPayload, String url, Class<T> type) {
        return responseToObject(post(headerList, jsonPayload, url), type);
    }

    private <T> T responseToObject(Response r, Class<T> t) {

        try {
            return toPojo(r.body().string(), t);
        } catch (Exception e) {
            log.info("--> Error converting response to object :: {}", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
