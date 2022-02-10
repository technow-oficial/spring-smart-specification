package io.github.technowoficial.springsmartspecification.jpa.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.ContainsJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.EndsWithJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.EqualsJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.GreaterThanEqualJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.GreaterThanJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.InJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.IsFalseJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.IsNotNullJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.IsNullJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.IsTrueJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.LessThanEqualJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.LessThanJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.LikeJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.NotEqualsJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.NotInJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.NotLikeJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.jpa.predicatebuilder.StartsWithJpaSpecificationFieldPredicateBuilder;
import io.github.technowoficial.springsmartspecification.path.PathPart;
import io.github.technowoficial.springsmartspecification.path.PathPartExtractor;

public class JpaSpecificationFieldPredicateBuilderFactory {

    private static final JpaSpecificationFieldPredicateBuilder DEFAULT = new EqualsJpaSpecificationFieldPredicateBuilder();
    private static final JpaSpecificationFieldPredicateBuilder[] BUILDERS = new JpaSpecificationFieldPredicateBuilder[] {
            DEFAULT,
            new ContainsJpaSpecificationFieldPredicateBuilder(),
            new LessThanJpaSpecificationFieldPredicateBuilder(),
            new LessThanEqualJpaSpecificationFieldPredicateBuilder(),
            new GreaterThanJpaSpecificationFieldPredicateBuilder(),
            new GreaterThanEqualJpaSpecificationFieldPredicateBuilder(),
            new IsNullJpaSpecificationFieldPredicateBuilder(),
            new IsNotNullJpaSpecificationFieldPredicateBuilder(),
            new LikeJpaSpecificationFieldPredicateBuilder(),
            new NotLikeJpaSpecificationFieldPredicateBuilder(),
            new StartsWithJpaSpecificationFieldPredicateBuilder(),
            new EndsWithJpaSpecificationFieldPredicateBuilder(),
            new NotEqualsJpaSpecificationFieldPredicateBuilder(),
            new IsTrueJpaSpecificationFieldPredicateBuilder(),
            new IsFalseJpaSpecificationFieldPredicateBuilder(),
            new InJpaSpecificationFieldPredicateBuilder(),
            new NotInJpaSpecificationFieldPredicateBuilder()
    };

    private static Map<String, JpaSpecificationFieldPredicateBuilder> buildersMap = new HashMap<>();
    private static List<String> patternList = new ArrayList<>();

    static {
        for (JpaSpecificationFieldPredicateBuilder builder : BUILDERS) {
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

    public static JpaSpecificationFieldPredicateBuilder getDefault() {
        return DEFAULT;
    }

    public static JpaSpecificationFieldPredicateBuilder get(String pattern) {
        return buildersMap.get(pattern);
    }

    public static List<String> getPatterns() {
        return Collections.unmodifiableList(patternList);
    }

    public static JpaSpecificationFieldPredicateBuilder get(PathPartExtractor pathPartExtractor) {
        if (!pathPartExtractor.isEmpty()) {
            for (String pattern : patternList) {
                Optional<PathPart> pathPartOptional = pathPartExtractor.tryExtract(pattern);
                if (pathPartOptional.isPresent()) {
                    return buildersMap.get(pattern);
                }
            }
        }
        return DEFAULT;
    }

}
