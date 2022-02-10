package br.com.technow.springsmartspecification.mongo.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.technow.springsmartspecification.mongo.expressionhandler.IgnoreCaseMongoSpecificationFieldExpressionHandler;
import br.com.technow.springsmartspecification.path.PathPart;
import br.com.technow.springsmartspecification.path.PathPartExtractor;

public class MongoSpecificationFieldExpressionHandlerFactory {

    private static final MongoSpecificationFieldExpressionHandler[] BUILDERS = new MongoSpecificationFieldExpressionHandler[] {
            new IgnoreCaseMongoSpecificationFieldExpressionHandler()
    };

    private static final Map<String, MongoSpecificationFieldExpressionHandler> buildersMap = new HashMap<>();
    private static final List<String> patternList = new ArrayList<>();

    static {
        for (MongoSpecificationFieldExpressionHandler builder : BUILDERS) {
            for (String pattern : builder.getPatterns()) {
                if (patternList.contains(pattern)) {
                    throw new IllegalArgumentException("Duplicated pattern: " + pattern);
                }
                buildersMap.put(pattern, builder);
                patternList.add(pattern);
            }
        }
        patternList.sort((a, b) -> b.length() - a.length());
    }

    public static List<MongoSpecificationFieldExpressionHandler> getList(PathPartExtractor pathPartExtractor) {
        List<MongoSpecificationFieldExpressionHandler> result = new ArrayList<>();
        int lastSize;
        if (!pathPartExtractor.isEmpty()) {
            do {
                lastSize = result.size();
                for (String pattern : patternList) {
                    Optional<PathPart> pathPartOptional = pathPartExtractor.tryExtract(pattern);
                    if (pathPartOptional.isPresent()) {
                        result.add(buildersMap.get(pattern));
                        break;
                    }
                }
            } while (result.size() != lastSize && !pathPartExtractor.isEmpty());
        }
        return result;
    }

    public static MongoSpecificationFieldExpressionHandler get(String pattern) {
        return buildersMap.get(pattern);
    }

    public static List<String> getPatterns() {
        return Collections.unmodifiableList(patternList);
    }

}
