package io.github.technowoficial.springsmartspecification.mongo.criteriabuilder;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationArgs;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoValue;

public class InMongoSpecificationFieldCriteriaBuilder implements MongoSpecificationFieldCriteriaBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("In");
    }

    @Override
    public CriteriaDefinition build(MongoSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        String path = (String) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        if (args.getValue() instanceof Collection) {
            return Criteria.where(path).in((Collection<?>) args.getValue());
        } else if (value instanceof MongoValue) {
            throw new IllegalArgumentException("Invalid value: " + value);
        } else {
            return Criteria.where(path).in((Object[]) args.getValue());
        }
    }

}
