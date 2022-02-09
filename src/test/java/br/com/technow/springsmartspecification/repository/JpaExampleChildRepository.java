package br.com.technow.springsmartspecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.technow.springsmartspecification.domain.JpaExampleChild;

@Repository
public interface JpaExampleChildRepository extends JpaRepository<JpaExampleChild, Long> {

}
