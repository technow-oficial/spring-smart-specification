package br.com.technow.springsmartspecification.mongo.criteriabuilder;

import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import br.com.technow.springsmartspecification.mongo.query.MongoSpecificationArgs;
import br.com.technow.springsmartspecification.mongo.query.MongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.query.MongoValue;

public class StartsWithMongoSpecificationFieldCriteriaBuilder implements MongoSpecificationFieldCriteriaBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("StartingWith", "StartsWith", "IsStartingWith");
    }

    @Override
    public CriteriaDefinition build(MongoSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        String path = (String) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        boolean ignoreCase = false;
        if (value instanceof MongoValue) {
            MongoValue mv = (MongoValue) value;
            if (mv.isIgnoreCase()) {
                ignoreCase = true;
                value = mv.quote();
            } else {
                throw new IllegalArgumentException("Invalid value: " + value);
            }
        } else if (value instanceof String) {
            value = Pattern.quote((String) value);
        } else {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        String regex = "^" + value;
        Pattern pattern;
        if (ignoreCase) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regex);
        }
        return Criteria.where(path).regex(pattern);
    }

}
