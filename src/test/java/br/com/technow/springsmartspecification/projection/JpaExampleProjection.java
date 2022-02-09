package br.com.technow.springsmartspecification.projection;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaExampleProjection {

    private Long id;
    private String name;

    private Long childId;

    @Value("#{target.child.fullName}")
    private String childName;

}
