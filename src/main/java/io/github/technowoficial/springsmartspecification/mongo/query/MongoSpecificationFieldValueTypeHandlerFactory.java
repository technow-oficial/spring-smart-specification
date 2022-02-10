package io.github.technowoficial.springsmartspecification.mongo.query;

import io.github.technowoficial.springsmartspecification.mongo.valuetypehandler.DefaultMongoSpecificationFieldValueTypeHandler;

public class MongoSpecificationFieldValueTypeHandlerFactory {

    private final static MongoSpecificationFieldValueTypeHandler DEFAULT = new DefaultMongoSpecificationFieldValueTypeHandler();

    public static MongoSpecificationFieldValueTypeHandler get(Class<?> type) {
        return DEFAULT;
    }

}
