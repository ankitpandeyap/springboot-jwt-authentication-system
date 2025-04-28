package com.robspecs.otp.service;

import org.springframework.stereotype.Service;


public interface OtpService {

	public void generateOtp(String email);

	public boolean validateOtp(String email, String otp);

}
