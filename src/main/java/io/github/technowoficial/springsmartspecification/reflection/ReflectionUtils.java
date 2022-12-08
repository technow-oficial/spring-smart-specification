package io.github.technowoficial.springsmartspecification.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Transient;

import io.github.technowoficial.springsmartspecification.exception.ReflectionException;
import io.github.technowoficial.springsmartspecification.path.PathPart;

public class ReflectionUtils {

    public static List<PathPart> extractPath(Class<?> domainClass, String fieldName, boolean stopOnNotFound) {
        List<PathPart> result = new ArrayList<>();
        PathPart last = null;
        String str = fieldName;
        Class<?> currentClass = domainClass;
        while (!str.isEmpty()) {
            Optional<Field> fieldOptional = findFirstField(currentClass, str);
            if (fieldOptional.isPresent()) {
                Field field = fieldOptional.get();
                PathPart actual = new PathPart(0, field.getName(), str.substring(0, field.getName().length()));
                currentClass = field.getType();
                if (last != null) {
                    actual.setIndex(actual.getIndex() + last.getIndex() + last.getPattern().length());
                }
                str = str.substring(actual.getPattern().length());
                result.add(actual);
                last = actual;
                if (isNativeClass(currentClass)) {
                    break;
                }
            } else if (stopOnNotFound) {
                break;
            } else {
                throw new ReflectionException("Field " + fieldName + " not found in class " + domainClass.getName());
            }
        }
        return result;
    }

    private static boolean isNativeClass(Class<?> cls) {
        return cls.getName().startsWith("java.");
    }

    private static Optional<Field> findFirstField(Class<?> domainClass, String str) {
        Field result = null;
        for (Field field : listPropFields(domainClass)) {
            if (str.toLowerCase().startsWith(field.getName().toLowerCase())) {
                result = field;
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    public static List<Field> listPropFields(Class<?> domainClass) {
        List<Field> fields = new ArrayList<>();
        for (Field field : domainClass.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Transient.class)) {
                if (!isStatic(field)) {
                    // field.setAccessible(true);
                    fields.add(field);
                }
            }
        }
        if (domainClass.getSuperclass() != null && !Object.class.equals(domainClass.getSuperclass())) {
            fields.addAll(listPropFields(domainClass.getSuperclass()));
        }
        fields.sort((a, b) -> {
            return a.getName().length() - b.getName().length();
        });
        return fields;
    }

    public static Field getPropField(Class<?> domainClass, String fieldName) {
        for (Field field : listPropFields(domainClass)) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return field;
            }
        }
        throw new ReflectionException("Field " + fieldName + " not found in class " + domainClass.getName());
    }

    public static boolean isStatic(Field field) {
        return java.lang.reflect.Modifier.isStatic(field.getModifiers());
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeFirstMethod(Object domain, String methodName, Object... args) {
        try {
            Method method = findFirstMethod(domain.getClass(), methodName);
            method.setAccessible(true);
            return (T) method.invoke(domain, args);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Method findFirstMethod(Class<?> domainClass, String methodName) {
        for (Method method : domainClass.getDeclaredMethods()) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        throw new ReflectionException("Method " + methodName + " not found in class " + domainClass.getName());
    }

    public static <P> P newInstance(Class<P> cls) {
        try {
            return cls.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new ReflectionException(e);
        }
    }

    public static Object getFieldValue(Object domain, List<String> path) {
        Object result = domain;
        for (String current : path) {
            try {
                if (result == null) {
                    return null;
                }
                Field field = getPropField(result.getClass(), current);
                field.setAccessible(true);
                result = field.get(result);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ReflectionException(e);
            }
        }
        return result;
    }

}
