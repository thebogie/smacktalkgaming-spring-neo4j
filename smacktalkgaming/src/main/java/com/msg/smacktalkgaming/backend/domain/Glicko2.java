package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.msg.smacktalkgaming.backend.domain.rating.Rating;
import com.msg.smacktalkgaming.backend.domain.rating.RatingCalculator;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@NodeEntity(label = "glicko2")
public class Glicko2 {

	private final static double DEFAULT_RATING = 1500.0;
	private final static double DEFAULT_DEVIATION = 350;
	private final static double DEFAULT_VOLATILITY = 0.06;

	@Relationship(type = "RATING_FROM")
	private Event rating_from;

	public Event getEventRatingFrom() {
		return (this.rating_from);
	}

	public void setEventRatingFrom(Event evt) {
		this.rating_from = evt;
	}

	@GraphId
	Long id;

	@Property(name = "uuid")
	private String uuid;

	@Property(name = "rating")
	private double rating;

	@Property(name = "ratingdeviation")
	private double ratingdeviation;

	@Property(name = "volatility")
	private double volatility;

	@Property(name = "created")
	private Date created;

	public Glicko2() {

		this.uuid = DomainTools.getTimeBasedUUID();
		this.rating = DEFAULT_RATING;
		this.ratingdeviation = DEFAULT_DEVIATION;
		this.volatility = DEFAULT_VOLATILITY;
	}

	public String getUUID() {
		return uuid;
	}

	public void setRatingfromEvent(Event evt) {
		this.rating_from = evt;
	}

	public double getRating() {
		return (rating);
	}

	public double getRatingdeviation() {
		return (ratingdeviation);
	}

	public double getVolatility() {
		return (volatility);
	}

	public void setVolatility(double volatility) {
		this.volatility = volatility;
	}

	public void setRatingdeviation(double ratingdeviation) {
		this.ratingdeviation = ratingdeviation;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return (created);
	}

	// @Override
	// public String toString() {
	// return String.format("%s played in %s and got %d place and %s", player,
	// event, place, result);
	// }
}
