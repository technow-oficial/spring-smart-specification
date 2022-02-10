package br.com.technow.springsmartspecification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.technow.springsmartspecification.domain.MongoExampleChild;

public interface MongoExampleChildRepository extends MongoRepository<MongoExampleChild, String> {

}
