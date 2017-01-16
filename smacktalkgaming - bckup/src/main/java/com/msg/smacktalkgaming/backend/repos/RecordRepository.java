package com.msg.smacktalkgaming.backend.repos;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.msg.smacktalkgaming.backend.domain.Event;
import com.msg.smacktalkgaming.backend.domain.Record;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author mh
 * @since 24.07.12
 */
// tag::repository[]
@Repository
// @RepositoryRestResource(collectionResourceRel = "records", path = "records")
public interface RecordRepository extends GraphRepository<Record> {
	@Query("MATCH (e)-[r]->(m) WHERE r.uuid = {uuid} RETURN r")
	Record findByUUID(@Param("uuid") String uuid);
}

// end::repository[]
