package io.github.technowoficial.springsmartspecification.specification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import io.github.technowoficial.springsmartspecification.domain.MongoExample;
import io.github.technowoficial.springsmartspecification.mongo.query.MongoSpecificationArgs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoExampleSpecification implements SmartSpecification<MongoExample> {

    private String id;
    private List<String> idIn;
    private List<String> idNotIn;
    private String childId;
    private String nameContains;
    private String nameContainsIgnoreCase;
    private String nameEqualsIgnoreCase;
    private String nameStartsWithIgnoreCase;
    private String nameStartsWith;
    private String nameEndsWith;
    private String nameNotEqualsIgnoreCase;
    private String nameRegex;
    private LocalDateTime timestampLessThan;
    private LocalDateTime timestampGreaterThan;

    private Long valueLessThan;
    private Long valueLessThanEqual;
    private Long valueGreaterThanEqual;

    private Boolean activeIsNull;
    private Boolean activeIsNotNull;
    private Boolean activeIsTrue;
    private Boolean activeIsFalse;
    private Boolean childFullNameIsNull;

    @SmartSpecificationField(path = "child.fullName", condition = "Equals")
    private String childName;

    @SmartSpecificationField(path = "id", condition = "eq")
    private String idCanBe;

    @SmartSpecificationField(path = "id", customMethod = "wantThisIdMethod")
    private String wantThisId;

    @Override
    public Class<MongoExample> getDomainClass() {
        return MongoExample.class;
    }

    protected CriteriaDefinition wantThisIdMethod(MongoSpecificationArgs args) {
        return Criteria.where(args.getPath()).is(args.getValue());
    }

}
