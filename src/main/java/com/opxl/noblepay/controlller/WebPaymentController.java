package com.opxl.noblepay.controlller;

import com.opxl.noblepay.dto.request.AuthorizeInswRequest;
import com.opxl.noblepay.dto.request.InitiateWebPayRequest;
import com.opxl.noblepay.dto.request.InswResendOtpRequest;
import com.opxl.noblepay.service.WebPayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/card")
public class WebPaymentController {
    private  final WebPayService webPayService;

    @PostMapping("/initializePayment")
    public ResponseEntity<?> initiatePayment(@RequestBody @Valid InitiateWebPayRequest initiateWebPayRequest){
        return ResponseEntity.ok(webPayService.initiatePayment(initiateWebPayRequest));
    }

    @PostMapping("/callback")
    public  ResponseEntity<?> CallBack(@RequestBody Map<String,Object> payload){
        return null;
    }


    @PostMapping("/authorizeTransaction")
    public ResponseEntity<?> authorizeTransaction(@RequestBody @Valid AuthorizeInswRequest authorizeInswRequest){
        return  ResponseEntity.ok(webPayService.authorizeTrans(authorizeInswRequest));
    }

    @PostMapping("/resentOtp")
    public  ResponseEntity<?> resendOtp(@RequestBody @Valid InswResendOtpRequest inswResendOtpRequest){
        return ResponseEntity.ok(webPayService.resendOtp(inswResendOtpRequest));
    }

    @PostMapping("/status/verify")
    public  ResponseEntity<?> statusVerify(@RequestParam  String transactionRef, @RequestParam String amount ){
        return ResponseEntity.ok(webPayService.statusVerify(transactionRef,amount));
    }


}
