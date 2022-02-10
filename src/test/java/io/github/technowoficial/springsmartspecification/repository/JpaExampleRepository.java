package io.github.technowoficial.springsmartspecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.technowoficial.springsmartspecification.domain.JpaExample;

@Repository
public interface JpaExampleRepository
        extends JpaRepository<JpaExample, Long>, JpaSmartSpecificationRepository<JpaExample> {

}
