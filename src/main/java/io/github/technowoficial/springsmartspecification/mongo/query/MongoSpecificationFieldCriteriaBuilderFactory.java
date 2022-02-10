package io.github.technowoficial.springsmartspecification.mongo.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.ContainsMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.EndsWithMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.EqualsMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.GreaterThanEqualMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.GreaterThanMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.InMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.IsFalseMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.IsNotNullMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.IsNullMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.IsTrueMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.LessThanEqualMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.LessThanMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.NotEqualsMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.NotInMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.RegexMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.mongo.criteriabuilder.StartsWithMongoSpecificationFieldCriteriaBuilder;
import io.github.technowoficial.springsmartspecification.path.PathPart;
import io.github.technowoficial.springsmartspecification.path.PathPartExtractor;

public class MongoSpecificationFieldCriteriaBuilderFactory {

    private static final MongoSpecificationFieldCriteriaBuilder DEFAULT = new EqualsMongoSpecificationFieldCriteriaBuilder();
    private static final MongoSpecificationFieldCriteriaBuilder[] BUILDERS = new MongoSpecificationFieldCriteriaBuilder[] {
            DEFAULT,
            new InMongoSpecificationFieldCriteriaBuilder(),
            new ContainsMongoSpecificationFieldCriteriaBuilder(),
            new LessThanMongoSpecificationFieldCriteriaBuilder(),
            new LessThanEqualMongoSpecificationFieldCriteriaBuilder(),
            new GreaterThanMongoSpecificationFieldCriteriaBuilder(),
            new GreaterThanEqualMongoSpecificationFieldCriteriaBuilder(),
            new IsNullMongoSpecificationFieldCriteriaBuilder(),
            new IsNotNullMongoSpecificationFieldCriteriaBuilder(),
            new StartsWithMongoSpecificationFieldCriteriaBuilder(),
            new EndsWithMongoSpecificationFieldCriteriaBuilder(),
            new NotEqualsMongoSpecificationFieldCriteriaBuilder(),
            new IsTrueMongoSpecificationFieldCriteriaBuilder(),
            new IsFalseMongoSpecificationFieldCriteriaBuilder(),
            new NotInMongoSpecificationFieldCriteriaBuilder(),
            new RegexMongoSpecificationFieldCriteriaBuilder()
    };

    private static Map<String, MongoSpecificationFieldCriteriaBuilder> buildersMap = new HashMap<>();
    private static List<String> patternList = new ArrayList<>();

    static {
        for (MongoSpecificationFieldCriteriaBuilder builder : BUILDERS) {
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

    public static MongoSpecificationFieldCriteriaBuilder getDefault() {
        return DEFAULT;
    }

    public static MongoSpecificationFieldCriteriaBuilder get(String pattern) {
        return buildersMap.get(pattern);
    }

    public static List<String> getPatterns() {
        return Collections.unmodifiableList(patternList);
    }

    public static MongoSpecificationFieldCriteriaBuilder get(PathPartExtractor pathPartExtractor) {
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
