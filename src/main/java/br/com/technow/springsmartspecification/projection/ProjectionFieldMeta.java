package br.com.technow.springsmartspecification.projection;

import java.lang.reflect.Field;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProjectionFieldMeta {

    private int index;
    private Field field;
    private List<String> path;

}
