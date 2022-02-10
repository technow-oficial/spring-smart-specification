package io.github.technowoficial.springsmartspecification.mongo.valuetypehandler;

import java.util.List;

import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationArgs;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationFieldValueTypeHandler;

public class DefaultMongoSpecificationFieldValueTypeHandler implements MongoSpecificationFieldValueTypeHandler {

    @Override
    public boolean supportsType(Class<?> type) {
        return true;
    }

    @Override
    public boolean isValidValue(Object value) {
        return value != null;
    }

    @Override
    public List<Object> getValueExpressions(MongoSpecificationArgs args) {
        return List.of(args.getValue());
    }

}
