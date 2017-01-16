package com.msg.smacktalkgaming.backend.services;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Game;
import com.msg.smacktalkgaming.backend.domain.Glicko2;
import com.msg.smacktalkgaming.backend.domain.Location;
import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.domain.Record;
import com.msg.smacktalkgaming.backend.domain.Record.enumContestResults;
import com.msg.smacktalkgaming.backend.domain.WasRated;
import com.msg.smacktalkgaming.backend.domain.rating.Rating;
import com.msg.smacktalkgaming.backend.domain.rating.RatingCalculator;
import com.msg.smacktalkgaming.backend.domain.rating.RatingPeriodResults;
import com.msg.smacktalkgaming.backend.entities.RecordResults;
import com.msg.smacktalkgaming.backend.repos.EventRepository;
import com.msg.smacktalkgaming.backend.repos.GameRepository;
import com.msg.smacktalkgaming.backend.repos.Glicko2Repository;
import com.msg.smacktalkgaming.backend.repos.LocationRepository;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;
import com.msg.smacktalkgaming.backend.repos.RecordRepository;
import com.msg.smacktalkgaming.backend.repos.WasRatedRepository;

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
	WasRatedRepository wRepo;

	@Autowired
	GameService gService;

	@Autowired
	Glicko2Repository glicko2Repo;

	@Autowired
	LocationService lService;

	public String RecordEvent(Event event, Game game, Location location, ArrayList<Player> players,
			ArrayList<Record> records) {

		System.out.println("Recording Event: " + event.getEventname());

		class RatingHolder {
			boolean counted;
			Record record;
			Player player;
			Rating rating;
		}

		Game gametouse = gService.createOrFindGame(game);
		HashSet<Game> games = new HashSet<Game>();
		games.add(gametouse);
		event.setPlayedWith(games);

		Location locationtouse = lService.createOrFindLocation(location);
		event.setLocation(locationtouse);

		ArrayList<RatingHolder> ratingholder = new ArrayList<RatingHolder>();
		RatingCalculator ratingSystem = new RatingCalculator();
		RatingPeriodResults results = new RatingPeriodResults();

		// should be the same as records... or error
		for (int i = 0; i < players.size(); i++) {

			RatingHolder cargo = new RatingHolder();

			cargo.counted = false;
			cargo.player = pService.createOrFindPlayer(players.get(i));
			System.out.println(cargo.player.toString());
			cargo.player.setCurrentevent(event.getUUID());

			Glicko2 currentrating = pService.getCurrentRating(cargo.player);
			cargo.rating = new Rating(cargo.player.getLogin(), ratingSystem);

			cargo.rating.setRating(currentrating.getRating());
			cargo.rating.setRatingDeviation(currentrating.getRatingdeviation());
			cargo.rating.setVolatility(currentrating.getVolatility());

			cargo.record = records.get(i);

			System.out.println("Player: " + cargo.player.getFirstname() + //
					" with record :" + cargo.record.getPlace() + ":" //
					+ cargo.record.getResult());
			System.out.println(//
					"Rating: " + cargo.rating.getRating() + //
							" Deviation:" + cargo.rating.getRatingDeviation() + //
							" Vol:" + cargo.rating.getVolatility());

			records.get(i).setPlayer(cargo.player);
			records.get(i).setEvent(event);

			ratingholder.add(cargo);

			pService.save(cargo.player);
			pService.saveRecord(cargo.record);

		}

		for (RatingHolder primary : ratingholder) {

			for (RatingHolder quest : ratingholder) {
				if (!quest.counted && //
						!quest.player.getUUID().equals(primary.player.getUUID())) {

					System.out.println("comparing: " + primary.player.getFirstname() //
							+ "place:" + primary.record.getPlace() //
							+ " and " + quest.player.getFirstname() //
							+ "place:" + quest.record.getPlace());

					// gotta match primary with cargo... or we cant save
					// ratings.
					if (primary.record.getPlace() <= quest.record.getPlace()) {
						if (primary.record.getPlace() == quest.record.getPlace()) {
							results.addDraw(primary.rating, quest.rating);
						} else {
							results.addResult(primary.rating, quest.rating);
						}

					} else {
						results.addResult(quest.rating, primary.rating);
					}
				}
				primary.counted = true;

			}
		}

		ratingSystem.updateRatings(results);

		System.out.println("******AFTER EVENT*******");
		for (RatingHolder cargo : ratingholder) {

			Glicko2 newglicko = new Glicko2();

			newglicko.setRating(cargo.rating.getRating());
			newglicko.setRatingdeviation(cargo.rating.getRatingDeviation());
			newglicko.setVolatility(cargo.rating.getVolatility());
			newglicko.setCreated(event.getStop());
			newglicko.setEventRatingFrom(event);

			// if (cargo.player.getCurrentrating() != null) {

			// wasrated.add(cargo.player.getCurrentrating());
			// cargo.player.setWasRated(wasrated);
			// }

			WasRated wasrated = new WasRated();
			wasrated.setPlayer(cargo.player);
			wasrated.setGlicko2(newglicko);

			// glicko2Repo.save(newglicko);
			pService.save(cargo.player);
			pService.saveWasRated(wasrated);
			pService.setCurrentRating(cargo.player, newglicko);

			System.out.println("Player: " + cargo.player.getFirstname() + //
					" with record :" + cargo.record.getPlace() + ":" //
					+ cargo.record.getResult());
			System.out.println(//
					"Rating: " + cargo.rating.getRating() + //
							" Deviation:" + cargo.rating.getRatingDeviation() + //
							" Vol:" + cargo.rating.getVolatility());

		}

		/*
		 * for (int i = 0; i < players.size(); i++) { Player playertosave =
		 * pService.createOrFindPlayer(players.get(i));
		 * System.out.println("Player Name: " + playertosave.getLogin() +
		 * "*Player ID:" + playertosave.getID());
		 * System.out.println("With record: " + records.get(i).toString());
		 * 
		 * records.get(i).setPlayer(playertosave);
		 * records.get(i).setEvent(event);
		 * 
		 * pRepo.save(playertosave); rRepo.save(records.get(i)); //
		 * players.set(i, playertosave);
		 * 
		 * }
		 */

		// UPDATE RATINGS HERE TOO TO AVOID THE CONTEXT ISSUES WE HAVE BEEN
		// HAVING...

		eRepo.save(event);
		System.out.println(event.toString());
		// lRepo.save(location);
		// gRepo.save(game);

		return event.getUUID();

	}

	public void UpdateRatingsFromEvent(String eventuuid) {
		Event event = eRepo.findByUUID(eventuuid);

		System.out.println("UPDATINGEVENT: " + event.toString());

		Collection<String> playersuuid = eRepo.getPlayersUUIDInEvent(event.getUUID());
		// Collection<RecordResults> records =
		// eRepo.fromEventGetPlayersRecords(event.getUUID());

		for (String playeruuid : playersuuid) {
			Player player = pRepo.findOne(pRepo.findIDByUUID(playeruuid));
			System.out.println("EVENTS PLAYEDIN for " + playeruuid + ":" //
					+ "** " + player.toString() + " **" //
					+ pRepo.numberEventsPlayedIn(playeruuid));
			Glicko2 newglicko = new Glicko2();
			newglicko.setRating(System.currentTimeMillis() % 1000);
			glicko2Repo.save(newglicko);

			// player.setCurrentrating(null);
			player.setAlignment("FISH");
			pService.save(player);

		}
		/*
		 * Player player = pRepo.findOne(pRepo.findIDByUUID(playeruuid));
		 * System.out.println("EVENTS PLAYEDIN for " + playeruuid + ":" // +
		 * "** " + player.toString() + " **" // +
		 * pRepo.numberEventsPlayedIn(playeruuid));
		 * 
		 * Glicko2 newglicko = new Glicko2();
		 * newglicko.setRating(System.currentTimeMillis() % 1000);
		 * 
		 * // newglicko.setPlayerCurrentRating(player);
		 * player.setCurrentrating(newglicko);
		 * 
		 * // pRepo.save(player); // eRepo.save(event); // pRepo.save(player);
		 * // pRepo.save(player); // glicko2Repo.save(newglicko);
		 * 
		 * }
		 */
		// eRepo.save(event);
	}

	public void NotWorkingUpdateRatingsFromEvent(Event event) {

		// Event event = eRepo.findByUUID(eventuuid);
		// Event event = eRepo.findOne(eRepo.findIDByUUID(eventuuid));
		System.out.println("***************UPDATING EVENT:" + event.getEventname());
		Collection<String> playersuuid = eRepo.getPlayersUUIDInEvent(event.getUUID());
		// Collection<RecordResults> records =
		// eRepo.fromEventGetPlayersRecords(event.getUUID());

		for (String playeruuid : playersuuid) {
			Player player = pRepo.findOne(pRepo.findIDByUUID(playeruuid));
			System.out.println("EVENTS PLAYEDIN for " + playeruuid + ":" //
					+ "** " + player.toString() + " **" //
					+ pRepo.numberEventsPlayedIn(playeruuid));

			Glicko2 newglicko = new Glicko2();
			newglicko.setRating(System.currentTimeMillis() % 1000);

			// newglicko.setPlayerCurrentRating(player);
			player.setCurrentrating(newglicko);

			// pRepo.save(player);
			// eRepo.save(event);
			// glicko2Repo.save(newglicko);

		}
		// eRepo.save(event);

	}

	private void updateToLatestRatings(Glicko2 newglicko2) {

	}

	public boolean testUpdateRatingsFromEvent(String eventuuid) {
		boolean retVal = false;

		Event event = eRepo.findByUUID(eventuuid);
		System.out.println("***************UPDATING EVENT:" + event.getEventname());
		Collection<String> playersuuid = eRepo.getPlayersUUIDInEvent(event.getUUID());
		Collection<RecordResults> records = eRepo.fromEventGetPlayersRecords(event.getUUID());

		class RatingHolder {
			boolean counted;
			RecordResults record;
			Player player;
			Rating rating;
		}

		ArrayList<RatingHolder> ratingholder = new ArrayList<RatingHolder>();
		RatingCalculator ratingSystem = new RatingCalculator();
		RatingPeriodResults results = new RatingPeriodResults();

		Iterator i = records.iterator();
		while (i.hasNext()) {

			RecordResults record = (RecordResults) i.next();
			RatingHolder cargo = new RatingHolder();

			cargo.counted = false;
			cargo.player = pRepo.findByUUID(record.getPlayeruuid());
			cargo.player.setCurrentevent(event.getUUID());

			cargo.rating = new Rating(cargo.player.getLogin(), ratingSystem);
			cargo.record = record;

			ratingholder.add(cargo);

		}

		for (String playeruuid : playersuuid) {
			RatingHolder primary = new RatingHolder();

			// find PRIMARY player
			int primaryPlayer = -1;
			for (int j = 0; j < ratingholder.size(); j++) {

				if (ratingholder.get(j).player.getUUID().equals(playeruuid)) {
					primary = ratingholder.get(j);
					System.out.println("PRIMARY Player Login:" + primary.player.getLogin());
					break;
				}

			}
			// System.out.println("CARGO:" + cargo.toString());
			// System.out.println(
			// "Player Login:" + player.getLogin() + " GLICKO2 UUID:" +
			// player.getCurrentrating().getUUID());
			// System.out.println(
			// "Player Login:" + player.getLogin() + " GLICKO2 RATING:" +
			// player.getCurrentrating().getRating());

			for (RatingHolder cargo : ratingholder) {

				if (!cargo.counted && //
						!cargo.player.getUUID().equals(primary.player.getUUID())) {

					if (primary.record.getPlace() <= cargo.record.getPlace()) {
						if (primary.record.getPlace() == cargo.record.getPlace()) {
							results.addDraw(primary.rating, cargo.rating);
						} else {
							results.addResult(primary.rating, cargo.rating);
						}

					} else {
						results.addResult(cargo.rating, primary.rating);
					}
				}
				primary.counted = true;

			}
		}

		ratingSystem.updateRatings(results);

		for (RatingHolder cargo : ratingholder) {

			Glicko2 newglicko = new Glicko2();

			newglicko.setRating(cargo.rating.getRating());
			newglicko.setRatingdeviation(cargo.rating.getRatingDeviation());
			newglicko.setVolatility(cargo.rating.getVolatility());
			newglicko.setCreated(event.getStop());
			newglicko.setEventRatingFrom(event);

			// cargo.player.addWasRated(cargo.player.getCurrentrating());
			cargo.player.setCurrentrating(newglicko);
			pRepo.save(cargo.player);

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

			// pRepo.save(player);

		}

		return retVal;

	}

}
