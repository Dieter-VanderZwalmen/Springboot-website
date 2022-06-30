package application.service;


import application.CoinBuilder;
import application.model.Coin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CoinRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CoinRepository coinRepository;


    @Test
    public void givenCoinExists_whenFindByJaartal_thenReturnCoin() {
        //given
        Coin coin = CoinBuilder.aValidCoin().build();
        entityManager.persistAndFlush(coin);

        //when
        Iterable<Coin> found = coinRepository.findAllByJaartal(coin.getJaartal());

        assertNotNull(found);
        assertThat(found.iterator().next().getName().equals(coin.getName()));

    }


}
