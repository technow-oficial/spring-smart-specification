package io.github.technowoficial.springsmartspecification.tests;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import io.github.technowoficial.springsmartspecification.domain.JpaExample2;
import io.github.technowoficial.springsmartspecification.repository.JpaExample2Repository;
import io.github.technowoficial.springsmartspecification.specification.JpaExample2Specification;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JpaExample2RepositoryTest {

    @Autowired
    private JpaExample2Repository jpaExample2Repository;

    @Test
    public void name_ReturnsAnExample2_WhenSuccessful() {
        JpaExample2 ex = JpaExample2.builder()
                .name("Juka")
                .build();
        jpaExample2Repository.save(ex);
        JpaExample2Specification spec = JpaExample2Specification.builder()
                .name(ex.getName())
                .build();
        Optional<JpaExample2> result = jpaExample2Repository.findOne(spec);
        Assertions.assertThat(result.isPresent()).isTrue();
        Assertions.assertThat(result.get().getId()).isEqualTo(ex.getId());
    }

}
