package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NodeEntity(label = "player")
public class Player {

	@Relationship(type = "PLAYED_IN", direction = Relationship.OUTGOING)
	private Collection<Record> records;

	public Collection<Record> getRecords() {
		return this.records;
	}

	public void setRecords(Collection<Record> records) {
		this.records = records;
	}

	public void addNewPlayedIn(Record record) {
		Collection<Record> newlist = new ArrayList<Record>();
		newlist.add(record);

		if (this.records == null) {

			this.records = newlist;

		} else {
			this.records.add(record);

		}
	}

	// @Relationship(type = "CURRENT_RATING", direction = Relationship.OUTGOING)
	private Glicko2 currentrating;

	public Glicko2 getCurrentrating() {
		return currentrating;
	}

	public void setCurrentrating(Glicko2 rating) {
		this.currentrating = rating;
	}

	@Relationship(type = "WAS_RATED", direction = Relationship.OUTGOING)
	private Collection<Glicko2> wasrated;

	public Collection<Glicko2> getWasRated() {
		return this.wasrated;
	}

	public void setWasRated(Collection<Glicko2> listwasrated) {
		this.wasrated = listwasrated;
	}

	public void addWasRated(Glicko2 oldrating) {
		Collection<Glicko2> newlist = new ArrayList<Glicko2>();
		newlist.add(oldrating);
		if (this.wasrated == null) {

			this.wasrated = newlist;

		} else {
			this.wasrated.add(oldrating);

		}
	}

	@GraphId
	private Long id;

	public Long getID() {
		return this.id;
	}

	@Property(name = "uuid")
	private String uuid;

	@Property(name = "firstname")
	private String firstname;

	@Property(name = "surname")
	private String surname;

	@Property(name = "nickname")
	private String nickname;

	@Property(name = "birthdate")
	private Date birthdate;

	@Property(name = "currentevent")
	private String currentevent;

	@Property(name = "alignment")
	private String alignment;

	@Property(name = "login")
	private String login;

	@Property(name = "password")
	private String password;

	@Property(name = "info")
	private String info;

	@Convert(PlayerRolesConverter.class)
	private SecurityRole[] roles;

	public enum SecurityRole implements GrantedAuthority {
		ROLE_USER, ROLE_ADMIN;

		@Override
		public String getAuthority() {
			return name();
		}
	}

	// TODO: set UUID for all methods
	public Player() {
		this.uuid = DomainTools.getTimeBasedUUID();
	}

	public Player(String login, String firstname, String password) {
		this.login = login;
		this.firstname = firstname;
		this.password = password;
	}

	public Player(String login, String firstname, String password, SecurityRole... roles) {
		this.login = login;
		this.firstname = firstname;
		this.password = encode(password);
		// this.roles = roles;
	}

	private String encode(String password) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	/* SECURITY */

	public SecurityRole[] getRole() {
		return null;
	}

	public void setRole(SecurityRole... roles) {
		this.roles = roles;

	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = encode(password);
	}

	public boolean comparePassword(String password) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(password, this.password);
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void updatePassword(String old, String newPass1, String newPass2) {
		if (!password.equals(encode(old))) {
			throw new IllegalArgumentException("Existing Password invalid");
		}
		if (!newPass1.equals(newPass2)) {
			throw new IllegalArgumentException("New Passwords don't match");
		}
		this.password = encode(newPass1);
	}

	/*
	 * @Relationship(type = "PLAYED_IN", direction = Relationship.OUTGOING)
	 * private Set<Record> records = new HashSet<Record>();
	 * 
	 * public Set<Record> getRecords() { return records; }
	 * 
	 * public Record playedIn(Event event, int place, String result) { final
	 * Record playedin = new Record(this, event, place, result);
	 * records.add(playedin); event.addResult(playedin); return playedin; }
	 */

	/*
	 * @Relationship(type = "PLAYED_IN", direction = Relationship.OUTGOING)
	 * private List<Game> games;
	 * 
	 * public List<Game> getGames() { return games; }
	 * 
	 * public void setGames(List<Game> games) { this.games = games; }
	 * 
	 * // @Relationship(type = "RATING", direction = Relationship.OUTGOING) //
	 * private Rating rating = new Rating();
	 * 
	 * // end::movie[]
	 */

	public String getUUID() {
		return uuid;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getSurname() {
		return surname;
	}

	public String getNickname() {
		return nickname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public String getCurrentevent() {
		return currentevent;
	}

	public String getAlignment() {
		return alignment;
	}

	public SecurityRole[] getRoles() {
		return roles;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public void setCurrentevent(String currentevent) {
		this.currentevent = currentevent;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
