package io.github.technowoficial.springsmartspecification.jpa.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import io.github.technowoficial.springsmartspecification.exception.ReflectionException;
import io.github.technowoficial.springsmartspecification.path.PathPartExtractor;
import io.github.technowoficial.springsmartspecification.reflection.ReflectionUtils;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecification;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecificationFieldMeta;

public class JpaSpecificationField {

    private final SmartSpecificationFieldMeta fieldMeta;
    private final JpaSpecificationFieldValueTypeHandler valueTypeHandler;
    private final List<JpaSpecificationFieldExpressionHandler> expressionHandlers;
    private final JpaSpecificationFieldPredicateBuilder predicateBuilder;

    public JpaSpecificationField(SmartSpecificationFieldMeta fieldMeta) {
        String condition = fieldMeta.getCondition() == null ? "" : fieldMeta.getCondition();
        PathPartExtractor pathPartExtractor = new PathPartExtractor(condition);
        this.fieldMeta = fieldMeta;
        this.valueTypeHandler = JpaSpecificationFieldValueTypeHandlerFactory
                .get(fieldMeta.getSpecificationField().getType());
        this.expressionHandlers = JpaSpecificationFieldExpressionHandlerFactory
                .getList(pathPartExtractor);
        this.predicateBuilder = JpaSpecificationFieldPredicateBuilderFactory
                .get(pathPartExtractor);
        validatePathPartExtractor(pathPartExtractor);
    }

    public boolean isValidValue(SmartSpecification<?> specification) {
        Object value = getValue(specification);
        return valueTypeHandler.isValidValue(value);
    }

    public Object getValue(SmartSpecification<?> specification) {
        try {
            Field rawField = fieldMeta.getSpecificationField();
            rawField.setAccessible(true);
            return rawField.get(specification);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public Predicate buildPredicate(JpaSpecificationArgs args) {
        if (isValidValue(args.getSpecification())) {
            args = createNewArgsWithExpressions(args, fieldMeta.getCustomMethod() != null);
            args = handleArgsExpressions(args);
            if (fieldMeta.getCustomMethod() != null) {
                SmartSpecification<?> specification = args.getSpecification();
                return ReflectionUtils.invokeFirstMethod(specification, fieldMeta.getCustomMethod(), args);
            }
            return predicateBuilder.build(args);
        }
        return args.getCriteriaBuilder().and();
    }

    public SmartSpecificationFieldMeta getFieldMeta() {
        return fieldMeta;
    }

    private JpaSpecificationArgs createNewArgsWithExpressions(JpaSpecificationArgs args, boolean withCustomMethod) {
        args = args.createNewArgs(this);
        List<Object> expressions = new ArrayList<>();
        if (withCustomMethod) {
            expressions.add(tryGetPath(args));
        } else {
            expressions.add(args.getPath());
        }
        List<Object> valueExpressions = valueTypeHandler.getValueExpressions(args);
        expressions.addAll(valueExpressions);
        args = args.createNewArgs(expressions);
        return args;
    }

    private Object tryGetPath(JpaSpecificationArgs args) {
        try {
            return args.getPath();
        } catch (Exception e) {
            return null;
        }
    }

    private JpaSpecificationArgs handleArgsExpressions(JpaSpecificationArgs args) {
        for (JpaSpecificationFieldExpressionHandler expressionHandler : expressionHandlers) {
            List<Object> expressions = expressionHandler.handle(args);
            args = args.createNewArgs(expressions);
        }
        return args;
    }

    private void validatePathPartExtractor(PathPartExtractor pathPartExtractor) {
        if (!pathPartExtractor.isEmpty()) {
            String message = String.format("%s: invalid pattern '%s'", getTypeAndFieldName(), fieldMeta.getCondition());
            throw new IllegalArgumentException(message);
        }
    }

    private String getTypeAndFieldName() {
        return String.format("%s.%s", fieldMeta.getSpecificationField().getDeclaringClass().getSimpleName(),
                fieldMeta.getSpecificationField().getName());
    }

}
