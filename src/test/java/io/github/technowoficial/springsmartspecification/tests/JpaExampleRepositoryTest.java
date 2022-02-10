package io.github.technowoficial.springsmartspecification.tests;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import io.github.technowoficial.springsmartspecification.creator.JpaExampleChildCreator;
import io.github.technowoficial.springsmartspecification.creator.JpaExampleCreator;
import io.github.technowoficial.springsmartspecification.domain.JpaExample;
import io.github.technowoficial.springsmartspecification.domain.JpaExampleChild;
import io.github.technowoficial.springsmartspecification.projection.JpaExampleProjection;
import io.github.technowoficial.springsmartspecification.repository.JpaExampleChildRepository;
import io.github.technowoficial.springsmartspecification.repository.JpaExampleRepository;
import io.github.technowoficial.springsmartspecification.specification.JpaExampleSpecification;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JpaExampleRepositoryTest {

    @Autowired
    private JpaExampleRepository jpaExampleRepository;

    @Autowired
    private JpaExampleChildRepository jpaExampleChildRepository;

    @BeforeEach
    public void setUp() {
        JpaExampleChild alexChild = JpaExampleChildCreator.createAlex();
        jpaExampleChildRepository.save(alexChild);

        JpaExample ex1 = JpaExampleCreator.createEx1();
        ex1.setChild(alexChild);

        jpaExampleRepository.save(ex1);
        jpaExampleRepository.save(JpaExampleCreator.createEx2());
        jpaExampleRepository.save(JpaExampleCreator.createEx3());
    }

    @Test
    public void count_Returns3_WhenSuccess() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder().build();
        Assertions.assertThat(jpaExampleRepository.count(specification)).isEqualTo(3);
    }

    @Test
    public void count_Returns1_WhenFilterByidEquals1L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(1L)
                .build();
        Assertions.assertThat(jpaExampleRepository.count(specification)).isEqualTo(1);
    }

    @Test
    public void exists_ReturnsTrue_WhenFilterByIdEquals1L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(1L)
                .build();
        Assertions.assertThat(jpaExampleRepository.exists(specification)).isTrue();
    }

    @Test
    public void exists_ReturnsFalse_WhenFilterByIdEquals0L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(0L)
                .build();
        Assertions.assertThat(jpaExampleRepository.exists(specification)).isFalse();
    }

    @Test
    public void findOne_ReturnsEx1_WhenFilterByIdEquals1L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(1L)
                .build();
        Assertions.assertThat(jpaExampleRepository.findOne(specification).get().getId())
                .isEqualTo(1L);
    }

    @Test
    public void findOne_ReturnsEmptyOptional_WhenFilterByIdEquals0L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(0L)
                .build();
        Assertions.assertThat(jpaExampleRepository.findOne(specification).isEmpty())
                .isTrue();
    }

    @Test
    public void findAll_ReturnsFirstTwoExamples_WhenFilterByIdIn() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .idIn(List.of(1L, 2L))
                .build();
        List<JpaExample> result = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void findAll_ReturnsFirstTwoExamples_WhenNoFilters() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .build();
        List<JpaExample> result = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(2).getId()).isEqualTo(3L);
    }

    @Test
    public void findAll_ReturnsPageWithFirstTwoExamples_WhenFilterByIdIn() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .idIn(List.of(1L, 2L))
                .build();
        Pageable pageable = Pageable.ofSize(10);
        Page<JpaExample> page = jpaExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(page.getContent().get(1).getId()).isEqualTo(2L);
    }

    @Test
    public void findAll_ReturnsPageWithEx3_WhenPageIs1() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .build();
        Pageable pageable = PageRequest.of(1, 2);
        Page<JpaExample> page = jpaExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(1);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(3L);
    }

    @Test
    public void findAll_ReturnsPageAllExamples_WhenSortedByIdDescAndChildIdDesc() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending().and(Sort.by("child.id").descending()));
        Page<JpaExample> page = jpaExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(3);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(page.getContent().get(1).getId()).isEqualTo(3L);
        Assertions.assertThat(page.getContent().get(2).getId()).isEqualTo(2L);
    }

    @Test
    public void findOneProjectedBy_ReturnsEx1Projection_WhenFilterByIdEquals1L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(1L)
                .build();
        Optional<JpaExampleProjection> result = jpaExampleRepository.findOneProjectedBy(
                JpaExampleProjection.class,
                specification);
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getId()).isEqualTo(1L);
        Assertions.assertThat(result.get().getName()).isEqualTo("Ex1");
        Assertions.assertThat(result.get().getChildId()).isEqualTo(1L);
        Assertions.assertThat(result.get().getChildName()).isEqualTo("Alex");
    }

    @Test
    public void findOneProjectedBy_ReturnsEmptyOptional_WhenFilterByIdEquals0L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(0L)
                .build();
        Optional<JpaExampleProjection> result = jpaExampleRepository.findOneProjectedBy(
                JpaExampleProjection.class,
                specification);
        Assertions.assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void findOneProjectedBy_ReturnsEx2_WhenFilterByIdEquals2L() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .id(2L)
                .build();
        Optional<JpaExampleProjection> result = jpaExampleRepository.findOneProjectedBy(
                JpaExampleProjection.class,
                specification);
        Assertions.assertThat(result.get().getId()).isEqualTo(2L);
        Assertions.assertThat(result.get().getName()).isEqualTo("Ex2");
        Assertions.assertThat(result.get().getChildId()).isNull();
        Assertions.assertThat(result.get().getChildName()).isNull();
    }

    @Test
    public void findAllProjectedBy_ReturnsFirstTwoExamples_WhenFilterByIdIn() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .idIn(List.of(1L, 2L))
                .build();
        List<JpaExampleProjection> result = jpaExampleRepository.findAllProjectedBy(
                JpaExampleProjection.class,
                specification);
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("Ex1");
        Assertions.assertThat(result.get(0).getChildId()).isEqualTo(1L);
        Assertions.assertThat(result.get(0).getChildName()).isEqualTo("Alex");
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2L);
        Assertions.assertThat(result.get(1).getName()).isEqualTo("Ex2");
        Assertions.assertThat(result.get(1).getChildId()).isNull();
        Assertions.assertThat(result.get(1).getChildName()).isNull();
    }

    @Test
    public void findAllProjectedBy_ReturnsPageAllExamples_WhenSortedByIdDescAndChildIdDesc() {
        JpaExampleSpecification specification = JpaExampleSpecification
                .builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending().and(Sort.by("child.id").descending()));
        Page<JpaExampleProjection> page = jpaExampleRepository.findAllProjectedBy(JpaExampleProjection.class,
                specification,
                pageable);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(page.getContent().size()).isEqualTo(3);
        Assertions.assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(page.getContent().get(1).getId()).isEqualTo(3L);
        Assertions.assertThat(page.getContent().get(2).getId()).isEqualTo(2L);
    }

}
