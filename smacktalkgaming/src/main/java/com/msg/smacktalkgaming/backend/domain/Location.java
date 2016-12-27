package com.msg.smacktalkgaming.backend.domain;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.neo4j.ogm.annotation.*;

import java.io.FileReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//@JsonIdentityInfo(generator = JSOGGenerator.class)
// tag::player[]

@NodeEntity(label = "location")
public class Location {

	String googleGeocode = "AIzaSyCXSL3n9tI-VTgRJOhXqJJJ42D1FO1EGBE";

	@GraphId
	private Long id;

	@Property(name = "uuid")
	private String uuid;

	@Property(name = "location")
	private String location;

	@Property(name = "longitude")
	private double longitude;

	@Property(name = "latitude")
	private double latitude;

	public Location() {

		this.uuid = DomainTools.getTimeBasedUUID();
	}

	public String getUUID() {
		return uuid;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {

		// find the log/lat using google
		GeoApiContext context = new GeoApiContext().setApiKey(googleGeocode);
		GeocodingResult[] results;
		try {
			results = GeocodingApi.geocode(context, location).await();
			this.location = results[0].formattedAddress;
			this.latitude = results[0].geometry.location.lat;
			this.longitude = results[0].geometry.location.lng;

			System.out.println(results[0].formattedAddress);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// something happened... set the location to the string sent in. fix
			// later
			this.location = location;
		}

	}

}
