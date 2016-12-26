package com.msg.smacktalkgaming.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;

import java.util.*;

@Service
@Transactional
public class PlayerService {
	@Autowired
	PlayerRepository pRepo;

	// @Autowired
	AuthenticationManager authenticationManager;

	public Authentication authenticatePlayer(String login, String password) {
		Authentication token = null;
		try {

			// TODO: FIX PLAYER FINDING BY LOGIN NAME
			// is the player in the system?
			Collection<Player> players = pRepo.findByLogin(login);

			if (players == null) {
				throw new BadCredentialsException("1000");
			}

			// TODO: first login with password??
			boolean foundMatch = false;
			for (Player player : players) {
				if (player.comparePassword(password)) {
					Collection<GrantedAuthority> ga = Collections.emptyList();
					Player.SecurityRole[] roles = player.getRoles();
					if (roles != null) {
						ga = Arrays.<GrantedAuthority>asList(roles);
					}

					token = new UsernamePasswordAuthenticationToken(login, password, ga);
					foundMatch = true;
					break;

				}
			}
			/*
			 * if (player.isDisabled()) { throw new DisabledException("1001"); }
			 */
			if (!foundMatch) {
				throw new BadCredentialsException("1000");
			}

		} catch (

		AuthenticationException ex) {
			System.out.println("ERROR: " + ex.toString());
			// ERROR
		}
		return token;
	}

	@Secured("ROLE_ADMIN")
	public String getAllPlayersNames() {

		return null;
		// pRepo.getAllPlayersNames().toString();
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	public String userMethod() {
		return "Hello from a user method";
	}

}