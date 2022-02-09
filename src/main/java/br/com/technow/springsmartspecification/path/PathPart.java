package br.com.technow.springsmartspecification.path;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PathPart {

    private int index;
    private String pattern;
    private String part;

}
