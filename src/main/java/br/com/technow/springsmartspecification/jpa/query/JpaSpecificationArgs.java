package br.com.technow.springsmartspecification.jpa.query;

import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;

import br.com.technow.springsmartspecification.specification.SmartSpecification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class JpaSpecificationArgs {

    private SmartSpecification<?> specification;
    private List<Object> expressions;

    private JpaPathRouter router;
    private CriteriaQuery<?> criteriaQuery;
    private CriteriaBuilder criteriaBuilder;

    private JpaSpecificationField field;

    public JpaSpecificationArgs createNewArgs(List<Object> expressions) {
        return JpaSpecificationArgs.builder()
                .specification(specification)
                .expressions(Collections.unmodifiableList(expressions))
                .router(router)
                .criteriaQuery(criteriaQuery)
                .criteriaBuilder(criteriaBuilder)
                .field(field)
                .build();
    }

    public JpaSpecificationArgs createNewArgs(JpaSpecificationField field) {
        return JpaSpecificationArgs.builder()
                .specification(specification)
                .expressions(expressions)
                .router(router)
                .criteriaQuery(criteriaQuery)
                .criteriaBuilder(criteriaBuilder)
                .field(field)
                .build();
    }

    public Object getValue() {
        return field.getValue(specification);
    }

    public Path<?> getPath() {
        return router.getPath(field.getFieldMeta().getPath());
    }

}
