
package com.msg.smacktalkgaming.backend.domain;

import org.neo4j.ogm.typeconversion.AttributeConverter;

public class PlayerRolesConverter implements AttributeConverter<Player.SecurityRole[], String[]> {

	@Override
	public String[] toGraphProperty(Player.SecurityRole[] value) {
		if (value == null) {
			return null;
		}
		String[] values = new String[(value.length)];
		int i = 0;
		for (Player.SecurityRole role : value) {
			values[i++] = role.name();
		}
		return values;
	}

	@Override
	public Player.SecurityRole[] toEntityAttribute(String[] value) {
		Player.SecurityRole[] roles = new Player.SecurityRole[value.length];
		int i = 0;
		for (String role : value) {
			roles[i++] = Player.SecurityRole.valueOf(role);
		}
		return roles;
	}
}
