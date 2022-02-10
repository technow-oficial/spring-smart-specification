package br.com.technow.springsmartspecification.mongo.query;

import br.com.technow.springsmartspecification.mongo.valuetypehandler.DefaultMongoSpecificationFieldValueTypeHandler;

public class MongoSpecificationFieldValueTypeHandlerFactory {

    private final static MongoSpecificationFieldValueTypeHandler DEFAULT = new DefaultMongoSpecificationFieldValueTypeHandler();

    public static MongoSpecificationFieldValueTypeHandler get(Class<?> type) {
        return DEFAULT;
    }

}
