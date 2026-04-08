package com.example.tasks_mgt_sys.entities.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByEmailAndPurposeContainingIgnoreCase(String email, String purpose);
}
