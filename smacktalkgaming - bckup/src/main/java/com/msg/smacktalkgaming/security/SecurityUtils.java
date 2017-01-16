package com.msg.smacktalkgaming.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.msg.smacktalkgaming.backend.domain.Player.SecurityRole;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated();
	}

	public static boolean hasRole(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.getAuthorities().contains(SecurityRole.valueOf(role));
	}
}
