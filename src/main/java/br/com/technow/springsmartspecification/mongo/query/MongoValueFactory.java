package br.com.technow.springsmartspecification.mongo.query;

public class MongoValueFactory {

    public static MongoValue get(Object value) {
        if (value instanceof MongoValue) {
            return (MongoValue) value;
        }
        return new MongoValue(value);
    }

}
