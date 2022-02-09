package br.com.technow.springsmartspecification.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.technow.springsmartspecification.jpa.query.JpaCriteriaQueryHandler;
import br.com.technow.springsmartspecification.specification.SmartSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultJpaSmartSpecificationRepository<D> implements JpaSmartSpecificationRepository<D> {

    private final EntityManager em;

    @Override
    public long count(SmartSpecification<D> specification) {
        return new JpaCriteriaQueryHandler<>(em, specification).count();
    }

    @Override
    public boolean exists(SmartSpecification<D> specification) {
        return new JpaCriteriaQueryHandler<>(em, specification).exists();
    }

    @Override
    public Optional<D> findOne(SmartSpecification<D> specification) {
        return new JpaCriteriaQueryHandler<>(em, specification).findOne();
    }

    @Override
    public List<D> findAll(SmartSpecification<D> specification) {
        return new JpaCriteriaQueryHandler<>(em, specification).findAll();
    }

    @Override
    public Page<D> findAll(SmartSpecification<D> specification, Pageable pageable) {
        return new JpaCriteriaQueryHandler<>(em, specification).findAll(pageable);
    }

    @Override
    public <P> Optional<P> findOneProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification) {
        return new JpaCriteriaQueryHandler<>(em, specification).findOneProjectedBy(projectionClass);
    }

    @Override
    public <P> List<P> findAllProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification) {
        return new JpaCriteriaQueryHandler<>(em, specification).findAllProjectedBy(projectionClass);
    }

    @Override
    public <P> Page<P> findAllProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification,
            Pageable pageable) {
        return new JpaCriteriaQueryHandler<>(em, specification).findAllProjectedBy(projectionClass, pageable);
    }

}
