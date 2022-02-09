package br.com.technow.springsmartspecification.jpa.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import br.com.technow.springsmartspecification.specification.SmartSpecification;

public class JpaSpecificationArgsFactory {

    public static JpaSpecificationArgs get(JpaPathRouter router, SmartSpecification<?> specification,
            CriteriaQuery<Long> cq, CriteriaBuilder cb) {
        return null;
    }

}
