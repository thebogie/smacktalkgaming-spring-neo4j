package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

//@JsonIdentityInfo(generator = JSOGGenerator.class)
@RelationshipEntity(type = "PLAYED_IN")
public class Record {

	@GraphId
	Long id;
	@Property(name = "Result")
	private String result;

	@Property(name = "Place")
	private int place;

	@Property(name = "UUID")
	private String uuid;

	@StartNode
	private Player player;
	@EndNode
	private Event event;

	public Record() {
	}

	public Record(Player player, Event event, int place, String result) {
		this.player = player;
		this.event = event;
		this.place = place;
		this.result = result;
	}

	public int getPlace() {
		return (place);
	}

	public String getResult() {
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

	public void setResult(String result) {
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
		return String.format("%s played in %s and got %d place and %s", player, event, place, result);
	}
}
