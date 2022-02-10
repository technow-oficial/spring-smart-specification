package io.github.technowoficial.springsmartspecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.technowoficial.springsmartspecification.domain.JpaExampleChild;

@Repository
public interface JpaExampleChildRepository extends JpaRepository<JpaExampleChild, Long> {

}
