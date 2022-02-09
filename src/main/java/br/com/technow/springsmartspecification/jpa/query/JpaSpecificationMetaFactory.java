package br.com.technow.springsmartspecification.jpa.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.technow.springsmartspecification.specification.SmartSpecification;
import br.com.technow.springsmartspecification.specification.SmartSpecificationFieldMeta;
import br.com.technow.springsmartspecification.specification.SmartSpecificationMeta;
import br.com.technow.springsmartspecification.specification.SmartSpecificationMetaFactory;

public class JpaSpecificationMetaFactory {

    private final static Map<Class<?>, JpaSpecificationMeta> metaMap = new HashMap<>();

    public static JpaSpecificationMeta get(SmartSpecification<?> specification) {
        Class<?> specificationClass = specification.getClass();
        JpaSpecificationMeta meta = metaMap.get(specificationClass);
        if (meta == null) {
            SmartSpecificationMeta rawMeta = SmartSpecificationMetaFactory.get(specification);
            meta = new JpaSpecificationMeta();
            meta.setFields(parseFields(rawMeta));
            metaMap.put(specificationClass, meta);
        }
        return meta;
    }

    private static List<JpaSpecificationField> parseFields(SmartSpecificationMeta rawMeta) {
        List<JpaSpecificationField> fields = new ArrayList<>();
        for (SmartSpecificationFieldMeta rawField : rawMeta.getFields()) {
            fields.add(new JpaSpecificationField(rawField));
        }
        return fields;
    }

}
