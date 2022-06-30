package application.service;
import application.CollectorBuilder;
import application.model.Collector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CollectorRepositoryTest {

    // TestEntityManager allows to use EntityManager in tests.
    // Spring Repository is an abstraction over EntityManager;
    // it shields developers from lower-level details of JPA
    // and brings many convenient methods. But Spring allows
    // to use EntityManager when needed in application code and tests.
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CollectorRepository collectorRepository;

    @Test
    public void givenVerzamelaarRegistered_whenFindByName_thenVerzamelaarIsReturned() {

        // given
        Collector johan = CollectorBuilder.aCollectorJohan().build();
        entityManager.persistAndFlush(johan);
        Collector pieter = CollectorBuilder.aCollectorPieter().build();
        entityManager.persistAndFlush(pieter);


        // when
        List<Collector> result = (List<Collector>) collectorRepository.findByName("Charlier");

        // then
        assertNotNull(result);

        assertThat(result.get(0).getName()).isEqualTo(johan.getName());
    }
    @Test
    public void givenVerzamelaarRegistered_whenFindByRegion_thenVerzamelaarIsReturned() {

        // given
        Collector johan = CollectorBuilder.aCollectorJohan().build();
        entityManager.persistAndFlush(johan);
        Collector pieter = CollectorBuilder.aCollectorPieter().build();
        entityManager.persistAndFlush(pieter);


        // when
        List<Collector> result = (List<Collector>) collectorRepository.findByRegio("Overijse");

        // then
        assertNotNull(result);
        assertThat(result.get(0).getRegio()).isEqualTo(johan.getRegio());
    }
    
    

}
