package com.robspecs.otp.service;

import com.robspecs.otp.entity.User;
import com.robspecs.otp.enums.Role;

public interface UserService {


	User registerNewUser(String name, String email, String rawPassword, Role role);



}
