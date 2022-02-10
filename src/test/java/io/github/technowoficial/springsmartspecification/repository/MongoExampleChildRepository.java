package io.github.technowoficial.springsmartspecification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.technowoficial.springsmartspecification.domain.MongoExampleChild;

public interface MongoExampleChildRepository extends MongoRepository<MongoExampleChild, String> {

}
