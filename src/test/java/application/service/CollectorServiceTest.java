package application.service;
import application.CollectorBuilder;
import application.model.Collector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CollectorServiceTest {

    @Mock
    CollectorRepository collectorRepository;

    @InjectMocks
    CollectorService collectorService;


    //zoeken op naam zou voor problemen kunnen zorgen moesten er meerdere verzamelaars in zitte
    //dit is niet het geval dus hier geen probleem =>pas op met copy pasten
    @Test
    public void givenNoVerzamelaars_whenValidVerzamelaarAdded_ThenVerzamelaarIsAddedAndPatientIsReturned() {
        // given
        Collector johan = CollectorBuilder.aCollectorJohan().build();

        // mock all methods that are called in method that is tested here
        // VerzamelaarService addPatient
        // because otherwhise the VerzamelaarRepository of VerzamelaarService will be used
        when(collectorRepository.findById(johan.getId())).thenReturn(null);
        when(collectorRepository.save(any())).thenReturn(johan);

        // when
        Collector addedCollector = collectorService.add(johan);

        // then
        assertThat(johan.getName()).isSameAs(addedCollector.getName());
    }



}
