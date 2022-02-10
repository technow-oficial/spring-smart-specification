package br.com.technow.springsmartspecification.mongo.criteriabuilder;

import java.util.Set;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import br.com.technow.springsmartspecification.mongo.query.MongoSpecificationArgs;
import br.com.technow.springsmartspecification.mongo.query.MongoSpecificationFieldCriteriaBuilder;

public class GreaterThanEqualMongoSpecificationFieldCriteriaBuilder implements MongoSpecificationFieldCriteriaBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("GreaterThanEqual", "Ge", "GreaterThanOrEqualTo");
    }

    @Override
    public CriteriaDefinition build(MongoSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        String path = (String) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        return Criteria.where(path).gte(value);
    }

}
