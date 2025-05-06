package com.robspecs.otp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.robspecs.otp.dto.RegistrationDTO;
import com.robspecs.otp.entity.User;
import com.robspecs.otp.enums.Role;
import com.robspecs.otp.repository.UserRepository;
import com.robspecs.otp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	 private UserRepository userRepository;
	 private PasswordEncoder passwordEncoder;
	 private final StringRedisTemplate redisTemplate;




	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,StringRedisTemplate redisTemplate) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.redisTemplate = redisTemplate;
	}





	@Override
	public User registerNewUser(RegistrationDTO currDTO) {
		// TODO Auto-generated method stub
		   String email =  currDTO.getEmail();
		   if (userRepository.existsByEmail(email)) {
	           User user =  userRepository.findByEmail(email).get();
	           if(user.isEnabled())
			   throw new RuntimeException("Email already registered!");
	          
		   }
            Role role  = currDTO.getRole().equalsIgnoreCase("Consumer") ? Role.CONSUMER : Role.ADMIN;
   
	        User user = new User();
	        user.setName(currDTO.getName());
	        user.setEmail(email);
	        user.setPassword(passwordEncoder.encode(currDTO.getPassword())); // Always hash password!
	        user.setRole(role);
	        user.setEnabled(true);
	        this.redisTemplate.opsForValue().getAndDelete(email);
	        return userRepository.save(user);
	    }
	}


