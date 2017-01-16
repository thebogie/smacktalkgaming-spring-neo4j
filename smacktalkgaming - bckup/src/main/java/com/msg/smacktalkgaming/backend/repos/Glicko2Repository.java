
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

//tag::repository[]
@Repository
public interface Glicko2Repository extends GraphRepository<Glicko2> {

	@Query("MATCH (g:Glicko2) WHERE g.uuid = {uuid} RETURN g")
	Player findByUUID(@Param("uuid") String uuid);

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
