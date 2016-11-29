package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//@JsonIdentityInfo(generator = JSOGGenerator.class)
// tag::player[]

@NodeEntity(label = "location")
public class Location {

	@GraphId
	Long id;

	@Property(name = "uuid")
	private String uuid;

	@Property(name = "location")
	private String location;

	@Property(name = "longitude")
	private String longitude;

	@Property(name = "latitude")
	private String latitude;

	public Location() {

		this.uuid = DomainTools.getTimeBasedUUID();
	}

	public String getUUID() {
		return uuid;
	}

	public String getLocation() {
		return location;
	}

	public void setUUID(String uuid) {
		// TODO: set UUID here
		this.uuid = uuid;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}