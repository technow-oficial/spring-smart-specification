package br.com.technow.springsmartspecification.repository;

import javax.persistence.EntityManager;

public class JpaSmartSpecificationRepositoryImpl<D> extends DefaultJpaSmartSpecificationRepository<D> {

    public JpaSmartSpecificationRepositoryImpl(EntityManager em) {
        super(em);
    }
    
}
