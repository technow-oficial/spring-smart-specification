package br.com.technow.springsmartspecification.specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.technow.springsmartspecification.path.PathPart;
import br.com.technow.springsmartspecification.path.PathPartExtractor;
import br.com.technow.springsmartspecification.path.PathUtils;
import br.com.technow.springsmartspecification.reflection.ReflectionUtils;

public class SmartSpecificationMetaFactory {

    private final static Map<Class<?>, SmartSpecificationMeta> metaMap = new HashMap<>();

    public static <T extends SmartSpecification<?>> SmartSpecificationMeta get(T specification) {
        Class<?> specificationClass = specification.getClass();
        SmartSpecificationMeta meta = metaMap.get(specificationClass);
        if (meta == null) {
            meta = new SmartSpecificationMeta();
            meta.setSpecificationClass(specificationClass);
            meta.setFields(parseFields(specification));
            metaMap.put(specificationClass, meta);
        }
        return meta;
    }

    private static <T extends SmartSpecification<?>> List<SmartSpecificationFieldMeta> parseFields(
            T specification) {
        List<SmartSpecificationFieldMeta> fields = new ArrayList<>();
        for (Field rawField : ReflectionUtils.listPropFields(specification.getClass())) {
            fields.add(parseField(specification, rawField));
        }
        return fields;
    }

    private static SmartSpecificationFieldMeta parseField(SmartSpecification<?> specification, Field rawField) {
        SmartSpecificationFieldMeta fieldMeta = new SmartSpecificationFieldMeta();
        SmartSpecificationField fieldAnnotation = rawField.getAnnotation(SmartSpecificationField.class);
        validateFieldAnnotation(specification, rawField, fieldAnnotation);
        fieldMeta.setSpecificationField(rawField);
        fieldMeta.setPath(getPath(specification, rawField, fieldAnnotation));
        PathPartExtractor pathPartExtractor = getPathPartExtrator(specification, rawField, fieldAnnotation);
        fieldMeta.setCondition(getCondition(pathPartExtractor, rawField, fieldAnnotation));
        fieldMeta.setCustomMethod(getCustomMethod(pathPartExtractor, rawField, fieldAnnotation));
        validateFieldMeta(specification, fieldMeta);
        return fieldMeta;
    }

    private static void validateFieldAnnotation(SmartSpecification<?> spec, Field rawField,
            SmartSpecificationField fieldAnnotation) {
        if (fieldAnnotation != null) {
            String fieldName = spec.getClass().getSimpleName() + ":" + rawField.getName();
            if (!fieldAnnotation.path().isEmpty() && fieldAnnotation.condition().isEmpty()
                    && fieldAnnotation.customMethod().isEmpty()) {
                throw new IllegalArgumentException(
                        fieldName + " : Must have a condition or a customMethod");
            }
            if (!fieldAnnotation.customMethod().isEmpty() && !fieldAnnotation.condition().isEmpty()) {
                throw new IllegalArgumentException(
                        fieldName + " : Can't have both a condition and a customMethod");
            }
        }
    }

    private static void validateFieldMeta(SmartSpecification<?> specification, SmartSpecificationFieldMeta fieldMeta) {
        if (fieldMeta.getCondition() != null && fieldMeta.getCustomMethod() != null) {
            throw new IllegalArgumentException(
                    "Field " + fieldMeta.getSpecificationField().getName()
                            + " can't have both condition and custom method");
        }
        if (!fieldMeta.getPath().isEmpty()) {
            String fieldPath = PathUtils.join(fieldMeta.getPath(), ".");
            try {
                String fieldName = PathUtils.join(fieldMeta.getPath(), "");
                ReflectionUtils.extractPath(specification.getDomainClass(), fieldName, false);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Field " + fieldMeta.getSpecificationField().getName() + " path " + fieldPath
                                + " is not valid for class " + specification.getDomainClass().getName());
            }
        }
    }

    private static String getCustomMethod(PathPartExtractor pathPartExtractor, Field rawField,
            SmartSpecificationField fieldAnnotation) {
        if (fieldAnnotation == null || fieldAnnotation.customMethod().isEmpty()) {
            return null;
        }
        return fieldAnnotation.customMethod();
    }

    private static String getCondition(PathPartExtractor pathPartExtractor, Field rawField,
            SmartSpecificationField fieldAnnotation) {
        if (fieldAnnotation != null && !fieldAnnotation.condition().isEmpty()) {
            return fieldAnnotation.condition();
        } else if (pathPartExtractor != null && !pathPartExtractor.isEmpty()) {
            if (fieldAnnotation == null || fieldAnnotation.customMethod().isEmpty()) {
                return pathPartExtractor.getString();
            }
        }
        return null;
    }

    private static PathPartExtractor getPathPartExtrator(SmartSpecification<?> specification, Field rawField,
            SmartSpecificationField fieldAnnotation) {
        if (fieldAnnotation != null && !fieldAnnotation.path().isEmpty()) {
            return null;
        }
        List<PathPart> pathPartList = ReflectionUtils.extractPath(specification.getDomainClass(), rawField.getName(),
                true);
        PathPartExtractor pathPartExtractor = new PathPartExtractor(rawField.getName());
        pathPartExtractor.ignore(pathPartList);
        return pathPartExtractor;
    }

    private static List<String> getPath(SmartSpecification<?> specification, Field rawField,
            SmartSpecificationField fieldAnnotation) {
        if (fieldAnnotation == null || fieldAnnotation.path().isEmpty()) {
            List<PathPart> pathPartList = ReflectionUtils.extractPath(specification.getDomainClass(),
                    rawField.getName(),
                    true);
            return pathPartList.stream().map(p -> p.getPattern()).collect(Collectors.toList());
        } else {
            String fieldPath = fieldAnnotation.path();
            return PathUtils.convertStringToList(fieldPath);
        }
    }

}
