package com.robspecs.otp.service;

import org.springframework.stereotype.Service;


public interface OtpService {

	public String generateOtp(String email);

	public boolean validateOtp(String email, String otp);

}
