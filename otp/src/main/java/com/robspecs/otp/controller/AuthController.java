package com.robspecs.otp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.robspecs.otp.dto.RegistrationDTO;
import com.robspecs.otp.enums.Role;
import com.robspecs.otp.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
    
	private final StringRedisTemplate redisTemplate;
	
	@Autowired
	public AuthController(UserService userService, StringRedisTemplate redisTemplate) {
	        this.userService = userService;
	        this.redisTemplate= redisTemplate;
	    }
	
	   @PostMapping("/register")
	    public ResponseEntity<?> signup(@RequestBody RegistrationDTO currDTO) {
		   
		   if(Boolean.FALSE.equals(currDTO.isVerified()) || this.redisTemplate.opsForValue().get(currDTO.getEmail()).equals("0"))
                   return new ResponseEntity<>("EMAIL IS NOT VERIFIED",HttpStatus.BAD_REQUEST);
		   
		   userService.registerNewUser(currDTO.getName()  , currDTO.getEmail(), currDTO.getPassword(), Role.CONSUMER); // Default role USER
		
	        return  ResponseEntity.ok(" User registered successfully!" );
	   }
 
}
