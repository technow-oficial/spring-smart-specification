package br.com.technow.springsmartspecification.mongo.query;

import java.util.Set;

import org.springframework.data.mongodb.core.query.CriteriaDefinition;

public interface MongoSpecificationFieldCriteriaBuilder {

    Set<String> getPatterns();

    CriteriaDefinition build(MongoSpecificationArgs args);

}
