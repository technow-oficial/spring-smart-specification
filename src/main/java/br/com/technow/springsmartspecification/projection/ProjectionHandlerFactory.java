package br.com.technow.springsmartspecification.projection;

import java.util.HashMap;
import java.util.Map;

public class ProjectionHandlerFactory {

    private static final Map<String, ProjectionHandler<?, ?>> projectionHandlerMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <D, P> ProjectionHandler<D, P> get(Class<D> domainClass, Class<P> projectionClass) {
        String key = getKey(domainClass, projectionClass);
        ProjectionHandler<D, P> projectionHandler = (ProjectionHandler<D, P>) projectionHandlerMap.get(key);
        if (projectionHandler == null) {
            projectionHandler = new ProjectionHandler<>(domainClass, projectionClass);
            projectionHandlerMap.put(key, projectionHandler);
        }
        return projectionHandler;
    }

    private static <D, P> String getKey(Class<D> domainClass, Class<P> projectionClass) {
        return domainClass.getName() + "-" + projectionClass.getName();
    }

}
