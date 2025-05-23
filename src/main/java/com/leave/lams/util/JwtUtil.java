package com.leave.lams.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.leave.lams.service.TokenBlacklistService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	@Autowired
	private TokenBlacklistService blacklistService;

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		if (blacklistService.isTokenBlacklisted(token)) {
			return false;
		}
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder()
				.setSubject(userDetails.getUsername())	// sets the subject claim as username(which is unique)
				.claim("roles", userDetails.getAuthorities())	// adding a claim named "roles".
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 min
				.signWith(key)
				.compact();
	}

	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	private Claims getClaims(String token) {
	    return Jwts.parserBuilder() // Step 1: Start building a JWT parser
	            .setSigningKey(key) // Step 2: Provide the signing key used to sign the JWT
	            .build()            // Step 3: Build the parser instance
	            .parseClaimsJws(token) // Step 4: Parse the JWT and verify its signature (JWS)
	            .getBody();         // Step 5: Extract the Claims (payload) from the verified JWT
	}

}
