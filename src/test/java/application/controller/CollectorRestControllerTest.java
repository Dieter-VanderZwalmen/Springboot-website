package application.controller;

import application.CollectorBuilder;
import application.model.Collector;
import application.service.CollectorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringRunner.class)
// @WebMvcTest also auto-configures MockMvc
// which offers a powerful way of easy testing MVC controllers without starting a full HTTP server
@WebMvcTest(CollectorRestController.class)
public class CollectorRestControllerTest {
    // For creating JSONs from objects
    ObjectMapper mapper = new ObjectMapper();

    // We use @MockBean to create and inject a mock for the service
    @MockBean
    CollectorService service;

    // To not start the server at all but to test only the layer below that,
    // where Spring handles the incoming HTTP request and hands it off to your controller.
    // That way, almost of the full stack is used, and your code will be called in exactly the
    // same way as if it were processing a real HTTP request but without the cost of starting the server.
    // To do that, use Spring’s MockMvc and ask for that to be injected for
    // you by using the @WebMvcTest annotation on the test case.
    @Autowired
    MockMvc VerzamelaarRestController;

    Collector elke, pieter, johan;

    @Before
    public void setUp() {
        johan = CollectorBuilder.aCollectorJohan().build();
        pieter = CollectorBuilder.aCollectorPieter().build();
        elke = CollectorBuilder.anInvalidCollectorNoName().build();
    }

    //de testen

    @Test
    public void givenVerzamelaars_whenGetRequestToAllVerzamelaars_thenJSONwithAllVerzamelaarsReturned() throws Exception {
        // given
        List<Collector> collectors = Arrays.asList(johan, pieter);

        // mocking
        given(service.findAll()).willReturn(collectors);

        // when
        VerzamelaarRestController.perform(get("/api/verzamelaars/all")
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(johan.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(pieter.getName())));
    }

    @Test
    public void givenNoVerzamelaars_whenPostRequestToAddAValidVerzamelaar_thenJSONisReturned() throws Exception {
        // given
        List<Collector> collectors = Arrays.asList(johan);

        // mocking
        when(service.add(johan)).thenReturn(johan);
        when(service.findAll()).thenReturn(collectors);

        // when
        VerzamelaarRestController.perform(post("/api/verzamelaars/add")
                        .content(mapper.writeValueAsString(johan))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(johan.getName())));
    }

    @Test
    public void givenNoVerzamelaars_whenPostRequestToAddAnInvalidVerzamelaar_thenErrorInJSONformatIsReturned() throws Exception {
        // given

        // when
        VerzamelaarRestController.perform(post("/api/verzamelaars/add")
                        .content(mapper.writeValueAsString(elke))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("name.missing")));
    }


    //todo
    //er moeten 2-3 test gevallen getest worden
    //"als je er een paar kunt maken weten we wel dat je het kan" ~ Elke steegmans

}
