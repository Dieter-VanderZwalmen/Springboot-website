package application.controller;

import application.CoinBuilder;
import application.model.Coin;
import application.service.CoinService;
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
@WebMvcTest(CoinRestController.class)
public class CoinRestControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    CoinService service;

    @Autowired
    MockMvc coinRestController;

    Coin validCoin, noNameCoin, negativeCoin;

    @Before
    public void setUp() {
        validCoin = CoinBuilder.aValidCoin().build();
        noNameCoin = CoinBuilder.anCoinWithEmptyName().build();
        negativeCoin = CoinBuilder.aCoinWithNegativeValue().build();
    }

    @Test
    public void givenCoin_whenGetRequestToAllCoins_thenJSONWithAllCoinsReturned() throws Exception {
        List<Coin> coins = Arrays.asList(validCoin);

        //mocking
        given(service.findAll()).willReturn(coins);

        //when
        coinRestController.perform(get("/api/coin/overview")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(validCoin.getName())));
    }

    @Test
    public void givenNoCoins_WhenPostRequestToAddAValidPatient_thenJSONisReturned() throws Exception {
        //given
        List<Coin> coins = Arrays.asList(validCoin);

        //mocking
        when(service.addCoin(validCoin)).thenReturn(validCoin);
        when(service.findAll()).thenReturn(coins);

        //when
        coinRestController.perform(post("/api/coin/add")
                .content(mapper.writeValueAsString(validCoin))
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(validCoin.getName())));
    }

    @Test
    public void givenNoCoin_whenPostRequestToAddAnInvalidCoin_thenErrorInJSONformatIsReturned() throws Exception {
        // given

        // when
        coinRestController.perform(post("/api/coin/add")
                        .content(mapper.writeValueAsString(noNameCoin))
                        .contentType(MediaType.APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", Is.is("munt.name.missing")));
    }
}
