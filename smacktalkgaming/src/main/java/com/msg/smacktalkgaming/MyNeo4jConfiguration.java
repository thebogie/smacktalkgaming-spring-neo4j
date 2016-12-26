package com.msg.smacktalkgaming;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// tag::config[]
//@EnableTransactionManagement
// @Import(RepositoryRestMvcConfiguration.class)
//@EnableScheduling

//@Configuration
// @EnableNeo4jRepositories(basePackages = "com.stg.repositories")

@Configuration
@EnableTransactionManagement
// @EnableAutoConfiguration

@ComponentScan("com.msg.smacktalkgaming")
@EnableNeo4jRepositories("com.msg.smacktalkgaming.backend.repos")

public class MyNeo4jConfiguration extends Neo4jConfiguration {

	public static final String URL = System.getenv("NEO4J_URL") != null ? System.getenv("NEO4J_URL")
			// : "http://neo4j:password@mymachine:7474";
			: "http://neo4j:password@localhost:7474";

	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
		config.driverConfiguration().setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver").setURI(URL);
		return config;
	}

	@Override
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(), "com.msg.smacktalkgaming.backend.domain");
	}

}
// end::config[]
