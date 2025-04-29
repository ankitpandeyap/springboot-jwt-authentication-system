package com.robspecs.otp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robspecs.otp.service.MailService;
import com.robspecs.otp.service.OtpService;

@RestController
@RequestMapping("/api/auth/otp")
public class OtpController {

	private final OtpService otpService;
	private final MailService mailService;

	public OtpController(OtpService otpService, MailService mailService) {
		this.otpService = otpService;
		this.mailService = mailService;
	}

	@PostMapping("/request")
	public ResponseEntity<?> requestOtp(@RequestParam String email) {
		
		try {
			String otp = otpService.generateOtp(email);
			mailService.sendOtpEmail(email, otp);
			return ResponseEntity.ok("OTP sent to " + email);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.TOO_EARLY).body(e.getLocalizedMessage());
		}
		
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
		try {
			boolean valid = otpService.validateOtp(email, otp);
			if (valid) {

				return ResponseEntity.ok("OTP verified");
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
		}
		return null;

	}

}
