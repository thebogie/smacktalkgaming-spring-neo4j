package com.msg.smacktalkgaming.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

	AuthenticationManager authenticationManager;

	public Authentication authenticatePlayer(String login, String password) {
		Authentication token = null;
		try {

			// TODO: FIX PLAYER FINDING BY LOGIN NAME
			// is the player in the system?
			Player player = null;
			// pRepo.findByLoginLikeIgnoreCase(login);

			if (player == null) {
				throw new BadCredentialsException("1000");
			}
			/*
			 * if (player.isDisabled()) { throw new DisabledException("1001"); }
			 */

			if (player.comparePassword(password)) {
				throw new BadCredentialsException("1000");
			}
			token = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

		} catch (AuthenticationException ex) {
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