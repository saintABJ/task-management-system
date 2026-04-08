package com.example.tasks_mgt_sys.entities.otp.dto;

import com.example.tasks_mgt_sys.utils.view.OtpView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;


@Builder
public record OtpRequest(

        @JsonView(OtpView.Base.class) String otp,
        @JsonView({OtpView.Base.class, OtpView.Resend.class}) String email,
        @JsonView(OtpView.Purpose.class) String purpose) {
}
