package com.robspecs.otp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.robspecs.otp.entity.User;
import com.robspecs.otp.enums.Role;
import com.robspecs.otp.repository.UserRepository;
import com.robspecs.otp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	 private UserRepository userRepository;
	 private PasswordEncoder passwordEncoder;
	
	 
	 
	 
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}





	@Override
	public User registerNewUser(String name, String email, String rawPassword, Role role) {
		// TODO Auto-generated method stub
		   if (userRepository.existsByEmail(email)) {
	            throw new RuntimeException("Email already registered!");
	        }
		    

	        User user = new User();
	        user.setName(name);
	        user.setEmail(email);
	        user.setPassword(passwordEncoder.encode(rawPassword)); // Always hash password!
	        user.setRole(role);

	        return userRepository.save(user);
	    }
	}


