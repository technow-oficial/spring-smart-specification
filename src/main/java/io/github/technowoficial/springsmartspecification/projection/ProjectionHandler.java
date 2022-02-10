package io.github.technowoficial.springsmartspecification.projection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import io.github.technowoficial.springsmartspecification.exception.ReflectionException;
import io.github.technowoficial.springsmartspecification.path.PathUtils;
import io.github.technowoficial.springsmartspecification.reflection.ReflectionUtils;

public class ProjectionHandler<D, P> {

    private final Class<D> domainClass;
    private final Class<P> projectionClass;
    private List<ProjectionFieldMeta> fields;

    protected ProjectionHandler(Class<D> domainClass, Class<P> projectionClass) {
        this.domainClass = domainClass;
        this.projectionClass = projectionClass;
    }

    private P create() {
        return ReflectionUtils.newInstance(projectionClass);
    }

    public P create(Object[] row) {
        P projection = create();
        for (ProjectionFieldMeta field : fields) {
            try {
                field.getField().setAccessible(true);
                field.getField().set(projection, row[field.getIndex()]);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ReflectionException(e);
            }
        }
        return projection;
    }

    public List<ProjectionFieldMeta> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
            int index = 0;
            for (Field field : ReflectionUtils.listPropFields(projectionClass)) {
                List<String> path = getPath(field);
                fields.add(ProjectionFieldMeta.builder()
                        .field(field)
                        .path(path)
                        .index(index++)
                        .build());
            }
        }
        return fields;
    }

    private List<String> getPath(Field field) {
        if (field.isAnnotationPresent(Value.class)) {
            Value value = field.getAnnotation(Value.class);
            return PathUtils.convertStringToList(value.value());
        } else {
            return ReflectionUtils.extractPath(domainClass, field.getName(), false)
                    .stream()
                    .map(p -> p.getPattern())
                    .collect(Collectors.toList());
        }
    }

    public List<P> create(List<Object[]> rowList) {
        List<P> projectionList = new ArrayList<>();
        for (Object[] row : rowList) {
            projectionList.add(create(row));
        }
        return projectionList;
    }

    public P createByDomain(D domain) {
        if (domain == null) {
            return null;
        }
        P projection = create();
        for (ProjectionFieldMeta field : fields) {
            try {
                Object value = ReflectionUtils.getFieldValue(domain, field.getPath());
                field.getField().setAccessible(true);
                field.getField().set(projection, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ReflectionException(e);
            }
        }
        return projection;
    }

    public List<P> createByDomain(List<D> domainList) {
        List<P> result = new ArrayList<>();
        for (D domain : domainList) {
            result.add(createByDomain(domain));
        }
        return result;
    }

}
