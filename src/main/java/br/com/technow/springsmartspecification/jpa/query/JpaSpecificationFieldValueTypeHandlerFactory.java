package br.com.technow.springsmartspecification.jpa.query;

import java.util.List;

import br.com.technow.springsmartspecification.jpa.valuetypehandler.DefaultJpaSpecificationFieldValueTypeHandler;
import br.com.technow.springsmartspecification.jpa.valuetypehandler.StringJpaSpecificationFieldValueTypeHandler;

public class JpaSpecificationFieldValueTypeHandlerFactory {

    private static final List<JpaSpecificationFieldValueTypeHandler> handlers = List.of(
            new StringJpaSpecificationFieldValueTypeHandler());

    private static final JpaSpecificationFieldValueTypeHandler defaultHandler = new DefaultJpaSpecificationFieldValueTypeHandler();

    public static JpaSpecificationFieldValueTypeHandler get(Class<?> type) {
        for (JpaSpecificationFieldValueTypeHandler typeHandler : handlers) {
            if (typeHandler.supportsType(type)) {
                return typeHandler;
            }
        }
        return defaultHandler;
    }

}
