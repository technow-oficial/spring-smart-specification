package io.github.technowoficial.springsmartspecification.mongo.query;

import java.util.Collections;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import io.github.technowoficial.springsmartspecification.path.PathUtils;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class MongoSpecificationArgs {

    private Query query;
    private SmartSpecification<?> specification;
    private MongoSpecificationField field;
    private List<Object> expressions;

    public MongoSpecificationArgs createNewArgs(List<Object> expressions) {
        return MongoSpecificationArgs.builder()
                .query(query)
                .specification(specification)
                .field(field)
                .expressions(Collections.unmodifiableList(expressions))
                .build();
    }

    public Object getValue() {
        return field.getValue(specification);
    }

    public String getPath() {
        return PathUtils.join(field.getFieldMeta().getPath(), ".");
    }

}
