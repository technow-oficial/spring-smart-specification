package br.com.technow.springsmartspecification.jpa.valuetypehandler;

import java.util.List;

import br.com.technow.springsmartspecification.jpa.query.JpaSpecificationArgs;
import br.com.technow.springsmartspecification.jpa.query.JpaSpecificationFieldValueTypeHandler;

public class DefaultJpaSpecificationFieldValueTypeHandler implements JpaSpecificationFieldValueTypeHandler {

    @Override
    public boolean supportsType(Class<?> type) {
        return true;
    }

    @Override
    public boolean isValidValue(Object value) {
        return value != null;
    }

    @Override
    public List<Object> getValueExpressions(JpaSpecificationArgs args) {
        return List.of(args.getValue());
    }

}
