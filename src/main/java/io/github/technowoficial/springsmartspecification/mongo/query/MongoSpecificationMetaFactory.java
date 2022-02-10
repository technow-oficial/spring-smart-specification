package io.github.technowoficial.springsmartspecification.mongo.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.technowoficial.springsmartspecification.specification.SmartSpecification;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecificationFieldMeta;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecificationMeta;
import io.github.technowoficial.springsmartspecification.specification.SmartSpecificationMetaFactory;

public class MongoSpecificationMetaFactory {

    private final static Map<Class<?>, MongoSpecificationMeta> metaMap = new HashMap<>();

    public static MongoSpecificationMeta get(SmartSpecification<?> specification) {
        Class<?> specificationClass = specification.getDomainClass();
        MongoSpecificationMeta meta = metaMap.get(specificationClass);
        if (!metaMap.containsKey(specificationClass)) {
            meta = new MongoSpecificationMeta(buildFields(specification));
            metaMap.put(specificationClass, meta);
        }
        return meta;
    }

    private static List<MongoSpecificationField> buildFields(SmartSpecification<?> specification) {
        List<MongoSpecificationField> result = new ArrayList<>();
        SmartSpecificationMeta meta = SmartSpecificationMetaFactory.get(specification);
        for (SmartSpecificationFieldMeta field : meta.getFields()) {
            result.add(new MongoSpecificationField(field));
        }
        return result;
    }

}
