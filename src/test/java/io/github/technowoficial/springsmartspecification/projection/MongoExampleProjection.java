package io.github.technowoficial.springsmartspecification.projection;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MongoExampleProjection {

    private String id;
    private String name;

    private String childId;

    @Value("#{target.child.fullName}")
    private String childName;

}
