package io.github.technowoficial.springsmartspecification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.technowoficial.springsmartspecification.specification.SmartSpecification;

public interface BaseSmartSpecificationRepository<D> {

    long count(SmartSpecification<D> specification);

    boolean exists(SmartSpecification<D> specification);

    Optional<D> findOne(SmartSpecification<D> specification);

    List<D> findAll(SmartSpecification<D> specification);

    Page<D> findAll(SmartSpecification<D> specification, Pageable pageable);

    <P> Optional<P> findOneProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification);

    <P> List<P> findAllProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification);

    <P> Page<P> findAllProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification, Pageable pageable);

}
