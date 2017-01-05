package com.msg.smacktalkgaming;

import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Game;
import com.msg.smacktalkgaming.backend.domain.Location;
import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.domain.Record;
import com.msg.smacktalkgaming.backend.domain.Player.SecurityRole;
import com.msg.smacktalkgaming.backend.domain.Record.enumContestResults;
import com.msg.smacktalkgaming.backend.repos.EventRepository;
import com.msg.smacktalkgaming.backend.repos.GameRepository;
import com.msg.smacktalkgaming.backend.repos.LocationRepository;
import com.msg.smacktalkgaming.backend.repos.PlayerRepository;
import com.msg.smacktalkgaming.backend.repos.RecordRepository;
import com.msg.smacktalkgaming.backend.services.EventService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MyNeo4jTestConfiguration.class)
@ActiveProfiles({ "test", "embedded" })
public class ImportOldNeo4j {

	class Cargo {
		Event event;
		Game game;
		Location location;
		ArrayList<Record> records = new ArrayList<Record>();
		ArrayList<Player> players = new ArrayList<Player>();

	}

	ArrayList<String> eventstoscore = new ArrayList<String>();

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

	public ImportOldNeo4j() {

	}

	@After
	public void UpdateEvents() throws ParseException {
		for (String event : eventstoscore) {
			//// glicko2 them
			// if (event.getEventname().equals("EVENT02")) {
			// eventService.UpdateRatingsFromEvent(event);
			// }

		}

	}

	@Before
	public void initialize() throws ParseException {

	}

	@Test
	public void importTheJSON() {
		ArrayList people = new ArrayList<Player>();
		ArrayList games = new ArrayList<Game>();
		ArrayList<Event> events = new ArrayList<Event>();
		ArrayList locations = new ArrayList<Location>();

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader(System.getProperty("user.dir") + //
					"/src/main/resources/inputTEST.json"));

			// Object obj = parser
			// .parse(new FileReader(System.getProperty("user.dir") +
			// "/src/main/resources/inputTEST.json"));

			JSONObject jsonObject = (JSONObject) obj;

			Cargo c = new Cargo();
			Date start = null;
			Date stop = null;
			int place = 0;
			String eventname = "START";
			for (int i = 1; i < jsonObject.size() + 1; i++) {

				JSONArray rowList = (JSONArray) jsonObject.get("row" + i);
				// System.out.println("\nRow" + i);
				// System.out.println("\nRow" + rowList.toString());
				// Iterator<String> iterator = rowList.iterator();

				if (!eventname.equals(rowList.get(0).toString())) {

					if (!eventname.equals("START")) {

						// eventService.UpdateRatingsFromEvent(
						String eventstr = eventService.RecordEvent(c.event, c.game, c.location, c.players, c.records);
						eventstoscore.add(eventstr);
						eventService.UpdateRatingsFromEvent(eventstr);
						
					}

					// reset the cargo box
					eventname = rowList.get(0).toString();

					c = new Cargo();

				}

				if (eventname.equals("END")) {
					break;
				}

				Event event = new Event();
				event.setEventname(rowList.get(0).toString());
				c.event = event;
				// eventname = rowList.get(0).toString();

				try {
					start = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")).parse(rowList.get(1).toString());
				} catch (ParseException e) {
					start = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX")).parse(rowList.get(1).toString());
				}
				c.event.setStart(start);

				try {
					stop = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")).parse(rowList.get(2).toString());
				} catch (ParseException e) {
					stop = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX")).parse(rowList.get(2).toString());
				}
				c.event.setStop(stop);

				if (rowList.get(3) != null) {

					Player p = new Player();

					p.setFirstname(rowList.get(5).toString());
					p.setLogin(rowList.get(5).toString());
					p.setPassword("letmein");
					p.setRole(SecurityRole.ROLE_USER);
					c.players.add(p);

					Record r = new Record();
					r.setPlace(Integer.parseInt(rowList.get(3).toString()));
					r.setResult(enumContestResults.valueOf(rowList.get(4).toString()));
					r.setPlayer(p);
					r.setEvent(c.event);

					c.records.add(r);

				}

				if (rowList.get(6) != null) {

					Game g = new Game();
					g.setName(rowList.get(6).toString());
					c.game = g;
					List<Game> listofgames = new ArrayList<Game>();
					listofgames.add(g);
					event.setGames(listofgames);

				}

				if (rowList.get(7) != null) {

					Location l = new Location();
					l.setLocation(rowList.get(7).toString());
					c.location = l;
					c.event.setLocation(l);

				}

			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

}
