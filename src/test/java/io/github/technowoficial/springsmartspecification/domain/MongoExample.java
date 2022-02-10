package io.github.technowoficial.springsmartspecification.domain;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MongoExample {

    @Id
    private String id;
    private String name;
    private LocalDateTime timestamp;
    private Boolean active;
    private Long value;
    private MongoExampleChild child;

}
