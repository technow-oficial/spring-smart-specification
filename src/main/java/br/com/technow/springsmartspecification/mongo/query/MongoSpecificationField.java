package br.com.technow.springsmartspecification.mongo.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.CriteriaDefinition;

import br.com.technow.springsmartspecification.exception.ReflectionException;
import br.com.technow.springsmartspecification.path.PathPartExtractor;
import br.com.technow.springsmartspecification.reflection.ReflectionUtils;
import br.com.technow.springsmartspecification.specification.SmartSpecification;
import br.com.technow.springsmartspecification.specification.SmartSpecificationFieldMeta;

public class MongoSpecificationField {

    private final SmartSpecificationFieldMeta fieldMeta;
    private final MongoSpecificationFieldValueTypeHandler valueTypeHandler;
    private final List<MongoSpecificationFieldExpressionHandler> expressionHandlers;
    private final MongoSpecificationFieldCriteriaBuilder criteriaBuilder;

    public MongoSpecificationField(SmartSpecificationFieldMeta fieldMeta) {
        String condition = fieldMeta.getCondition() == null ? "" : fieldMeta.getCondition();
        PathPartExtractor pathPartExtractor = new PathPartExtractor(condition);
        this.fieldMeta = fieldMeta;
        this.valueTypeHandler = MongoSpecificationFieldValueTypeHandlerFactory
                .get(fieldMeta.getSpecificationField().getType());
        this.expressionHandlers = MongoSpecificationFieldExpressionHandlerFactory.getList(pathPartExtractor);
        this.criteriaBuilder = MongoSpecificationFieldCriteriaBuilderFactory.get(pathPartExtractor);
        validatePathPartExtractor(pathPartExtractor);
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

    public boolean isValidValue(SmartSpecification<?> specification) {
        Object value = getValue(specification);
        return valueTypeHandler.isValidValue(value);
    }

    public Object getValue(SmartSpecification<?> specification) {
        try {
            Field field = fieldMeta.getSpecificationField();
            field.setAccessible(true);
            return field.get(specification);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new ReflectionException(e);
        }
    }

    public CriteriaDefinition buildCriteria(MongoSpecificationArgs args) {
        args = createArgsWithExpressions(args);
        args = handleArgsExpressions(args);
        String customMethod = args.getField().getFieldMeta().getCustomMethod();
        if (customMethod == null) {
            return criteriaBuilder.build(args);
        }
        return ReflectionUtils.invokeFirstMethod(args.getSpecification(), customMethod, args);
    }

    private MongoSpecificationArgs createArgsWithExpressions(MongoSpecificationArgs args) {
        List<Object> expressions = new ArrayList<>();
        expressions.add(args.getPath());
        expressions.add(args.getValue());
        return args.createNewArgs(expressions);
    }

    private MongoSpecificationArgs handleArgsExpressions(MongoSpecificationArgs args) {
        for (MongoSpecificationFieldExpressionHandler expressionHandler : expressionHandlers) {
            List<Object> expressions = expressionHandler.handle(args);
            args = args.createNewArgs(expressions);
        }
        return args;
    }

    public SmartSpecificationFieldMeta getFieldMeta() {
        return fieldMeta;
    }

}