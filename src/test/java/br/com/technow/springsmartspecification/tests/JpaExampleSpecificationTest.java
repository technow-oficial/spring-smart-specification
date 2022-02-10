package br.com.technow.springsmartspecification.tests;

import java.time.LocalDateTime;
import java.util.List;

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

import br.com.technow.springsmartspecification.creator.JpaExampleChildCreator;
import br.com.technow.springsmartspecification.creator.JpaExampleCreator;
import br.com.technow.springsmartspecification.domain.JpaExample;
import br.com.technow.springsmartspecification.domain.JpaExampleChild;
import br.com.technow.springsmartspecification.projection.JpaExampleProjection;
import br.com.technow.springsmartspecification.repository.JpaExampleChildRepository;
import br.com.technow.springsmartspecification.repository.JpaExampleRepository;
import br.com.technow.springsmartspecification.specification.JpaExampleSpecification;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JpaExampleSpecificationTest {

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
    public void id_ReturnEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .id(1L)
                .build();
        JpaExample actual = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(1L);
    }

    @Test
    public void idCanBe_ReturnEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .idCanBe(1L)
                .build();
        JpaExample actual = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(1L);
    }

    @Test
    public void wantThisId_ReturnEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .wantThisId(1L)
                .build();
        JpaExample actual = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(1L);
    }

    @Test
    public void nameContains_ReturnPageWithEx2_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameContains("2")
                .build();
        Pageable pageable = Pageable.ofSize(5);
        Page<JpaExample> actual = jpaExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(actual.getTotalElements())
                .isEqualTo(1);
        Assertions.assertThat(actual.getContent().get(0).getId()).isEqualTo(2L);
    }

    @Test
    public void nameContainsIgnoreCase_ReturnPageWithEx2_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameContainsIgnoreCase("e%2")
                .build();
        Pageable pageable = Pageable.ofSize(5);
        Page<JpaExample> actual = jpaExampleRepository.findAll(specification, pageable);
        Assertions.assertThat(actual.getTotalElements())
                .isEqualTo(1);
        Assertions.assertThat(actual.getContent().get(0).getId()).isEqualTo(2L);
    }

    @Test
    public void idLessThan_ReturnsEx1_WhenSuccessful() {
        Long expectedId = 1L;
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .idLessThan(2L)
                .build();
        JpaExample actual = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(expectedId);
    }

    @Test
    public void timestampLessThan_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .timestampLessThan(LocalDateTime.of(2001, 1, 1, 0, 0))
                .build();
        JpaExample actual = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(1L);
    }

    @Test
    public void idLessThanEqual_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .idLessThanEqual(2L)
                .build();
        List<JpaExample> list = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(list.size())
                .isEqualTo(2);
        Assertions.assertThat(list.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(list.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void timestampGreaterThan_ReturnsTwoLastExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .timestampGreaterThan(LocalDateTime.of(2001, 1, 1, 0, 0))
                .build();
        List<JpaExample> list = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(list.size())
                .isEqualTo(2);
        Assertions.assertThat(list.get(0).getId())
                .isEqualTo(2L);
        Assertions.assertThat(list.get(1).getId())
                .isEqualTo(3L);
    }

    @Test
    public void idGreaterThanEqual_ReturnsTwoLastExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .idGreaterThanEqual(2L)
                .build();
        List<JpaExample> list = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(list.size())
                .isEqualTo(2);
        Assertions.assertThat(list.get(0).getId())
                .isEqualTo(2L);
        Assertions.assertThat(list.get(1).getId())
                .isEqualTo(3L);
    }

    @Test
    public void activeIsNull_ReturnsEx3_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .activeIsNull(true)
                .build();
        JpaExample example = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(example.getId())
                .isEqualTo(3);
    }

    @Test
    public void activeIsNull_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .activeIsNull(false)
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void activeIsNotNull_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .activeIsNotNull(true)
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void activeIsNotNull_ReturnsEx3_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .activeIsNotNull(false)
                .build();
        JpaExample example = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(example.getId())
                .isEqualTo(3);
    }

    @Test
    public void nameLike_ReturnsEx2_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameLike("%2")
                .build();
        JpaExample example = jpaExampleRepository.findOne(specification).get();
        Assertions.assertThat(example.getId())
                .isEqualTo(2);
    }

    @Test
    public void nameNotLike_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameNotLike("%3")
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void nameStartsWithIgnoreCase_ReturnsAllExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameStartsWithIgnoreCase("eX")
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(3);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
        Assertions.assertThat(exampleList.get(2).getId())
                .isEqualTo(3L);
    }

    @Test
    public void nameEndsWith_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameEndsWith("1")
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(1);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
    }

    @Test
    public void nameNotEqualsIgnoreCase_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameNotEqualsIgnoreCase("eX3")
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void activeIsTrue_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .activeIsTrue(true)
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(1);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
    }

    @Test
    public void activeIsFalse_ReturnsEx2_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .activeIsFalse(true)
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(1);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(2L);
    }

    @Test
    public void idIn_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .idIn(List.of(1L, 2L))
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void idNotIn_ReturnsFirstTwoExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .idNotIn(List.of(3L))
                .build();
        List<JpaExample> exampleList = jpaExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(1L);
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(2L);
    }

    @Test
    public void id_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .id(1L)
                .build();
        JpaExampleProjection projection = jpaExampleRepository
                .findOneProjectedBy(JpaExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childId_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .childId(1L)
                .build();
        JpaExampleProjection projection = jpaExampleRepository
                .findOneProjectedBy(JpaExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childFullName_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .childFullName("Alex")
                .build();
        JpaExampleProjection projection = jpaExampleRepository
                .findOneProjectedBy(JpaExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childName_ReturnsEx1_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .childName("Alex")
                .build();
        JpaExampleProjection projection = jpaExampleRepository
                .findOneProjectedBy(JpaExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(1L);
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void nameStartsWith_ReturnsAllExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .nameStartsWith("Ex")
                .build();
        List<JpaExampleProjection> projectionList = jpaExampleRepository
                .findAllProjectedBy(JpaExampleProjection.class, specification);
        Assertions.assertThat(projectionList.size())
                .isEqualTo(3);
        JpaExampleProjection firstProjection = projectionList.get(0);
        Assertions.assertThat(firstProjection.getId())
                .isEqualTo(1L);
        Assertions.assertThat(firstProjection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(firstProjection.getChildId())
                .isEqualTo(1L);
        Assertions.assertThat(firstProjection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childFullNameIsNull_ReturnsTwoLastExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .childFullNameIsNull(true)
                .build();
        List<JpaExampleProjection> projectionList = jpaExampleRepository
                .findAllProjectedBy(JpaExampleProjection.class, specification);
        Assertions.assertThat(projectionList.size())
                .isEqualTo(2);
        Assertions.assertThat(projectionList.get(0).getId())
                .isEqualTo(2L);
        Assertions.assertThat(projectionList.get(1).getId())
                .isEqualTo(3L);
    }

    @Test
    public void ownerFullNameIsNull_ReturnsAPageWithTwoLastExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder()
                .childFullNameIsNull(true)
                .build();
        Pageable pageable = Pageable.ofSize(10);
        Page<JpaExampleProjection> page = jpaExampleRepository
                .findAllProjectedBy(JpaExampleProjection.class, specification, pageable);
        Assertions.assertThat(page.getNumberOfElements())
                .isEqualTo(2);
        Assertions.assertThat(page.getContent().get(0).getId())
                .isEqualTo(2L);
        Assertions.assertThat(page.getContent().get(1).getId())
                .isEqualTo(3L);
    }

    @Test
    public void _ReturnsAPageAllExamples_WhenSuccessful() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder().build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"));
        Page<JpaExampleProjection> page = jpaExampleRepository
                .findAllProjectedBy(JpaExampleProjection.class, specification, pageable);
        Assertions.assertThat(page.getNumberOfElements())
                .isEqualTo(3);
        Assertions.assertThat(page.getContent().get(0).getId())
                .isEqualTo(3L);
        Assertions.assertThat(page.getContent().get(1).getId())
                .isEqualTo(2L);
        Assertions.assertThat(page.getContent().get(2).getId())
                .isEqualTo(1L);
    }

    @Test
    public void findAllProjectedBy_ReturnsLastPage_WhenPageIs1() {
        JpaExampleSpecification specification = JpaExampleSpecification.builder().build();
        Pageable pageable = PageRequest.of(1, 2);
        Page<JpaExampleProjection> page = jpaExampleRepository
                .findAllProjectedBy(JpaExampleProjection.class, specification, pageable);
        Assertions.assertThat(page.getNumberOfElements())
                .isEqualTo(1);
        Assertions.assertThat(page.getContent().get(0).getId())
                .isEqualTo(3L);
    }

}
