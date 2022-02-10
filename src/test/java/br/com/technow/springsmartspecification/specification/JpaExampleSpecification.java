package br.com.technow.springsmartspecification.specification;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.criteria.Predicate;

import br.com.technow.springsmartspecification.domain.JpaExample;
import br.com.technow.springsmartspecification.jpa.query.JpaSpecificationArgs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaExampleSpecification implements SmartSpecification<JpaExample> {

    private Long id;
    private Long idLessThan;
    private Long idLessThanEqual;
    private Long idGreaterThanEqual;
    private Long childId;
    private String nameContains;
    private String nameContainsIgnoreCase;
    private String nameLike;
    private String nameNotLike;
    private String nameStartsWithIgnoreCase;
    private String nameEndsWith;
    private String nameNotEqualsIgnoreCase;
    private String childFullName;
    private String nameStartsWith;
    private LocalDateTime timestampLessThan;
    private LocalDateTime timestampGreaterThan;
    private Boolean activeIsNull;
    private Boolean activeIsNotNull;
    private Boolean activeIsTrue;
    private Boolean activeIsFalse;
    private Boolean childFullNameIsNull;

    private List<Long> idIn;
    private List<Long> idNotIn;

    @SmartSpecificationField(path = "id", condition = "equals")
    private Long idCanBe;

    @SmartSpecificationField(customMethod = "wantThisIdMethod")
    private Long wantThisId;

    @SmartSpecificationField(path = "child.fullName", condition = "eq")
    private String childName;

    @Override
    public Class<JpaExample> getDomainClass() {
        return JpaExample.class;
    }

    protected Predicate wantThisIdMethod(JpaSpecificationArgs args) {
        return args.getCriteriaBuilder().equal(args.getRouter().getPath("id"), args.getValue());
    }

}
