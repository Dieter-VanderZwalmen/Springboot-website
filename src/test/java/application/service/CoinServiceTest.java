package application.service;

import application.CoinBuilder;
import application.model.Coin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoinServiceTest {
    @Mock
    CoinRepository coinRepository;

    @InjectMocks
    CoinService coinService;

    @Test
    public void givenNoCoins_whenValidCoinAdded_ThenCoinIsAdded() {
        //given
        Coin coin = CoinBuilder.aValidCoin().build();

        //when(coinRepository.findByName(coin.getName())).thenReturn(null);
        when(coinRepository.save(any())).thenReturn(coin);

        //when
        Coin addedCoin = coinService.addCoin(coin);

        assertThat(coin.getName()).isSameAs(addedCoin.getName());
    }

    @Test
    public void givenCoinAlreadyExists_whenAddedCoinWithSameName_ThenCoinIsNotAddedAndErrorIsThrown(){
        Coin coin = CoinBuilder.aValidCoin().build();

        when(coinRepository.findByName(coin.getName())).thenReturn(coin);

        //when
        final Throwable raisedException = catchThrowable(() -> coinService.add(coin));

        //then
        assertThat(raisedException).isInstanceOf(ServiceException.class).hasMessageContaining("coin already exists");
    }

}
