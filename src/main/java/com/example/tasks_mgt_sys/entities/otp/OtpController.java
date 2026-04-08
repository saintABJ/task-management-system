package com.example.tasks_mgt_sys.entities.otp;

import com.example.tasks_mgt_sys.entities.otp.dto.OtpRequest;
import com.example.tasks_mgt_sys.entities.otp.service.OtpService;
import com.example.tasks_mgt_sys.utils.ApiResponse;
import com.example.tasks_mgt_sys.utils.view.OtpView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/otp")
@RestController
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@JsonView(OtpView.Base.class) @RequestBody OtpRequest request) {

        otpService.verifyOtp(OtpRequest.builder()
                .purpose(OtpType.ACCOUNT_VERIFICATION.name())
                .email(request.email())
                .otp(request.otp())
                .build());

        return ResponseEntity.ok(ApiResponse.success("User verified", null));
    }


    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<String>> resendOtp(@JsonView(OtpView.Resend.class) @RequestBody OtpRequest request) {
        otpService.resendOtp(request);
        return ResponseEntity.ok(ApiResponse.success("Otp sent successfully", null));
    }

}
