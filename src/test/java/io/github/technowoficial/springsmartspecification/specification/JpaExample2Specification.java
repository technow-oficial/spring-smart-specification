package io.github.technowoficial.springsmartspecification.specification;

import io.github.technowoficial.springsmartspecification.domain.JpaExample2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaExample2Specification implements SmartSpecification<JpaExample2> {

    private Long id;
    private String name;

    @Override
    public Class<JpaExample2> getDomainClass() {
        return JpaExample2.class;
    }

}
