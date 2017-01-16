package com.msg.smacktalkgaming;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msg.smacktalkgaming.MyNeo4jTestConfiguration;
import com.msg.smacktalkgaming.backend.domain.*;
import com.msg.smacktalkgaming.backend.domain.Player.SecurityRole;
import com.msg.smacktalkgaming.backend.domain.Record.enumContestResults;
import com.msg.smacktalkgaming.backend.entities.RecordResults;
import com.msg.smacktalkgaming.backend.repos.*;
import com.msg.smacktalkgaming.backend.services.EventService;
import com.msg.smacktalkgaming.backend.services.PlayerService;

import com.msg.smacktalkgaming.randomperson.RandomPerson;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MyNeo4jTestConfiguration.class)
@ActiveProfiles({ "test", "embedded" })
public class DomainTest {
	// @Autowired
	// private PlayerRepository playerRepository;

	// String nowAsISO, plusHourAsISO;
	ZonedDateTime utcNow, utcTwoHoursFromNow, birthdayTime;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private EventService eventService;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private RecordRepository recordRepository;

	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private PlayerService playerService;

	@Autowired
	private Glicko2Repository glicko2Repository;

	public DomainTest() {
	}

	@Before
	public void initialize() throws ParseException {

		// save date in UTC format
		utcNow = ZonedDateTime.now(ZoneOffset.UTC);
		utcTwoHoursFromNow = utcNow.plusHours(2);
		birthdayTime = ZonedDateTime.parse("1995-12-03T00:00+00:00[UTC]");

		/*
		 * TimeZone tz = TimeZone.getTimeZone("UTC"); DateFormat df = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted // "Z" // to //
		 * indicate // UTC, // no // timezone // offset df.setTimeZone(tz);
		 * 
		 * Date d1 = new Date(); Calendar cl = Calendar.getInstance();
		 * cl.setTime(d1); cl.add(Calendar.HOUR, 1); nowAsISO = df.format(d1);
		 * plusHourAsISO = df.format(cl.getTime());
		 * System.out.println("Set ISO Time:" + nowAsISO);
		 * System.out.println("Set ISO Time + hour:" + plusHourAsISO);
		 */

	}

	private Game createUniqueGame() {
		Game retVal;

		/**** GAME ****/
		Game game = new Game();
		game.setName("GAME:" + utcNow.toString());
		game.setPublished("PUBLISH:" + utcNow.toString());
		game.setBgglink("http://cnn.com");
		retVal = game;

		return retVal;
	}

	private Player createUniquePlayer() {
		Player player = new Player();
		ZonedDateTime snapshot = ZonedDateTime.now(ZoneOffset.UTC);
		String playerName = "PLAYER " + snapshot.toString();

		player.setFirstname(playerName);
		player.setSurname("SURNAME:" + snapshot.toString());
		player.setNickname("NICK:" + snapshot.toString());
		// player.setCurrentevent("NICK");
		// player.setBirthdate("11/11/2000");
		// player.setBirthdate(new GregorianCalendar(1995, Calendar.FEBRUARY,
		// 11).getTime());
		player.setBirthdate(Date.from(birthdayTime.toInstant()));
		player.setAlignment("LAWFUL GOOD");
		player.setLogin("thebogie@live.com");
		player.setPassword("fish");
		player.setInfo("fish2");
		player.setRole(SecurityRole.ROLE_USER, SecurityRole.ROLE_ADMIN);

		try {

			ObjectMapper mapper = new ObjectMapper();
			com.msg.smacktalkgaming.randomperson.RandomPerson //
			randomperson = mapper.readValue(new URL("https://randomuser.me/api/"),
					com.msg.smacktalkgaming.randomperson.RandomPerson.class);

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			player.setFirstname(randomperson.getResults().get(0).getName().getFirst());
			player.setBirthdate(formatter.parse(randomperson.getResults().get(0).getDob()));
			player.setAlignment("LAWFUL GOOD");
			player.setLogin(randomperson.getResults().get(0).getEmail());
			player.setPassword(randomperson.getResults().get(0).getLogin().getPassword());
			System.out.println("Player:" + player.getFirstname() + "*login:" + player.getLogin() + "*password:"//
					+ randomperson.getResults().get(0).getLogin().getPassword());

			player.setInfo(randomperson.getResults().get(0).getLogin().getPassword());

		} catch (IOException | ParseException ex) {
			// | JsonParseException | JsonMappingException |
			// MalformedURLException
			System.out.println("randomuser.me failed:" + ex);

		}
		return player;

	}

