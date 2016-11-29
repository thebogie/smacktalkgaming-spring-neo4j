package com.msg.smacktalkgaming.backend.repos;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author mh
 * @since 24.07.12
 */
// tag::repository[]
// @RepositoryRestResource(collectionResourceRel = "events", path = "events")
@Repository
public interface EventRepository extends GraphRepository<Event> {
	Event findOne(Long id);

	@Query("MATCH (e:event) WHERE e.eventname = {eventname} RETURN e")
	Event findByEventname(@Param("eventname") String eventname);

	@Query("MATCH (e:event) WHERE e.uuid = {uuid} RETURN e")
	Event findByUUID(@Param("uuid") String uuid);

	@Query("MATCH (e:event)<-[r:PLAYED_IN]-(p:player) WHERE e.eventname = {eventname} return  count(r)")
	int findNumberOfPlayers(@Param("eventname") String eventname);

	// Player findByNickname(@Param("nickname") String nickname);

	// @Query("MATCH (p:Player) WHERE p.Nickname =~ ('(?i).*'+{nickname}+'.*')
	// RETURN p")
	// Collection<Player> findByNicknameContaining(@Param("nickname") String
	// nickname);

	// @Query("MATCH (m:Movie)<-[:ACTED_IN]-(a:Person) RETURN m.title as movie,
	// collect(a.name) as cast LIMIT {limit}")
	// List<Map<String,Object>> graph(@Param("limit") int limit);
}
// end::repository[]