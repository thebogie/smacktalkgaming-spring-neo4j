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
import com.msg.smacktalkgaming.backend.domain.Glicko2;
import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.domain.Record;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;

import java.util.*;

@Service
@Transactional
public class PlayerService {
	@Autowired
	PlayerRepository pRepo;

	// @Autowired
	AuthenticationManager authenticationManager;

	public Player createOrFindPlayer(Player player) {

		Player retVal = player;

		// try to find player existing with login
		Collection<Player> playersFound = pRepo.findByLogin(retVal.getLogin());
		// should be only one if it exists, since we are importing unqiue
		// names
		if (playersFound.isEmpty()) {
			// the player exists, swap out the player in cargo with the
			// previous player

			Glicko2 startrating = new Glicko2();
			startrating.setCreated(new Date());
			retVal.setCurrentrating(startrating);

			ArrayList<Glicko2> newlist = new ArrayList<Glicko2>();
			newlist.add(startrating);
			retVal.setWasRated(newlist);

			pRepo.save(retVal);

		} else {
			retVal = playersFound.iterator().next();
		}

		return retVal;

	}

	public void addNewPlayedIn(Player player, Record record) {

		ArrayList<Record> newlist = new ArrayList<Record>();
		newlist.add(record);

		Collection<Record> listwasrated = player.getRecords();
		if (listwasrated == null) {

			player.setRecords(newlist);

		} else {
			listwasrated.add(record);
			player.setRecords(listwasrated);

		}
		// pRepo.save(player);

	}

	public void addNewWasRated(Glicko2 oldrating, Player player) {

		ArrayList<Glicko2> newlist = new ArrayList<Glicko2>();
		newlist.add(oldrating);

		Collection<Glicko2> listwasrated = player.getWasRated();
		if (listwasrated == null) {

			player.setWasRated(newlist);

		} else {
			listwasrated.add(oldrating);
			player.setWasRated(listwasrated);

		}
		// pRepo.save(player);

	}

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

	public void moveCurrentRatingToNewRating(Event event, Player player) {

		System.out.println("Move Current Rating to New Rating");

		Glicko2 newglicko = new Glicko2();
		Glicko2 oldglicko = player.getCurrentrating();

		newglicko.setRating(oldglicko.getRating());
		newglicko.setRatingdeviation(oldglicko.getRatingdeviation());
		newglicko.setVolatility(oldglicko.getVolatility());
		newglicko.setCreated(event.getStop());
		newglicko.setEventRatingFrom(event);

		player.setCurrentrating(newglicko);
		pRepo.save(player);

		// List<Glicko2> newglickoList = new ArrayList<Glicko2>();
		// newglickoList.add(newglicko);

		addNewWasRated(newglicko, player);

		// return player;
	}

}