package br.com.technow.springsmartspecification.jpa.predicatebuilder;

import java.util.Set;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import br.com.technow.springsmartspecification.jpa.query.JpaSpecificationArgs;
import br.com.technow.springsmartspecification.jpa.query.JpaSpecificationFieldPredicateBuilder;

public class LikeJpaSpecificationFieldPredicateBuilder implements JpaSpecificationFieldPredicateBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("Like");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate build(JpaSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        Expression<String> path = (Expression<String>) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        if (value instanceof String) {
            return args.getCriteriaBuilder().like(path, (String) value);
        } else {
            Expression<String> exp = (Expression<String>) value;
            return args.getCriteriaBuilder().like(path, exp);
        }
    }

}
