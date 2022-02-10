package io.github.technowoficial.springsmartspecification.jpa.predicatebuilder;

import java.util.Set;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import io.github.technowoficial.springsmartspecification.jpa.query.JpaSpecificationArgs;
import io.github.technowoficial.springsmartspecification.jpa.query.JpaSpecificationFieldPredicateBuilder;

public class IsNullJpaSpecificationFieldPredicateBuilder implements JpaSpecificationFieldPredicateBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("IsNull", "Null");
    }

    @Override
    public Predicate build(JpaSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        Expression<?> path = (Expression<?>) args.getExpressions().get(0);
        Boolean value = (Boolean) args.getExpressions().get(1);
        if (value) {
            return args.getCriteriaBuilder().isNull(path);
        } else {
            return args.getCriteriaBuilder().isNotNull(path);
        }
    }

}
