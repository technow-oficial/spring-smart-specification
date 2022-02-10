package br.com.technow.springsmartspecification.mongo.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.technow.springsmartspecification.mongo.criteriabuilder.ContainsMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.EndsWithMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.EqualsMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.GreaterThanEqualMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.GreaterThanMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.InMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.IsFalseMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.IsNotNullMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.IsNullMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.IsTrueMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.LessThanEqualMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.LessThanMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.NotEqualsMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.NotInMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.RegexMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.mongo.criteriabuilder.StartsWithMongoSpecificationFieldCriteriaBuilder;
import br.com.technow.springsmartspecification.path.PathPart;
import br.com.technow.springsmartspecification.path.PathPartExtractor;

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
