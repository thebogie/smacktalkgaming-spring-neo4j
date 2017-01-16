package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@NodeEntity(label = "game")
public class Game {

	@GraphId
	Long graphId;

	@Property(name = "uuid")
	private String uuid;

	@Property(name = "name")
	private String name;

	@Property(name = "published")
	private String published;

	@Property(name = "bgglink")
	private String bgglink;

	public Game() {

		this.uuid = DomainTools.getTimeBasedUUID();

	}

	public String getUUID() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getPublished() {
		return published;
	}

	public String getBgglink() {
		return bgglink;
	}

	// public Collection<Role> getRoles() {
	// return roles;
	// }

	public void setUUID(String uuid) {
		// TODO: set UUID here
		this.uuid = uuid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public void setBgglink(String bgglink) {
		this.bgglink = bgglink;
	}

}
