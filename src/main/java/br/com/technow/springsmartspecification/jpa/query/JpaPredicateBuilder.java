package br.com.technow.springsmartspecification.jpa.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import br.com.technow.springsmartspecification.specification.SmartSpecification;

public class JpaPredicateBuilder {

    public static Expression<Boolean> build(JpaPathRouter router, SmartSpecification<?> specification,
            CriteriaQuery<?> cq, CriteriaBuilder cb) {
        JpaSpecificationMeta meta = JpaSpecificationMetaFactory.get(specification);
        Predicate result = cb.and();
        for (JpaSpecificationField field : meta.getFields()) {
            if (field.isValidValue(specification)) {
                JpaSpecificationArgs args = JpaSpecificationArgs.builder()
                        .specification(specification)
                        .router(router)
                        .criteriaQuery(cq)
                        .criteriaBuilder(cb)
                        .build();
                result = cb.and(result, field.buildPredicate(args));
            }
        }
        return result;
    }

}
