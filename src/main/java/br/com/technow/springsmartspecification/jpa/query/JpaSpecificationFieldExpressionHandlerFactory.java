package br.com.technow.springsmartspecification.jpa.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.technow.springsmartspecification.jpa.expressionhandler.IgnoreCaseJpaSpecificationFieldExpressionHandler;
import br.com.technow.springsmartspecification.path.PathPart;
import br.com.technow.springsmartspecification.path.PathPartExtractor;

public class JpaSpecificationFieldExpressionHandlerFactory {

    private static final JpaSpecificationFieldExpressionHandler[] BUILDERS = new JpaSpecificationFieldExpressionHandler[] {
            new IgnoreCaseJpaSpecificationFieldExpressionHandler()
    };

    private static final Map<String, JpaSpecificationFieldExpressionHandler> buildersMap = new HashMap<>();
    private static final List<String> patternList = new ArrayList<>();

    static {
        for (JpaSpecificationFieldExpressionHandler builder : BUILDERS) {
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

    public static List<JpaSpecificationFieldExpressionHandler> getList(PathPartExtractor pathPartExtractor) {
        List<JpaSpecificationFieldExpressionHandler> result = new ArrayList<>();
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

    public static JpaSpecificationFieldExpressionHandler get(String pattern) {
        return buildersMap.get(pattern);
    }

    public static List<String> getPatterns() {
        return Collections.unmodifiableList(patternList);
    }

}
