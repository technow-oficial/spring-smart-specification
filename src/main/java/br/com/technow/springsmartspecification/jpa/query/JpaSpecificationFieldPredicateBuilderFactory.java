package br.com.technow.springsmartspecification.jpa.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.technow.springsmartspecification.jpa.predicatebuilder.ContainsJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.EndsWithJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.EqualsJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.GreaterThanEqualJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.GreaterThanJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.InJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.IsFalseJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.IsNotNullJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.IsNullJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.IsTrueJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.LessThanEqualJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.LessThanJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.LikeJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.NotEqualsJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.NotInJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.NotLikeJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.jpa.predicatebuilder.StartsWithJpaSpecificationFieldPredicateBuilder;
import br.com.technow.springsmartspecification.path.PathPart;
import br.com.technow.springsmartspecification.path.PathPartExtractor;

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
