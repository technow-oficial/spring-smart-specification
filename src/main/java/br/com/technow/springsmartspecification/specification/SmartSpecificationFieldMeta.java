package br.com.technow.springsmartspecification.specification;

import java.lang.reflect.Field;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmartSpecificationFieldMeta {

    private List<String> path;
    private String condition;
    private String customMethod;
    private Field specificationField;

}
