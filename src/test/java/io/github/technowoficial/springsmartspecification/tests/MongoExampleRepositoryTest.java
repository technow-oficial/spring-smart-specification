package io.github.technowoficial.springsmartspecification.tests;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.technowoficial.springsmartspecification.creator.MongoExampleChildCreator;
import io.github.technowoficial.springsmartspecification.creator.MongoExampleCreator;
import io.github.technowoficial.springsmartspecification.domain.MongoExample;
import io.github.technowoficial.springsmartspecification.domain.MongoExampleChild;
import io.github.technowoficial.springsmartspecification.projection.MongoExampleProjection;
import io.github.technowoficial.springsmartspecification.repository.MongoExampleChildRepository;
import io.github.technowoficial.springsmartspecification.repository.MongoExampleRepository;
import io.github.technowoficial.springsmartspecification.specification.MongoExampleSpecification;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mongo")
public class MongoExampleRepositoryTest {

    @Autowired
    private MongoExampleRepository mongoExampleRepository;

    @Autowired
    private MongoExampleChildRepository mongoExampleChildRepository;

    private MongoExample ex1;
    private MongoExample ex2;
    private MongoExample ex3;

    @BeforeEach
    public void setUp() {

        MongoExampleChild alexChild = MongoExampleChildCreator.createAlex();
        mongoExampleChildRepository.save(alexChild);

        ex1 = MongoExampleCreator.createEx1();
        ex1.setChild(alexChild);

        ex1 = mongoExampleRepository.save(ex1);
        ex2 = mongoExampleRepository.save(MongoExampleCreator.createEx2());
        ex3 = mongoExampleRepository.save(MongoExampleCreator.createEx3());

    }

    @Test
    public void count_Returns3_WhenWithoutSpecification() {
        Assertions.assertThat(mongoExampleRepository.count()).isEqualTo(3);
    }

