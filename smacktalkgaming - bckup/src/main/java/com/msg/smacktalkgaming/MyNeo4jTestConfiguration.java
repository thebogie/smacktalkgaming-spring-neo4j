package com.msg.smacktalkgaming;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
// @EnableAutoConfiguration
// @ComponentScan({ "com.msg.smacktalkgaming.backend" })

@ComponentScan(basePackages = { "com.msg.smacktalkgaming.backend.repos", //
		"com.msg.smacktalkgaming.backend.domain", //
		"com.msg.smacktalkgaming.backend.domain.rating", //
		"com.msg.smacktalkgaming.backend.services",//
})

// @Configuration
@EnableNeo4jRepositories({ "com.msg.smacktalkgaming.backend.repos" })
@Profile({ "embedded", "test" })
@EnableGlobalMethodSecurity(securedEnabled = false)
public class MyNeo4jTestConfiguration extends Neo4jConfiguration {

	// to see embedded: bin\neo4j-shell.bat -path
	// "C:\_smacktalk\embedded\graph.db\"
	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
		config.driverConfiguration().setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver")
				.setURI("file:/home/osboxes/workspace/embedded/graph.db");
		// .setURI("file:/C:/tools/neo4j.304/data/databases/graph.db/");

		return config;
	}

	@Override
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(), "com.msg.smacktalkgaming.backend");
	}
}
