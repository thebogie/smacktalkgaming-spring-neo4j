package com.msg.smacktalkgaming.backend.entities;

import org.neo4j.ogm.annotation.*;
import org.springframework.data.neo4j.annotation.QueryResult;

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

@QueryResult
public class RecordResults {

	private int place;
	private String playeruuid;

	public void setPlace(int place) {
		this.place = place;
	}

	public int getPlace() {
		return this.place;
	}

}
