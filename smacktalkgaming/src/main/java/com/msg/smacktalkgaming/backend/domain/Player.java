package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import com.voodoodyne.jackson.jsog.JSOGGenerator;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//@JsonIdentityInfo(generator = JSOGGenerator.class)
// tag::player[]
@NodeEntity(label = "Player")
public class Player {
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@GraphId
	Long id;

	// @Property(name = "UUID")
	private String uuid;

	@Property(name = "Firstname")
	private String firstname;

	// @Property(name = "Surname")
	private String surname;

	// @Property(name = "Nickname")
	private String nickname;

	// @Property(name = "Birthdate")
	private Date birthdate;

	// @Property(name = "Currentevent")
	private String currentevent;

	// @Property(name = "Alignment")
	private String alignment;

	// @Property(name = "Admin")
	private String admin;

	// @Property(name = "Login")
	private String login;

	// @Property(name = "Password")
	private String password;

	// @Property(name = "Info")
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

	public Player(String login, String firstname, String password) {
		this.login = login;
		this.firstname = firstname;
		this.password = password;
	}

	public Player(String login, String firstname, String password, SecurityRole... roles) {
		this.login = login;
		this.firstname = firstname;
		this.password = encode(password);
		this.roles = roles;
	}

	private String encode(String password) {

		return passwordEncoder.encode(password);
	}

	/* SECURITY */

	public SecurityRole[] getRole() {
		return roles;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public boolean comparePassword(String password) {

		return !passwordEncoder.matches(password, this.password);
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
	public Player() {
	}

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

	public String getAdmin() {
		return admin;
	}

	// public Collection<Role> getRoles() {
	// return roles;
	// }

	public void setUUID(String uuid) {
		// TODO: set UUID here
		this.uuid = uuid;
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

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = encode(password);
	}

}
