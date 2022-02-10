package br.com.technow.springsmartspecification.creator;

import java.time.LocalDateTime;

import br.com.technow.springsmartspecification.domain.MongoExample;

public class MongoExampleCreator {

    public static MongoExample createEx1() {
        return MongoExample.builder()
                .name("Ex1")
                .timestamp(LocalDateTime.of(2000, 01, 02, 03, 04))
                .active(true)
                .value(1L)
                .build();
    }

    public static MongoExample createEx2() {
        return MongoExample.builder()
                .name("Ex2")
                .timestamp(LocalDateTime.of(2010, 11, 12, 13, 14))
                .active(false)
                .value(2L)
                .build();
    }

    public static MongoExample createEx3() {
        return MongoExample.builder()
                .name("Ex3")
                .timestamp(LocalDateTime.now())
                .value(3L)
                .build();
    }

}
