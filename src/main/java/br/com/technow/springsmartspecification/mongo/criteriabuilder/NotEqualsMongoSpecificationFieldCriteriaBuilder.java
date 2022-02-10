package br.com.technow.springsmartspecification.mongo.criteriabuilder;

import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import br.com.technow.springsmartspecification.mongo.query.MongoSpecificationArgs;
import br.com.technow.springsmartspecification.mongo.query.MongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.query.MongoValue;

public class NotEqualsMongoSpecificationFieldCriteriaBuilder implements MongoSpecificationFieldCriteriaBuilder {

    @Override
    public Set<String> getPatterns() {
        return Set.of("NotEquals", "Ne", "Not");
    }

    @Override
    public CriteriaDefinition build(MongoSpecificationArgs args) {
        if (args.getExpressions().size() != 2) {
            throw new IllegalArgumentException("Invalid number of expressions: " + args.getExpressions().size());
        }
        String path = (String) args.getExpressions().get(0);
        Object value = args.getExpressions().get(1);
        if (value instanceof MongoValue) {
            MongoValue mv = (MongoValue) value;
            if (mv.isIgnoreCase()) {
                String regex = "^" + mv.quote() + "$";
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                return Criteria.where(path).not().regex(pattern);
            }
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        return Criteria.where(path).ne(value);
    }

}
