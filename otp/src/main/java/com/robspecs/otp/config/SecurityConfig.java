package com.robspecs.otp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.robspecs.otp.security.CustomUserDetailsService;
import com.robspecs.otp.security.JWTAuthenticationEntryPoint;
import com.robspecs.otp.security.JWTAuthenticationFilter;
import com.robspecs.otp.util.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;

	private final JWTUtil jwtUtils;

	private final CustomUserDetailsService userDetailsService;

	private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	public SecurityConfig(JWTUtil jwtUtils, CustomUserDetailsService userDetailsService,
			JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint, AuthenticationManager authenticationManager) {
		super();
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		this.authenticationManager = authenticationManager;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(AuthenticationManager authenticationManager,HttpSecurity http,JWTUtil jwtUtils) throws Exception {
		JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(authenticationManager, jwtUtils);
        
		return http.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated())
				   .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				   .build();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	   @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
	            throws Exception {
	        return configuration.getAuthenticationManager();
	    }
}
