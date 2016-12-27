
package com.msg.smacktalkgaming.backend.repos;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Glicko2;
import com.msg.smacktalkgaming.backend.domain.Player;
import com.msg.smacktalkgaming.backend.domain.Record;

//tag::repository[]
@Repository
public interface PlayerRepository extends GraphRepository<Player> {

	// find the place and result for a player for an event
	@Query("match (p:player {uuid:{playeruuid}})-[r:PLAYED_IN]->(e:event {uuid:{eventuuid}}) return r")
	// @Query("match ((p:player) WHERE p.uuid =
	// {playeruuid})-[r:PLAYED_IN]->((e:event) WHERE e.eventuuid = {eventuuid})
	// return r")
	Record getRecordForPlayerFromEvent(@Param("eventuuid") String eventuuid, @Param("playeruuid") String playeruuid);

	// find all records (for a count of total plays or number of places/results
	@Query("match (p:player {uuid:{playeruuid}})-[r:PLAYED_IN]-(e) return r")
	Collection<Record> getAllRecordForPlayer(@Param("playeruuid") String playeruuid);

	// find all glicko2 records
	@Query("match (p:player {uuid:{playeruuid}})-[r:WAS_RATED]-(g) return r")
	Collection<Glicko2> getAllGlicko2ForPlayer(@Param("playeruuid") String playeruuid);

	@Query("MATCH (p:player) WHERE p.uuid = {uuid} RETURN p")
	Player findByUUID(@Param("uuid") String uuid);

	@Query("MATCH (p:player {uuid:{uuid}})-[r:CURRENT_RATING]-(m) RETURN m")
	Glicko2 findGlicko2CurrentRating(@Param("uuid") String uuid);

	// @Query("MATCH (p:player) RETURN p")
	// Collection<Player> getAllPlayers();

	// @Query("MATCH (p:player) RETURN p.firstname")
	// List<String> getAllPlayersNames();

	@Query("MATCH (p:player) WHERE p.login = {login}  RETURN p")
	Collection<Player> findByLogin(@Param("login") String login);

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
// end::repository[]
