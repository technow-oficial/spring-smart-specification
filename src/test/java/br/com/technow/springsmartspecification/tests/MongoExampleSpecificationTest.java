package br.com.technow.springsmartspecification.tests;

import java.time.LocalDateTime;
import java.util.List;

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

import br.com.technow.springsmartspecification.creator.MongoExampleChildCreator;
import br.com.technow.springsmartspecification.creator.MongoExampleCreator;
import br.com.technow.springsmartspecification.domain.MongoExample;
import br.com.technow.springsmartspecification.domain.MongoExampleChild;
import br.com.technow.springsmartspecification.projection.MongoExampleProjection;
import br.com.technow.springsmartspecification.repository.MongoExampleChildRepository;
import br.com.technow.springsmartspecification.repository.MongoExampleRepository;
import br.com.technow.springsmartspecification.specification.MongoExampleSpecification;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("mongo")
public class MongoExampleSpecificationTest {

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
    public void id_ReturnEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .id(ex1.getId())
                .build();
        MongoExample actual = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void idCanBe_ReturnEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .idCanBe(ex1.getId())
                .build();
        MongoExample actual = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void wantThisId_ReturnEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .wantThisId(ex1.getId())
                .build();
        MongoExample actual = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void nameContains_ReturnPageWithEx2_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameContains("2")
                .build();
        Pageable pageable = Pageable.ofSize(5);
        Page<MongoExample> actual = mongoExampleRepository.findAll(specification,
                pageable);
        Assertions.assertThat(actual.getTotalElements())
                .isEqualTo(1);
        Assertions.assertThat(actual.getContent().get(0).getId()).isEqualTo(ex2.getId());
    }

    @Test
    public void nameContainsIgnoreCase_ReturnPageWithEx2_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameContainsIgnoreCase("X2")
                .build();
        Pageable pageable = Pageable.ofSize(5);
        Page<MongoExample> actual = mongoExampleRepository.findAll(specification,
                pageable);
        Assertions.assertThat(actual.getTotalElements())
                .isEqualTo(1);
        Assertions.assertThat(actual.getContent().get(0).getId()).isEqualTo(ex2.getId());
    }

    @Test
    public void nameEqualsIgnoreCase_ReturnPageWithEx2_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameEqualsIgnoreCase("eX2")
                .build();
        Pageable pageable = Pageable.ofSize(5);
        Page<MongoExample> actual = mongoExampleRepository.findAll(specification,
                pageable);
        Assertions.assertThat(actual.getTotalElements())
                .isEqualTo(1);
        Assertions.assertThat(actual.getContent().get(0).getId()).isEqualTo(ex2.getId());
    }

    @Test
    public void nameEqualsIgnoreCase_ReturnsEmptyPage_WhenFail() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameEqualsIgnoreCase("eX22")
                .build();
        Pageable pageable = Pageable.ofSize(5);
        Page<MongoExample> actual = mongoExampleRepository.findAll(specification,
                pageable);
        Assertions.assertThat(actual.getTotalElements())
                .isEqualTo(0);
    }

    @Test
    public void valueLessThan_ReturnsEx1_WhenSuccessful() {
        Long expectedValue = 1L;
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .valueLessThan(2L)
                .build();
        MongoExample actual = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getValue())
                .isEqualTo(expectedValue);
    }

    @Test
    public void timestampLessThan_ReturnsEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .timestampLessThan(LocalDateTime.of(2001, 1, 1, 0, 0))
                .build();
        MongoExample actual = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(actual.getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void idLessThanEqual_ReturnsFirstTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .valueLessThanEqual(2L)
                .build();
        List<MongoExample> list = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(list.size())
                .isEqualTo(2);
        Assertions.assertThat(list.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(list.get(1).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void timestampGreaterThan_ReturnsTwoLastExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .timestampGreaterThan(LocalDateTime.of(2001, 1, 1, 0, 0))
                .build();
        List<MongoExample> list = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(list.size())
                .isEqualTo(2);
        Assertions.assertThat(list.get(0).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(list.get(1).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void idGreaterThanEqual_ReturnsTwoLastExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .valueGreaterThanEqual(2L)
                .build();
        List<MongoExample> list = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(list.size())
                .isEqualTo(2);
        Assertions.assertThat(list.get(0).getValue())
                .isEqualTo(2L);
        Assertions.assertThat(list.get(1).getValue())
                .isEqualTo(3L);
    }

    @Test
    public void activeIsNull_ReturnsEx3_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .activeIsNull(true)
                .build();
        MongoExample example = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(example.getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void activeIsNull_ReturnsFirstTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .activeIsNull(false)
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void activeIsNotNull_ReturnsFirstTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .activeIsNotNull(true)
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void activeIsNotNull_ReturnsEx3_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .activeIsNotNull(false)
                .build();
        MongoExample example = mongoExampleRepository.findOne(specification).get();
        Assertions.assertThat(example.getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void nameStartsWith_ReturnsAllExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameStartsWith("Ex")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(3);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(exampleList.get(2).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void nameStartsWithIgnoreCase_ReturnsAllExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameStartsWithIgnoreCase("eX")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(3);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(exampleList.get(2).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void nameStartsWithIgnoreCase_ReturnsAllExamples_WhenFail() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameStartsWithIgnoreCase("X")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(0);
    }

    @Test
    public void nameEndsWith_ReturnsEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameEndsWith("1")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(1);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void nameEndsWith_ReturnsEx1_WhenFail() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameEndsWith("x")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(0);
    }

    @Test
    public void nameNotEquals_ReturnsLastTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameNotEqualsIgnoreCase("Ex1")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void nameNotEqualsIgnoreCase_ReturnsFirstTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameNotEqualsIgnoreCase("eX3")
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void activeIsTrue_ReturnsEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .activeIsTrue(true)
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(1);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void activeIsFalse_ReturnsEx2_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .activeIsFalse(true)
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(1);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void idIn_ReturnsFirstTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .idIn(List.of(ex1.getId(), ex2.getId()))
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void idNotIn_ReturnsFirstTwoExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .idNotIn(List.of(ex3.getId()))
                .build();
        List<MongoExample> exampleList = mongoExampleRepository.findAll(specification);
        Assertions.assertThat(exampleList.size())
                .isEqualTo(2);
        Assertions.assertThat(exampleList.get(0).getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(exampleList.get(1).getId())
                .isEqualTo(ex2.getId());
    }

    @Test
    public void id_ReturnsEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .id(ex1.getId())
                .build();
        MongoExampleProjection projection = mongoExampleRepository
                .findOneProjectedBy(MongoExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(ex1.getChild().getId());
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childId_ReturnsEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .childId(ex1.getChild().getId())
                .build();
        MongoExampleProjection projection = mongoExampleRepository
                .findOneProjectedBy(MongoExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(ex1.getChild().getId());
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childName_ReturnsEx1_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .childName("Alex")
                .build();
        MongoExampleProjection projection = mongoExampleRepository
                .findOneProjectedBy(MongoExampleProjection.class, specification)
                .get();
        Assertions.assertThat(projection.getId())
                .isEqualTo(ex1.getId());
        Assertions.assertThat(projection.getName())
                .isEqualTo("Ex1");
        Assertions.assertThat(projection.getChildId())
                .isEqualTo(ex1.getChild().getId());
        Assertions.assertThat(projection.getChildName())
                .isEqualTo("Alex");
    }

    @Test
    public void childFullNameIsNull_ReturnsTwoLastExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .childFullNameIsNull(true)
                .build();
        List<MongoExampleProjection> projectionList = mongoExampleRepository
                .findAllProjectedBy(MongoExampleProjection.class, specification);
        Assertions.assertThat(projectionList.size())
                .isEqualTo(2);
        Assertions.assertThat(projectionList.get(0).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(projectionList.get(1).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void ownerFullNameIsNull_ReturnsAPageWithTwoLastExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .childFullNameIsNull(true)
                .build();
        Pageable pageable = Pageable.ofSize(10);
        Page<MongoExampleProjection> page = mongoExampleRepository
                .findAllProjectedBy(MongoExampleProjection.class, specification, pageable);
        Assertions.assertThat(page.getNumberOfElements())
                .isEqualTo(2);
        Assertions.assertThat(page.getContent().get(0).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(page.getContent().get(1).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void _ReturnsAPageAllExamples_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder().build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,
                "name"));
        Page<MongoExampleProjection> page = mongoExampleRepository
                .findAllProjectedBy(MongoExampleProjection.class, specification, pageable);
        Assertions.assertThat(page.getNumberOfElements())
                .isEqualTo(3);
        Assertions.assertThat(page.getContent().get(0).getId())
                .isEqualTo(ex3.getId());
        Assertions.assertThat(page.getContent().get(1).getId())
                .isEqualTo(ex2.getId());
        Assertions.assertThat(page.getContent().get(2).getId())
                .isEqualTo(ex1.getId());
    }

    @Test
    public void findAllProjectedBy_ReturnsLastPage_WhenPageIs1() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder().build();
        Pageable pageable = PageRequest.of(1, 2);
        Page<MongoExampleProjection> page = mongoExampleRepository
                .findAllProjectedBy(MongoExampleProjection.class, specification, pageable);
        Assertions.assertThat(page.getNumberOfElements())
                .isEqualTo(1);
        Assertions.assertThat(page.getContent().get(0).getId())
                .isEqualTo(ex3.getId());
    }

    @Test
    public void nameRegex_ReturnsEx2_WhenSuccessful() {
        MongoExampleSpecification specification = MongoExampleSpecification.builder()
                .nameRegex("2$")
                .build();
        MongoExample example = mongoExampleRepository
                .findOne(specification)
                .get();
        Assertions.assertThat(example.getId())
                .isEqualTo(ex2.getId());
    }

}
