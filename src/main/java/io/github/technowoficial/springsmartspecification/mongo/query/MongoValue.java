package io.github.technowoficial.springsmartspecification.mongo.query;

import java.util.regex.Pattern;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MongoValue {

    private final Object value;
    private boolean ignoreCase;

    public String quote() {
        return Pattern.quote((String) value);
    }

}
