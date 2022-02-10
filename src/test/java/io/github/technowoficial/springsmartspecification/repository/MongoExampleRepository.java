package io.github.technowoficial.springsmartspecification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.github.technowoficial.springsmartspecification.domain.MongoExample;

@Repository
public interface MongoExampleRepository
        extends MongoRepository<MongoExample, String>, MongoSmartSpecificationRepository<MongoExample> {

}
