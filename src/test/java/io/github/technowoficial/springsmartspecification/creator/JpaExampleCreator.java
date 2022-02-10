package io.github.technowoficial.springsmartspecification.creator;

import java.time.LocalDateTime;

import io.github.technowoficial.springsmartspecification.domain.JpaExample;

public class JpaExampleCreator {

    public static JpaExample createEx1() {
        return JpaExample.builder()
                .name("Ex1")
                .timestamp(LocalDateTime.of(2000, 01, 02, 03, 04))
                .active(true)
                .build();
    }

    public static JpaExample createEx2() {
        return JpaExample.builder()
                .name("Ex2")
                .timestamp(LocalDateTime.of(2010, 11, 12, 13, 14))
                .active(false)
                .build();
    }

    public static JpaExample createEx3() {
        return JpaExample.builder()
                .name("Ex3")
                .timestamp(LocalDateTime.now())
                .build();
    }

}
