package com.msg.smacktalkgaming.backend.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.ogm.annotation.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Game;
import com.msg.smacktalkgaming.backend.domain.Glicko2;
import com.msg.smacktalkgaming.backend.domain.Location;
import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.domain.Record;
import com.msg.smacktalkgaming.backend.domain.Record.enumContestResults;
import com.msg.smacktalkgaming.backend.domain.rating.Rating;
import com.msg.smacktalkgaming.backend.domain.rating.RatingCalculator;
import com.msg.smacktalkgaming.backend.domain.rating.RatingPeriodResults;
import com.msg.smacktalkgaming.backend.repos.EventRepository;
import com.msg.smacktalkgaming.backend.repos.GameRepository;
import com.msg.smacktalkgaming.backend.repos.Glicko2Repository;
import com.msg.smacktalkgaming.backend.repos.LocationRepository;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;
import com.msg.smacktalkgaming.backend.repos.RecordRepository;

@Service
@Transactional
public class EventService {
	@Autowired
	PlayerService pService;

	@Autowired
	EventRepository eRepo;

	@Autowired
	PlayerRepository pRepo;

	@Autowired
	RecordRepository rRepo;

	@Autowired
	GameRepository gRepo;

	@Autowired
	Glicko2Repository glicko2Repo;

	@Autowired
	LocationRepository lRepo;

	public String RecordEvent(Event event, Game game, Location location, ArrayList<Player> players,
			ArrayList<Record> records) {

		System.out.println("Recording Event: " + event.getEventname());

		Game g = gRepo.findByName(game.getName());
		if (g != null) {
			// game already there!
			game = g;

		}

		List<Game> listofgames = new ArrayList<Game>();
		listofgames.add(game);
		event.setGames(listofgames);

		Location l = lRepo.findByLocation(location.getLocation());
		if (l != null) {
			// location already exists!
			location = l;

		}

		event.setLocation(location);

		// should be the same as records... or error
		for (int i = 0; i < players.size(); i++) {
			Player playertosave = pService.createOrFindPlayer(players.get(i));
			System.out.println("Player Name: " + playertosave.getLogin() + "*Player ID:" + playertosave.getID());

			records.get(i).setPlayer(playertosave);
			records.get(i).setEvent(event);

			pService.addNewPlayedIn(playertosave, records.get(i));

			pRepo.save(playertosave);
			// players.set(i, playertosave);

		}

		eRepo.save(event);

		return event.getUUID();

	}

	public void UpdateRatingsFromEvent(String eventuuid) {

		Event event = eRepo.findByUUID(eventuuid);
		System.out.println("***************UPDATING EVENT:" + event.getEventname());
		Collection<String> playersuuid = eRepo.getPlayersUUIDInEvent(event.getUUID());

		for (String playeruuid : playersuuid) {

			Player player = pRepo.findByUUID(playeruuid);
			player.setCurrentevent(event.getUUID());
			System.out.println("Player Login:" + player.getLogin() + "  UUID:" + player.getUUID());
			// System.out.println("CARGO:" + cargo.toString());
			// System.out.println(
			// "Player Login:" + player.getLogin() + " GLICKO2 UUID:" +
			// player.getCurrentrating().getUUID());
			// System.out.println(
			// "Player Login:" + player.getLogin() + " GLICKO2 RATING:" +
			// player.getCurrentrating().getRating());

			Glicko2 newglicko2 = new Glicko2();

			newglicko2.setRating(0);
			newglicko2.setRatingdeviation(0);
			newglicko2.setVolatility(0);

			pService.addNewWasRated(player.getCurrentrating(), player);
			player.setCurrentrating(newglicko2);

			// System.out.println("AFTER Player Login:" + player.getLogin() + "
			// UUID:" + player.getUUID());
			// System.out.println("AFTER Player Login:" + player.getLogin() + "
			// GLICKO2 UUID:"
			// + player.getCurrentrating().getUUID());
			// System.out.println("AFTER Player Login:" + player.getLogin() + "
			// GLICKO2 RATING:"
			// + player.getCurrentrating().getRating());
			// pRepo.save(player);
			//
			// pRepo.save(player);

			pRepo.save(player);

		}

		eRepo.save(event);

		for (String playeruuid : playersuuid) {
			Player player = pRepo.findByUUID(playeruuid);
			Record record = pRepo.getRecordForPlayerFromEvent(eventuuid, playeruuid);
			System.out.println("record: " + record.getPlace());
			for (String compareuuid : playersuuid) {
				Player compare = pRepo.findByUUID(compareuuid);
				if (!player.getUUID().equals(compare.getUUID())) {
					// compare records
					Record comparerecord = pRepo.getRecordForPlayerFromEvent(eventuuid, compareuuid);

					System.out.println("comparerecord: " + comparerecord.getPlace());
				}

			}
			System.out.println("AFTER Player Login:" + player.getLogin() //
					+ " GLICKO2 RATING:" + player.getCurrentrating().getRating());
		}

	}

	private void updateToLatestRatings(Glicko2 newglicko2) {

	}

	public boolean testUpdateRatingsFromEvent(Event event) {
		boolean retVal = false;

		System.out.println("Update Ratings");

		Collection<Player> players = eRepo.getPlayersInEvent(event.getUUID());
		Collection<Record> records = eRepo.fromEventGetPlayersRecords(event.getUUID());

		RatingCalculator ratingSystem = new RatingCalculator();
		RatingPeriodResults results = new RatingPeriodResults();

		for (Player player : players) {

			Glicko2 currentRating = player.getCurrentrating();
			System.out.println("player:" + player.getLogin() + " currentrating:" + currentRating.getRating());

			currentRating.setRating(System.currentTimeMillis());

			pRepo.save(player);

		}
		Collection<Player> playersA = eRepo.getPlayersInEvent(event.getUUID());
		for (Player player : playersA) {

			Glicko2 currentRating = player.getCurrentrating();
			System.out.println("player:" + player.getLogin() + " currentrating:" + currentRating.getRating());

		}

		return retVal;

	}

}