	private Event createUniqueEvent() {
		Event event = new Event();
		ZonedDateTime snapshot = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime twohrslater = snapshot.plusHours(2);
		String eventName = "Event " + snapshot.toString();

		event.setStart(Date.from(snapshot.toInstant()));
		event.setStop(Date.from(twohrslater.toInstant()));

		return event;

	}

	private Location createLocation() {
		Location retVal = new Location();
		Random randomGenerator = new Random();

		ArrayList<String> randomlocs = new ArrayList<String>();

		randomlocs.add("1 N. Sheffield Ave. Danville VA ");
		randomlocs.add("9 W. Valley Street Fitchburg, MA ");
		randomlocs.add("21 Riverview Dr. Freeport, NY ");
		randomlocs.add("41 George Street District Heights, MD ");
		randomlocs.add("7255 North Second St. Temple Hills, MD ");
		randomlocs.add("8108 Manor Lane Banning, CA 92220");

		int index = randomGenerator.nextInt(randomlocs.size() - 1);
		retVal.setLocation(randomlocs.get(index));
		return retVal;
	}

	@Test
	public void updateRatingsFromMultipleEvents() {

		// HashMap testset = updateRatingsFromEvent();

		// HashMap testset2 = updateRatingsFromEvent();

		// Event event = createUniqueEvent();
		// String eventUUID = event.getUUID();

	}

	@Test
	public void shouldBeAbleToRecordAnEvent() {

		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Record> records = new ArrayList<Record>();

		Game game = createUniqueGame();
		String gameUUID = game.getUUID();

		Location location = createLocation();
		String locationUUID = location.getUUID();

		// 4 players
		// 1 game
		// WON, LOST, LOST, LOST scenario
		// Event
		Player player1 = createUniquePlayer();
		String player1UUID = player1.getUUID();

		Player player2 = createUniquePlayer();
		String player2UUID = player2.getUUID();

		Player player3 = createUniquePlayer();
		String player3UUID = player3.getUUID();

		Player player4 = createUniquePlayer();
		String player4UUID = player4.getUUID();

		Event event = createUniqueEvent();
		String eventUUID = event.getUUID();

		Record playedin1 = new Record();
		playedin1.setPlayer(player1);
		playedin1.setPlace(1);
		playedin1.setResult(enumContestResults.WON);
		playedin1.setEvent(event);
		String playedin1UUID = playedin1.getUUID();

		Record playedin2 = new Record();
		playedin2.setPlayer(player2);
		playedin2.setPlace(2);
		playedin2.setResult(enumContestResults.LOST);
		playedin2.setEvent(event);
		String playedin2UUID = playedin2.getUUID();

		Record playedin3 = new Record();
		playedin3.setPlayer(player3);
		playedin3.setPlace(3);
		playedin3.setResult(enumContestResults.LOST);
		playedin3.setEvent(event);
		String playedin3UUID = playedin3.getUUID();

		Record playedin4 = new Record();
		playedin4.setPlayer(player4);
		playedin4.setPlace(4);
		playedin4.setResult(enumContestResults.LOST);
		playedin4.setEvent(event);
		String playedin4UUID = playedin4.getUUID();

		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);

		records.add(playedin1);
		records.add(playedin2);
		records.add(playedin3);
		records.add(playedin4);

		String recordedEventUUID = eventService.RecordEvent(event, game, location, players, records);

		// first place
		assertEquals(1799.62, playerService.getCurrentRating(player1).getRating(), 0.01);
		assertEquals(227.73, playerService.getCurrentRating(player1).getRatingdeviation(), 0.01);
		assertEquals(0.060001, playerService.getCurrentRating(player1).getVolatility(), 0.01);

