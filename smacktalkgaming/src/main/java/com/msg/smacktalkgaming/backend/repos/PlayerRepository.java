
package com.msg.smacktalkgaming.backend.repos;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.msg.smacktalkgaming.backend.domain.Player;

//tag::repository[]

public interface PlayerRepository extends GraphRepository<Player> {

	@Query("MATCH (p:Player) RETURN p")
	Collection<Player> getAllPlayers();

	@Query("MATCH (p:Player) RETURN p.Firstname")
	List<String> getAllPlayersNames();

	@Query("MATCH (p:Player {Firstname:{0}}) RETURN p")
	Player findByLoginLikeIgnoreCase(String login);

	/* A version to fetch List instead of Page to avoid extra count query. */
	// @Query("MATCH (n) WHERE id(n)={0} RETURN n")
	// List<Player> findAllBy(Pageable pageable);

	// List<Person> findAllBy(Pageable pageable);

	// List<Player> findByNameLikeIgnoreCase(String nameFilter);

	// For lazy loading and filtering
	// List<Player> findByNameLikeIgnoreCase(String nameFilter, Pageable
	// pageable);

	// long countByNameLike(String nameFilter);

}