    @Test
    public void count_Returns3_WhenWithSpecification() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder().build();
        Assertions.assertThat(mongoExampleRepository.count(specification)).isEqualTo(3);
    }

    @Test
    public void count_Returns1_WhenFilterByidEqualsEx1() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id(ex1.getId())
                .build();
        Assertions.assertThat(mongoExampleRepository.count(specification)).isEqualTo(1L);
    }

    @Test
    public void exists_ReturnsTrue_WhenFilterByIdEqualsEx1() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id(ex1.getId())
                .build();
        Assertions.assertThat(mongoExampleRepository.exists(specification)).isTrue();
    }

    @Test
    public void exists_ReturnsFalse_WhenFilterByIdEquals0L() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id("0L")
                .build();
        Assertions.assertThat(mongoExampleRepository.exists(specification)).isFalse();
    }

    @Test
    public void findOne_ReturnsEx1_WhenFilterByIdEqualsEx1() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id(ex1.getId())
                .build();
        Assertions.assertThat(mongoExampleRepository.findOne(specification).get().getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void findOne_ReturnsEmptyOptional_WhenFilterByIdEquals0L() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id("0L")
                .build();
        Assertions.assertThat(mongoExampleRepository.findOne(specification).isEmpty())
                .isTrue();
    }

    @Test
    public void findAll_ReturnsFirstTwoExamples_WhenFilterByIdIn() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .idIn(List.of(ex1.getId(), ex2.getId()))
                .build();
        List<MongoExample> result = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(ex1.getId());
        Assertions.assertThat(result.get(1).getId()).isEqualTo(ex2.getId());
    }

    @Test
    public void findAll_ReturnsFirstTwoExamples_WhenNoFilters() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .build();
        List<MongoExample> result = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(ex1.getId());
        Assertions.assertThat(result.get(1).getId()).isEqualTo(ex2.getId());
        Assertions.assertThat(result.get(2).getId()).isEqualTo(ex3.getId());
    }

    @Test
    public void findAll_ReturnsPageWithFirstTwoExamples_WhenFilterByIdIn() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .idIn(List.of(ex1.getId(), ex2.getId()))
                .build();
        Pageable pageable = Pageable.ofSize(10);
        Page<MongoExample> page = mongoExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(ex1.getId());
        Assertions.assertThat(page.getContent().get(1).getId()).isEqualTo(ex2.getId());
    }

    @Test
    public void findAll_ReturnsPageWithEx3_WhenPageIs1() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .build();
        Pageable pageable = PageRequest.of(1, 2);
        Page<MongoExample> page = mongoExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(1);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(ex3.getId());
    }

    @Test
    public void findAll_ReturnsPageAllExamples_WhenSortedByIdDescAndChildIdDesc() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by("child.fullName").descending().and(Sort.by("name").descending()));
        Page<MongoExample> page = mongoExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(3);
        Assertions.assertThat(page.getContent().get(0).getName()).isEqualTo("Ex1");
        Assertions.assertThat(page.getContent().get(1).getName()).isEqualTo("Ex3");
        Assertions.assertThat(page.getContent().get(2).getName()).isEqualTo("Ex2");
    }

    @Test
    public void findOneProjectedBy_ReturnsEx1Projection_WhenFilterByIdEqualsEx1() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id(ex1.getId())
                .build();
        Optional<MongoExampleProjection> result = mongoExampleRepository.findOneProjectedBy(
                MongoExampleProjection.class,
                specification);
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getId()).isEqualTo(ex1.getId());
        Assertions.assertThat(result.get().getName()).isEqualTo("Ex1");
        Assertions.assertThat(result.get().getChildId()).isEqualTo(ex1.getChild().getId());
        Assertions.assertThat(result.get().getChildName()).isEqualTo("Alex");
    }

    @Test
    public void findOneProjectedBy_ReturnsEmptyOptional_WhenFilterByIdEquals0L() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id("0L")
                .build();
        Optional<MongoExampleProjection> result = mongoExampleRepository.findOneProjectedBy(
                MongoExampleProjection.class,
                specification);
        Assertions.assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findOneProjectedBy_ReturnsEx2_WhenFilterByIdEquals2L() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .id(ex2.getId())
                .build();
        Optional<MongoExampleProjection> result = mongoExampleRepository.findOneProjectedBy(
                MongoExampleProjection.class,
                specification);
        Assertions.assertThat(result.get().getId()).isEqualTo(ex2.getId());
        Assertions.assertThat(result.get().getName()).isEqualTo("Ex2");
        Assertions.assertThat(result.get().getChildId()).isNull();
        Assertions.assertThat(result.get().getChildName()).isNull();
    }

    @Test
    public void findAllProjectedBy_ReturnsFirstTwoExamples_WhenFilterByIdIn() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .idIn(List.of(ex1.getId(), ex2.getId()))
                .build();
        List<MongoExampleProjection> result = mongoExampleRepository.findAllProjectedBy(
                MongoExampleProjection.class,
                specification);
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(ex1.getId());
        Assertions.assertThat(result.get(0).getName()).isEqualTo("Ex1");
        Assertions.assertThat(result.get(0).getChildId()).isEqualTo(ex1.getChild().getId());
        Assertions.assertThat(result.get(0).getChildName()).isEqualTo("Alex");
        Assertions.assertThat(result.get(1).getId()).isEqualTo(ex2.getId());
        Assertions.assertThat(result.get(1).getName()).isEqualTo("Ex2");
        Assertions.assertThat(result.get(1).getChildId()).isNull();
        Assertions.assertThat(result.get(1).getChildName()).isNull();
    }

    @Test
    public void findAllProjectedBy_ReturnsPageAllExamples_WhenSortedByIdDescAndChildIdDesc() {
        MongoExampleSpecification specification = MongoExampleSpecification
                .builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("child.id").descending().and(Sort.by("id").descending()));
        Page<MongoExampleProjection> page = mongoExampleRepository.findAllProjectedBy(MongoExampleProjection.class,
                specification,
                pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(3);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(ex1.getId());
        Assertions.assertThat(page.getContent().get(1).getId()).isEqualTo(ex3.getId());
        Assertions.assertThat(page.getContent().get(2).getId()).isEqualTo(ex2.getId());
    }

}
