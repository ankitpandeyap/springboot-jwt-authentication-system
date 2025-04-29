package com.robspecs.otp.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {

	private static final String SECRET_KEY = "giligilichooyour-secure-secret-key-min-32bytesaabracadabara";
	private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

	// Generate JWT Token
	public String generateToken(String username, long expiryMinutes) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000)) // in milliseconds
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String validateAndExtractUsername(String token) {
		try {
			return Jwts.parserBuilder().
					setSigningKey(key).
					build().
					parseClaimsJws(token).
					getBody().
					getSubject();
		} catch (JwtException | IllegalArgumentException  e) {
			return "";
		}
	}

}
