package br.com.technow.springsmartspecification.mongo.query;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MongoSpecificationMeta {

    private final List<MongoSpecificationField> fields;

    public List<MongoSpecificationField> getFields() {
        return fields;
    }

}
