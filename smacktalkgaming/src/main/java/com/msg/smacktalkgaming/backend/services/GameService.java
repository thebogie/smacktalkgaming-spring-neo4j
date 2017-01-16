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

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Game;
import com.msg.smacktalkgaming.backend.domain.Glicko2;
import com.msg.smacktalkgaming.backend.domain.Location;
import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.domain.Record;
import com.msg.smacktalkgaming.backend.repos.GameRepository;
import com.msg.smacktalkgaming.backend.repos.Glicko2Repository;
import com.msg.smacktalkgaming.backend.repos.LocationRepository;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;
import com.msg.smacktalkgaming.backend.repos.RecordRepository;

import java.util.*;

@Service
@Transactional
public class GameService {
	@Autowired
	GameRepository gRepo;

	public void save(Game g) {
		gRepo.save(g);

	}

	public Game createOrFindGame(Game game) {

		Game retVal = game;

		// try to find player existing with login
		Game foundGame = gRepo.findByName(game.getName());
		// should be only one if it exists, since we are importing unqiue
		// names
		if (null != foundGame) {

			retVal = foundGame;
			// Location newlocation = new Location();
			// retVal = newlocation;

		} else {
			// not there use the one passed in.
			// retVal = found.iterator().next();
		}

		return retVal;

	}

}