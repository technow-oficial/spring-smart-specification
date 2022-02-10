package io.github.technowoficial.springsmartspecification.mongo.criteriabuilder;

import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationArgs;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoValue;

public class RegexMongoSpecificationFieldCriteriaBuilder implements MongoSpecificationFieldCriteriaBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("Regex");
    }

    @Override
    public CriteriaDefinition build(MongoSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        String path = (String) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        if (value instanceof String) {
            String regex = (String) value;
            Pattern pattern = Pattern.compile(regex);
            return Criteria.where(path).regex(pattern);
        } else if (value instanceof MongoValue) {
            MongoValue mv = (MongoValue) value;
            if (mv.isIgnoreCase()) {
                String regex = (String) mv.getValue();
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                return Criteria.where(path).regex(pattern);
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

}
