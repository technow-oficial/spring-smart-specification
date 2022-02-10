package io.github.technowoficial.springsmartspecification.jpa.predicatebuilder;

import java.util.Set;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import io.github.technowoficial.springsmartspecification.jpa.query.JpaSpecificationArgs;
import io.github.technowoficial.springsmartspecification.jpa.query.JpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.reflection.ReflectionUtils;

public class LessThanJpaSpecificationFieldPredicateBuilder implements JpaSpecificationFieldPredicateBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("LessThan", "Lt", "Before");
    }

    @Override
    public Predicate build(JpaSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        Expression<?> path = (Expression<?>) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        return ReflectionUtils.invokeFirstMethod(this, "buildMagic", args, path, value);
    }

    public <T extends Comparable<T>> Predicate buildMagic(JpaSpecificationArgs args, Expression<T> path, T value) {
        return args.getCriteriaBuilder().lessThan(path, value);
    }

}
