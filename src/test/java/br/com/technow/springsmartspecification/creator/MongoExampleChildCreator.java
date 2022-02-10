package br.com.technow.springsmartspecification.creator;

import br.com.technow.springsmartspecification.domain.MongoExampleChild;

public class MongoExampleChildCreator {

    public static MongoExampleChild createAlex() {
        return MongoExampleChild.builder()
                .fullName("Alex")
                .build();
    }

}
