package application.service;

import application.model.Coin;
import application.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
        Iterable<Coin> findAllByJaartal(int jaartal);
        Iterable<Coin> findAllByLand(String land);
        List<Coin> findCoinByCollection(Collection collection);
        Coin findByName(String name);
}
