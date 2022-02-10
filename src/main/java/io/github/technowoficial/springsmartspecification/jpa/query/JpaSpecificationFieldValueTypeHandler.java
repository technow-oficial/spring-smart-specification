package io.github.technowoficial.springsmartspecification.jpa.query;

import java.util.List;

public interface JpaSpecificationFieldValueTypeHandler {

    boolean supportsType(Class<?> type);

    boolean isValidValue(Object value);

    List<Object> getValueExpressions(JpaSpecificationArgs args);

}
