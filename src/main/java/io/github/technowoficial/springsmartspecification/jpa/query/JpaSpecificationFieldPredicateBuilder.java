package io.github.technowoficial.springsmartspecification.jpa.query;

import java.util.Set;

import javax.persistence.criteria.Predicate;

public interface JpaSpecificationFieldPredicateBuilder {

    Set<String> getPatterns();

    Predicate build(JpaSpecificationArgs args);

}
