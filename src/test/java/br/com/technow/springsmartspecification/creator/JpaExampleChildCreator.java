package br.com.technow.springsmartspecification.creator;

import br.com.technow.springsmartspecification.domain.JpaExampleChild;

public class JpaExampleChildCreator {

    public static JpaExampleChild createAlex() {
        return JpaExampleChild.builder()
                .fullName("Alex")
                .build();
    }

}
