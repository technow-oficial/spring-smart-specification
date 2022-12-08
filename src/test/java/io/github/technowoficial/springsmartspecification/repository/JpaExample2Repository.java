package io.github.technowoficial.springsmartspecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.technowoficial.springsmartspecification.domain.JpaExample2;

public interface JpaExample2Repository
        extends JpaRepository<JpaExample2, Long>, JpaSmartSpecificationRepository<JpaExample2> {

}
