package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//@JsonIdentityInfo(generator = JSOGGenerator.class)
// tag::player[]

@NodeEntity(label = "event")
public class Event {
	public static final String ISO_8601_24H_STG_FORMAT = "yyyy-MM-dd'T'HH:mmXXX";

	@GraphId
	Long id;

	@Property(name = "uuid")
	private String uuid;

	@Property(name = "eventname")
	private String eventname;

	@Property(name = "start")
	private String start;

	@Property(name = "stop")
	private String stop;

	@Relationship(type = "PLAYED_IN", direction = Relationship.INCOMING)
	Set<Record> results = new HashSet<>();

	@Relationship(type = "PLAYED_IN", direction = Relationship.INCOMING)
	public Collection<Record> getResults() {
		return results;
	}

	public void addResult(Record result) {
		results.add(result);
	}

	@Relationship(type = "PLAYED_WITH", direction = Relationship.OUTGOING)
	private Set<Game> games;

	public Set<Game> getGames() {
		return games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

	// @Relationship(type = "PLAYED_IN", direction = Relationship.INCOMING)
	// private Result result;

	// @Relationship(type = "PLAYED_WITH", direction = Relationship.OUTGOING)
	// private List<Game> gamesPlayed;

	// @Relationship(type = "RATING", direction = Relationship.OUTGOING)
	// private Rating rating = new Rating();

	// end::movie[]

	public Event() {

		this.uuid = DomainTools.getTimeBasedUUID();
		this.eventname = createRandomEventName();
	}

	public String getUUID() {
		return uuid;
	}

	public String getEventname() {
		return eventname;
	}

	public String getStart() {
		return start;
	}

	public String getStop() {
		return stop;
	}

	public void setUUID(String uuid) {
		// TODO: set UUID here
		this.uuid = uuid;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	private String createRandomEventName() {

		String worknikKey = "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5";

		class RandomEventname {
			String start = "the";
			String adj = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX").withZone(ZoneOffset.UTC)
					.format(Instant.now());
			String noun = "placeholder";
			String end = "event";

		}

		RandomEventname ren = new RandomEventname();

		try {

			/* JSON provider */
			URL adjURL = new URL(
					"http://api.wordnik.com:80/v4/words.json/randomWord?hasDictionaryDef=true&includePartOfSpeech=adjective&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=0&maxLength=-1&api_key="
							+ worknikKey);
			URL nounURL = new URL(
					"http://api.wordnik.com:80/v4/words.json/randomWord?hasDictionaryDef=true&includePartOfSpeech=noun&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=0&maxLength=-1&api_key="
							+ worknikKey);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, String> adj = mapper.readValue(adjURL, Map.class);
			Map<String, String> noun = mapper.readValue(nounURL, Map.class);

			ren.adj = adj.get("word");
			ren.noun = noun.get("word");

		} catch (IOException ex) {
			// | JsonParseException | JsonMappingException |
			// MalformedURLException
			System.out.println("Event Name random creation failed, using default names: " + ex);

		}

		return ren.start + " " + ren.adj + " " + ren.noun + " " + ren.end;

	}

}
