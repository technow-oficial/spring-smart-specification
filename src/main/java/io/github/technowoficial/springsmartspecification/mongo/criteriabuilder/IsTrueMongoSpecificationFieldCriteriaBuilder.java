package io.github.technowoficial.springsmartspecification.mongo.criteriabuilder;

import java.util.Set;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationArgs;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationFieldCriteriaBuilder;

public class IsTrueMongoSpecificationFieldCriteriaBuilder implements MongoSpecificationFieldCriteriaBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("True", "IsTrue");
    }

    @Override
    public CriteriaDefinition build(MongoSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        String path = (String) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        if (value instanceof Boolean) {
            if ((Boolean) value) {
                return Criteria.where(path).is(true);
            } else {
                return Criteria.where(path).is(false);
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

}
