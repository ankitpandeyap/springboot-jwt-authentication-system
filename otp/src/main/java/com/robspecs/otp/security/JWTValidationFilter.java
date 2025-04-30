package com.robspecs.otp.security;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.robspecs.otp.exception.TokenNotFoundException;
import com.robspecs.otp.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.MissingClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTValidationFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;

	public JWTValidationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			CustomUserDetailsService customUserDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String token = extractTokenFromRequest(request);
			String usernameFromToken = jwtUtil.validateAndExtractUsername(token);
			UserDetails currentUser = customUserDetailsService.loadUserByUsername(usernameFromToken);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(currentUser, null,
					currentUser.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authToken);

		} catch (TokenNotFoundException e) {

			throw new AuthenticationException("Token not found: " + e.getMessage(), e);
		} catch (MissingClaimException e) {

			throw new AuthenticationException("Missing claim in token: " + e.getMessage(), e);
		} catch (InvalidClaimException e) {

			throw new AuthenticationException("Invalid claim in token: " + e.getMessage(), e);
		} catch (UsernameNotFoundException e) {

			throw new AuthenticationException("Username not found: " + e.getMessage(), e);
		} catch (ExpiredJwtException e) {

			request.setAttribute("expiredToken", true);
			request.setAttribute("expiredTokenUsername", e.getClaims().getSubject());
		} catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
			throw new AuthenticationException("Authentication failed: " + e.getLocalizedMessage(), e);
			//coomnce here own exception classes
		}
		filterChain.doFilter(request, response);
	}

	private String extractTokenFromRequest(HttpServletRequest request) throws TokenNotFoundException {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		throw new TokenNotFoundException(request.getLocalName() + " request doesn't contain token");
	}

}
