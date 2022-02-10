package br.com.technow.springsmartspecification.mongo.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import br.com.technow.springsmartspecification.path.PathUtils;
import br.com.technow.springsmartspecification.projection.ProjectionFieldMeta;
import br.com.technow.springsmartspecification.projection.ProjectionHandler;
import br.com.technow.springsmartspecification.projection.ProjectionHandlerFactory;
import br.com.technow.springsmartspecification.specification.SmartSpecification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MongoCriteriaQueryHandler<D> {

    private final MongoTemplate mongoTemplate;
    private final SmartSpecification<D> specification;

    public long count() {
        Query query = createQuery();
        return mongoTemplate.count(query, specification.getDomainClass());
    }

    public boolean exists() {
        return count() > 0;
    }

    public Optional<D> findOne() {
        Query query = createQuery();
        D domain = mongoTemplate.findOne(query, specification.getDomainClass());
        return Optional.ofNullable(domain);
    }

    public List<D> findAll() {
        Query query = createQuery();
        return mongoTemplate.find(query, specification.getDomainClass());
    }

    public Page<D> findAll(Pageable pageable) {
        long total = count();
        if (total == 0) {
            return Page.empty(pageable);
        }
        Query query = createQuery();
        handlePabeable(query, pageable);
        handleSort(query, pageable);
        List<D> content = mongoTemplate.find(query, specification.getDomainClass());
        return new PageImpl<>(content, pageable, total);
    }

    public <P> Optional<P> findOneProjectedBy(Class<P> projectionClass) {
        ProjectionHandler<D, P> handler = ProjectionHandlerFactory.get(specification.getDomainClass(), projectionClass);
        Query query = createQuery();
        handleProjection(query, handler);
        D domain = mongoTemplate.findOne(query, specification.getDomainClass());
        P projection = handler.createByDomain(domain);
        return Optional.ofNullable(projection);
    }

    public <P> List<P> findAllProjectedBy(Class<P> projectionClass) {
        ProjectionHandler<D, P> handler = ProjectionHandlerFactory.get(specification.getDomainClass(), projectionClass);
        Query query = createQuery();
        handleProjection(query, handler);
        List<D> domainList = mongoTemplate.find(query, specification.getDomainClass());
        List<P> projectionList = handler.createByDomain(domainList);
        return projectionList;
    }

    public <P> Page<P> findAllProjectedBy(Class<P> projectionClass, Pageable pageable) {
        long total = count();
        if (total == 0) {
            return Page.empty(pageable);
        }
        ProjectionHandler<D, P> handler = ProjectionHandlerFactory.get(specification.getDomainClass(), projectionClass);
        Query query = createQuery();
        handleProjection(query, handler);
        handleSort(query, pageable);
        handlePabeable(query, pageable);
        List<D> domainList = mongoTemplate.find(query, specification.getDomainClass());
        List<P> projectionList = handler.createByDomain(domainList);
        return new PageImpl<>(projectionList, pageable, total);
    }

    private <P> void handleProjection(Query query, ProjectionHandler<D, P> handler) {
        for (ProjectionFieldMeta field : handler.getFields()) {
            String path = PathUtils.join(field.getPath(), ".");
            query.fields().include(path);
        }
    }

    private void handleSort(Query query, Pageable pageable) {
        if (pageable.getSort() != null) {
            query.with(pageable.getSort());
        }
    }

    private void handlePabeable(Query query, Pageable pageable) {
        query.limit(pageable.getPageSize());
        query.skip(pageable.getOffset());
    }

    private Query createQuery() {
        Query query = new Query();
        populateCriterias(query);
        return query;
    }

    private void populateCriterias(Query query) {
        MongoSpecificationMeta meta = getMongoSpecificationMeta();
        for (MongoSpecificationField field : meta.getFields()) {
            if (field.isValidValue(specification)) {
                MongoSpecificationArgs args = MongoSpecificationArgs.builder()
                        .specification(specification)
                        .field(field)
                        .query(query)
                        .build();
                query.addCriteria(field.buildCriteria(args));
            }
        }
    }

    private MongoSpecificationMeta getMongoSpecificationMeta() {
        return MongoSpecificationMetaFactory.get(specification);
    }

}
