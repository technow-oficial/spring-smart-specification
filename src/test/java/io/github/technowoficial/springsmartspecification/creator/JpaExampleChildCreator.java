package io.github.technowoficial.springsmartspecification.creator;

import io.github.technowoficial.springsmartspecification.domain.JpaExampleChild;

public class JpaExampleChildCreator {

    public static JpaExampleChild createAlex() {
        return JpaExampleChild.builder()
                .fullName("Alex")
                .build();
    }

}
