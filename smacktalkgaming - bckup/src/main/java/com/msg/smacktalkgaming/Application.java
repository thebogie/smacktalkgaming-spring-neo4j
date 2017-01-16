package com.msg.smacktalkgaming;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.msg.smacktalkgaming.backend.services.PlayerService;
import com.msg.smacktalkgaming.security.VaadinSessionSecurityContextHolderStrategy;
import com.vaadin.server.VaadinServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Application {

	@Configuration
	@Import(MyNeo4jConfiguration.class)
	@EnableGlobalMethodSecurity(securedEnabled = true)

	public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			// @formatter:off
			/*
			 * try { auth.userDetailsService(pService.userDetails());
			 * 
			 * DaoAuthenticationProvider daoAuthenticationProvider = new
			 * DaoAuthenticationProvider(); //
			 * daoAuthenticationProvider.setPasswordEncoder(pService.
			 * passwordEncoder());
			 * daoAuthenticationProvider.setUserDetailsService(pService.
			 * userDetails()); //
			 * daoAuthenticationProvider.setPreAuthenticationChecks(
			 * userDetailsChecker());
			 * auth.authenticationProvider(daoAuthenticationProvider); } catch
			 * (Exception e) { throw new RuntimeException(e); }
			 * 
			 * auth.inMemoryAuthentication().withUser("admin").password("p").
			 * roles("ADMIN", "USER").and().withUser("user")
			 * .password("p").roles("USER");
			 */
			// @formatter:on
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return authenticationManager();
		}

		static {
			// Use a custom SecurityContextHolderStrategy
			SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
		}

	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
