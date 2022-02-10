package io.github.technowoficial.springsmartspecification.mongo.expressionhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationArgs;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationFieldExpressionHandler;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoValue;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoValueFactory;

public class IgnoreCaseMongoSpecificationFieldExpressionHandler implements MongoSpecificationFieldExpressionHandler {

    @Override
    public Set<String> getPatterns() {
        return Set.of("IgnoreCase");
    }

    @Override
    public List<Object> handle(MongoSpecificationArgs args) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < args.getExpressions().size(); i++) {
            Object value = args.getExpressions().get(i);
            if (i == 0) {
                result.add(value);
            } else {
                MongoValue mv = MongoValueFactory.get(value);
                mv.setIgnoreCase(true);
                result.add(mv);
            }
        }
        return result;
    }

}
