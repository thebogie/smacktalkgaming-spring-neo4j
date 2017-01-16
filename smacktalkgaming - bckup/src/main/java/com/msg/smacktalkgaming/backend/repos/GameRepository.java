package com.msg.smacktalkgaming.backend.repos;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.msg.smacktalkgaming.backend.domain.Game;

@Repository
// @RepositoryRestResource(collectionResourceRel = "games", path = "games")
public interface GameRepository extends GraphRepository<Game> {

	// Game findOne(Long id);

	@Query("MATCH (g:game) WHERE g.name = {name} RETURN g")
	Game findByName(@Param("name") String name);
}