		// sceond place
		assertEquals(1599.87, playerService.getCurrentRating(player2).getRating(), 0.01);
		assertEquals(227.73, playerService.getCurrentRating(player2).getRatingdeviation(), 0.01);
		assertEquals(0.059998, playerService.getCurrentRating(player2).getVolatility(), 0.01);

		// third place
		assertEquals(1400.12, playerService.getCurrentRating(player3).getRating(), 0.01);
		assertEquals(227.73, playerService.getCurrentRating(player3).getRatingdeviation(), 0.01);
		assertEquals(0.059998, playerService.getCurrentRating(player3).getVolatility(), 0.01);

		// last place
		assertEquals(1200.37, playerService.getCurrentRating(player4).getRating(), 0.01);
		assertEquals(227.73, playerService.getCurrentRating(player4).getRatingdeviation(), 0.01);
		assertEquals(0.060001, playerService.getCurrentRating(player4).getVolatility(), 0.01);

		// retVal.put("player1", player1UUID);
		//// retVal.put("player2", player2UUID);
		// retVal.put("player3", player3UUID);
		// retVal.put("player4", player4UUID);
		// retVal.put("event1", eventUUID);

	}

	@Test
	public void shouldBeAbleToConnectEventToGame() {

	}

	@Test
	public void shouldBeAbleToConnectEventToLocation() {

	}

	@Test
	public void shouldBeAbleToConnectNewPlayerToNewEvent() {

		Player player1 = createUniquePlayer();
		String player1UUID = player1.getUUID();

		Event event1 = createUniqueEvent();
		String event1UUID = event1.getUUID();

		Record playedin = new Record();
		playedin.setPlayer(player1);
		playedin.setPlace(3);
		playedin.setResult(enumContestResults.WON);
		playedin.setEvent(event1);
		String playedinUUID = playedin.getUUID();

		playerRepository.save(player1);
		eventRepository.save(event1);
		recordRepository.save(playedin);

		// eventService.setRatingFromEvent(event1, player1);

		Record foundplayedin = recordRepository.findByUUID(playedinUUID);
		System.out.println("VLAUED:" + foundplayedin.getResult() + "*");

		// TODO: assert values and delete everything

	}

	@Test
	public void shouldBeAbleToAddAllAndDeleteDomainObjs() {

		/**** LOCATION ****/
		Location location = new Location();
		location.setLocation("2613 W 10th, Austin, TX 78703");
		location.setLatitude(24.0304903);
		location.setLongitude(-38.304903);
		locationRepository.save(location);

		Location locationFound = locationRepository.findByLocation("2613 W 10th, Austin, TX 78703");

		System.out.println("UUID:" + locationFound.getUUID());
		assertEquals(24.0304903, locationFound.getLatitude(), 1e-15);
		assertEquals(-38.304903, locationFound.getLongitude(), 1e-15);

		locationRepository.delete(locationFound);

		locationFound = locationRepository.findByLocation("2613 W 10th, Austin, TX 78703");
		assertEquals(null, locationFound);

		/**** PLAYER ****/
		/* TODO: TEST EMPTY ARRAY OF ROLES */
		Player player = new Player();
		player.setFirstname("FIRSTNAME:" + utcNow.toString());
		player.setSurname("SURNAME:" + utcNow.toString());
		player.setNickname("NICK");
		player.setCurrentevent("NICK");
		// player.setBirthdate("11/11/2000");
		// player.setBirthdate(new GregorianCalendar(1995, Calendar.FEBRUARY,
		// 11).getTime());
		player.setBirthdate(Date.from(birthdayTime.toInstant()));
		player.setAlignment("ALIGNEMNT:" + utcNow.toString());
		player.setLogin("thebogie@live.com");
		player.setPassword("fish");
		player.setInfo("fish2");
		player.setRole(SecurityRole.ROLE_USER, SecurityRole.ROLE_ADMIN);
		playerRepository.save(player);

		Player foundplayer;
		Collection<Player> foundplayers;

		// FIND BY login and password
		foundplayers = playerRepository.findByLogin("thebogie@live.com");

		String playerUUID = null;
		boolean passwordMatch = false;
		// TODO: multople nodes with same email?
		for (Iterator iterator = foundplayers.iterator(); iterator.hasNext();) {
			Player p = (Player) iterator.next();

			if (p.comparePassword("fish")) {
				playerUUID = p.getUUID();
				passwordMatch = true;
				break;
			}

		}

		assertEquals(passwordMatch, true);

		playerRepository.delete(playerRepository.findByUUID(playerUUID));

		// FIND BY UUID
		foundplayer = playerRepository.findByUUID(playerUUID);
		assertEquals(null, foundplayer);

		/**** GAME ****/
		Game game = new Game();
		game.setName("GAME:" + utcNow.toString());
		game.setPublished("PUBLISH:" + utcNow.toString());
		game.setBgglink("http://cnn.com");
		gameRepository.save(game);

		Game gameFound = gameRepository.findByName("GAME:" + utcNow.toString());

		System.out.println("UUID:" + gameFound.getUUID());
		assertEquals("PUBLISH:" + utcNow.toString(), gameFound.getPublished());
		assertEquals("GAME:" + utcNow.toString(), gameFound.getName());
		assertEquals("http://cnn.com", gameFound.getBgglink());

		gameRepository.delete(gameFound);

		gameFound = gameRepository.findByName("GAME:" + utcNow.toString());
		assertEquals(null, gameFound);

		/**** EVENT ****/
		Event event = new Event();
		event.setStart(Date.from(utcNow.toInstant()));
		event.setStart(Date.from(utcTwoHoursFromNow.toInstant()));

		String eventUUID = event.getUUID();
		String eventName = event.getEventname();
		eventRepository.save(event);

		// FIND BY UUID and EVENTNAME
		Event eventFound = eventRepository.findByUUID(eventUUID);
		Event eventFoundByEventName = eventRepository.findByEventname(eventName);
		System.out.println("UUID:" + eventFound.getUUID());
		System.out.println("EVTNAME:" + eventFound.getEventname());

		assertEquals(eventFoundByEventName, eventFound);
		assertEquals(utcNow.toString(), eventFound.getStart());
		assertEquals(utcTwoHoursFromNow.toString(), eventFoundByEventName.getStop());

		eventRepository.delete(eventFound);

		// FIND BY EVENTNAME
		eventFound = eventRepository.findByEventname(eventName);
		assertEquals(null, eventFound);

	}

	/**
	 * Test of findByTitleContaining method, of class MovieRepository.
	 * 
	 * @throws ParseException
	 */
	@Test
	public void eventShouldBeAbleToHaveMultiplePlayers() throws ParseException {

		/*
		 * Player player2 = new Player("player2", "John", "fishhead",
		 * Player.SecurityRole.ROLE_USER); player2.setFirstname("John");
		 * player2.setNickname("turd"); player2.setAlignment("Lawful Evil");
		 * player2.playedIn(event, 2, "LOST");
		 * 
		 * playerRepository.save(player2);
		 */

		/*
		 * // @formatter:off String gamename = "Kingdom Builder"; Game game =
		 * new Game(); game.setName(gamename);
		 * 
		 * String gamename2 = "Isle of Skye"; Game game2 = new Game();
		 * game2.setName(gamename2);
		 * 
		 * Set<Game> games = new HashSet<Game>(); Set<Game> onegame = new
		 * HashSet<Game>(); games.add(game); onegame.add(game);
		 * games.add(game2);
		 * 
		 * Event event = new Event(); event.setEventname(); event.setStart(new
		 * java.util.Date()); event.setGames(games);
		 * 
		 * String neweventname = event.getEventname();
		 * 
		 * Player mitch = new Player("thebogie", "Mitch", "fishhead",
		 * Player.SecurityRole.ROLE_ADMIN); mitch.setNickname("toad");
		 * mitch.setAlignment("Lawful Neturl"); playerRepository.save(mitch);
		 * 
		 * Player player = new Player("player1", "Tom", "fishhead",
		 * Player.SecurityRole.ROLE_USER); player.setNickname("catfish");
		 * player.setAlignment("Lawful Neturl"); player.playedIn(event, 1,
		 * "WON"); playerRepository.save(player);
		 * 
		 * Player player2 = new Player("player2", "John", "fishhead",
		 * Player.SecurityRole.ROLE_USER); player2.setFirstname("John");
		 * player2.setNickname("turd"); player2.setAlignment("Lawful Evil");
		 * player2.playedIn(event, 2, "LOST"); playerRepository.save(player2);
		 * 
		 * Player player3 = new Player("player3", "John", "fishhead",
		 * Player.SecurityRole.ROLE_USER); player3.setFirstname("Fred");
		 * player3.setNickname("frog"); player3.setAlignment("Lawful Good");
		 * player3.playedIn(event, 3, "LOST"); playerRepository.save(player3);
		 * 
		 * Event event2 = new Event(); event2.setEventname();
		 * event2.setStart(new java.util.Date()); event2.setGames(onegame);
		 * 
		 * mitch.playedIn(event2, 1, "WON"); playerRepository.save(mitch);
		 * 
		 * player2.playedIn(event2, 2, "TIE"); playerRepository.save(player2);
		 * 
		 * player3.playedIn(event2, 3, "TIE"); playerRepository.save(player3);
		 * 
		 * 
		 * // eventService.eventRepository.save(player);
		 * 
		 * 
		 * 
		 * savedevent = eventRepository.findByEventname(neweventname);
		 * assertEquals(savedevent.getEventname(), neweventname);
		 * 
		 * int numplayers = eventRepository.findNumberOfPlayers(neweventname);
		 * assertEquals(numplayers, 3);
		 * 
		 * // @formatter:on
		 * 
		 */

		/*
		 * Record playedin = new Record(); playedin.setPlayer(player);
		 * playedin.setPlace(3); playedin.setResult("LOST");
		 * playedin.setPlayer(player); playedin.setEvent(event);
		 * 
		 * Record playedin2 = new Record(); playedin2.setPlayer(player2);
		 * playedin2.setPlace(2); playedin2.setResult("LOST");
		 * playedin2.setPlayer(player2); playedin2.setEvent(event);
		 * 
		 * Record playedin3 = new Record(); playedin3.setPlayer(player3);
		 * playedin3.setPlace(1); playedin3.setResult("WON");
		 * playedin3.setPlayer(player3); playedin3.setEvent(event);
		 * 
		 * playerRepository.save(player); playerRepository.save(player2);
		 * playerRepository.save(player3); eventRepository.save(event);
		 * recordRepository.save(playedin); recordRepository.save(playedin2);
		 * recordRepository.save(playedin3);
		 * 
		 * 
		 * 
		 * 
		 */

		// eventRepository.save(event);

		// Record playedin = new Record();
		// playedin.setResult("WIN");
		// playedin.setPlace(3);
		// playedin.setEvent(event);
		// playedin.setPlayer(player);

		// player.setRecord(playedin);
		// playerRepository.save(player);

		/*
		 * Director foundRobert = findDirectorByProperty("name",
		 * robert.getName()).iterator().next(); assertEquals(robert.getId(),
		 * foundRobert.getId()); assertEquals(robert.getName(),
		 * foundRobert.getName()); assertEquals(forrest,
		 * robert.getDirectedMovies().iterator().next());
		 * 
		 * Movie foundForrest = findMovieByProperty("title",
		 * forrest.getTitle()).iterator().next(); assertEquals(1,
		 * foundForrest.getDirectors().size()); assertEquals(foundRobert,
		 * foundForrest.getDirectors().iterator().next());
		 */
	}

	@Test
	public void shouldBeAbleToAddOldPlayerToEvent() throws ParseException {

		// Player oldplayer = playerRepository.findByFirstname("Mitch");
		// oldplayer.playedIn(savedevent, 1, "WON");
		// playerRepository.save(oldplayer);

	}

}
