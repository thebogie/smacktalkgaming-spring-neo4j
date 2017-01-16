package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

//@JsonIdentityInfo(generator = JSOGGenerator.class)
@RelationshipEntity(type = "WAS_RATED")
public class WasRated {

	@GraphId
	Long id;

	@Property(name = "uuid")
	private String uuid;

	@StartNode
	private Player player;
	@EndNode
	private Glicko2 glicko2;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Glicko2 getGlicko2() {
		return this.glicko2;
	}

	public void setGlicko2(Glicko2 glicko2) {
		this.glicko2 = glicko2;
	}

	public WasRated() {

		this.uuid = DomainTools.getTimeBasedUUID();
	}

	public String getUUID() {
		return uuid;
	}

	@Override
	public String toString() {
		return String.format("Player %s WAS RATED %d", glicko2.getRating());
	}
}
