package com.msg.smacktalkgaming.backend.domain;

import java.util.UUID;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class DomainTools {

	private static TimeBasedGenerator TB_GENERATOR = Generators.timeBasedGenerator(EthernetAddress.fromInterface());
	private static RandomBasedGenerator RD_GENERATOR = Generators.randomBasedGenerator();

	public static String getRandomBasedUUID() {
		return RD_GENERATOR.generate().toString();
	}

	public static String getTimeBasedUUID() {
		return TB_GENERATOR.generate().toString();
	}
}
