package com.msg.smacktalkgaming.backend.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.msg.smacktalkgaming.backend.domain.Player;

public class PlayerUserDetails implements UserDetails {

	private final Player player;

	public PlayerUserDetails(Player player) {
		this.player = player;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Player.SecurityRole[] roles = player.getRole();
		if (roles == null) {
			return Collections.emptyList();
		}
		return Arrays.<GrantedAuthority>asList(roles);
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public String getPassword() {
		return player.getPassword();
	}

	public String getLogin() {
		return player.getLogin();
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
