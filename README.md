# spring-smart-specification

# How use

```java

    jpaExampleRepository.count(JpaExampleSpecification.builder()
                .nameContains("Luiz")
                .timestampLessThan(LocalDateTime.of(2000, 01, 02, 03, 04))
                .build());

```


```java

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JpaExample {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime timestamp;
    private Boolean active;

}

@Repository
public interface JpaExampleRepository
        extends JpaRepository<JpaExample, Long>, JpaSmartSpecificationRepository<JpaExample> {

}


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaExampleSpecification implements SmartSpecification<JpaExample> {

    private Long id;
    private String nameContains;
    private String nameContainsIgnoreCase;
    private Long childId;
    private LocalDateTime timestampLessThan;
    private LocalDateTime timestampGreaterThan;

}

```

# Maven

```maven
<dependecy>
  <groupId>io.github.technow-oficial</groupId>
  <artifactId>spring-smart-specification</artifactId>
  <version>1.0.0</version>
</dependecy>
```