package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

//@JsonIdentityInfo(generator = JSOGGenerator.class)
@RelationshipEntity(type = "PLAYED_IN")
public class Record {

	@GraphId
	Long id;

	// @Convert(RecordConverter.class)
	@Property(name = "result")
	private enumContestResults result;

	@Property(name = "place")
	private int place;

	@Property(name = "uuid")
	private String uuid;

	@StartNode
	private Player player;
	@EndNode
	private Event event;

	public enum enumContestResults {
		DEMOLISH, WON, TIE, LOST, SKUNK, DROP, QUIT;
	}

	public Record() {

		this.uuid = DomainTools.getTimeBasedUUID();
	}

	public String getUUID() {
		return uuid;
	}

	public Record(Player player, Event event, short place, enumContestResults result) {
		this.player = player;
		this.event = event;
		this.place = place;
		this.result = result;
	}

	public int getPlace() {
		return (place);
	}

	public enumContestResults getResult() {
		return (result);
	}

	public Player getPlayer() {
		return player;
	}

	public Event getEvent() {
		return event;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setResult(enumContestResults result) {
		this.result = result;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setPlace(int i) {
		this.place = i;
	}

	@Override
	public String toString() {
		return String.format("%s played in %s and got %d place and %s", player.getLogin(), event.getEventname(), place,
				result);
	}
}
