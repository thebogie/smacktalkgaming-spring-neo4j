package com.msg.smacktalkgaming.backend.repos;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.msg.smacktalkgaming.backend.domain.Game;
import com.msg.smacktalkgaming.backend.domain.Location;

@Repository
//@RepositoryRestResource(collectionResourceRel = "location", path = "locations")
public interface LocationRepository extends GraphRepository<Location> {
	Location findOne(Long id);

}
