package com.robspecs.otp.security;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.robspecs.otp.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTRefreshFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;

	public JWTRefreshFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			CustomUserDetailsService customUserDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Boolean isExpiredToken = (Boolean) request.getAttribute("expiredToken");// null can be evaluated top false
		boolean isRefreshRequest = request.getServletPath().equals("/api/auth/refresh");
		boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() != null;


		// If token is NOT expired AND user is authenticated OR it's NOT a refresh call
		if (((isExpiredToken == null || !isExpiredToken) && isAuthenticated) 
			    || isRefreshRequest)  {
		    filterChain.doFilter(request, response);
		    return;
		}

		try {
			String usernameFromAccessToken = (String) request.getAttribute("expiredTokenUsername");

			String refreshToken = extractJwtFromRequest(request);
			if (refreshToken == null) {
				throw new AuthenticationException("Refresh Token is Invalid or not present");
			}
			String usernameFromRefreshToken = jwtUtil.validateAndExtractUsername(refreshToken);
			if (!usernameFromAccessToken.equals(usernameFromRefreshToken)) {
				throw new AuthenticationException("Refresh Token is Invalid or not present");
			}

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(usernameFromRefreshToken);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());

			String newAccessToken = jwtUtil.generateToken(userDetails.getUsername(), 15);


			response.setHeader("Authorization", "Bearer " + newAccessToken);

			SecurityContextHolder.getContext().setAuthentication( authToken);

			filterChain.doFilter(request, response);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Refresh Token is Inavlaid or Expired 	 " + e.getMessage());
			return;
		}
	}

	private String extractJwtFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		String refreshToken = null;
		for (Cookie cookie : cookies) {
			if ("refreshToken".equals(cookie.getName())) {
				refreshToken = cookie.getValue();
			}
		}
		return refreshToken;
	}

}
