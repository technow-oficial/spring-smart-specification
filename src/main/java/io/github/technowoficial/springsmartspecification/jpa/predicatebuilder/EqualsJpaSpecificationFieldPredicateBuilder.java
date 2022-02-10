package io.github.technowoficial.springsmartspecification.jpa.predicatebuilder;

import java.util.Set;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import io.github.technowoficial.springsmartspecification.jpa.query.JpaSpecificationArgs;
import io.github.technowoficial.springsmartspecification.jpa.query.JpaSpecificationFieldPredicateBuilder;

public class EqualsJpaSpecificationFieldPredicateBuilder implements JpaSpecificationFieldPredicateBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("Equals", "Eq", "Is");
    }

    @Override
    public Predicate build(JpaSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        Expression<?> path = (Expression<?>) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        return args.getCriteriaBuilder().equal(path, value);
    }

}
