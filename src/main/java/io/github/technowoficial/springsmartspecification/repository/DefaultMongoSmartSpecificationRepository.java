package io.github.technowoficial.springsmartspecification.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import io.github.technowoficial.springsmartspecification.mongo.query.MongoCriteriaQueryHandler;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultMongoSmartSpecificationRepository<D> implements BaseSmartSpecificationRepository<D> {

    private final MongoTemplate mongoTemplate;

    @Override
    public long count(SmartSpecification<D> specification) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).count();
    }

    @Override
    public boolean exists(SmartSpecification<D> specification) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).exists();
    }

    @Override
    public Optional<D> findOne(SmartSpecification<D> specification) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).findOne();
    }

    @Override
    public List<D> findAll(SmartSpecification<D> specification) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).findAll();
    }

    @Override
    public Page<D> findAll(SmartSpecification<D> specification, Pageable pageable) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).findAll(pageable);
    }

    @Override
    public <P> Optional<P> findOneProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).findOneProjectedBy(projectionClass);
    }

    @Override
    public <P> List<P> findAllProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).findAllProjectedBy(projectionClass);
    }

    @Override
    public <P> Page<P> findAllProjectedBy(Class<P> projectionClass, SmartSpecification<D> specification,
            Pageable pageable) {
        return new MongoCriteriaQueryHandler<>(mongoTemplate, specification).findAllProjectedBy(projectionClass,
                pageable);
    }

}
