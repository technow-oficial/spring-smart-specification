package io.github.technowoficial.springsmartspecification.creator;

import io.github.technowoficial.springsmartspecification.domain.MongoExampleChild;

public class MongoExampleChildCreator {

    public static MongoExampleChild createAlex() {
        return MongoExampleChild.builder()
                .fullName("Alex")
                .build();
    }

}
