package com.example.tasks_mgt_sys.entities.otp.service;

import com.example.tasks_mgt_sys.entities.otp.dto.OtpRequest;

public interface OtpService {

    void createOtp(OtpRequest request);

    void generateAndStoreOtp(OtpRequest request);

    void resendOtp(OtpRequest request);

    void verifyOtp(OtpRequest request);

}
