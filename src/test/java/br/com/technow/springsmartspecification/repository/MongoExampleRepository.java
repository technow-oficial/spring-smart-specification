package br.com.technow.springsmartspecification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.technow.springsmartspecification.domain.MongoExample;

@Repository
public interface MongoExampleRepository
        extends MongoRepository<MongoExample, String>, MongoSmartSpecificationRepository<MongoExample> {

}
