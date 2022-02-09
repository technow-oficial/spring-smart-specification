package br.com.technow.springsmartspecification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.technow.springsmartspecification.domain.JpaExample;

@Repository
public interface JpaExampleRepository extends JpaRepository<JpaExample, Long>, JpaSmartSpecificationRepository<JpaExample> {
    
}
