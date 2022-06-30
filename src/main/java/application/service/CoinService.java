package application.service;

import application.model.Coin;
import application.model.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CoinService {
    @Autowired
    private CoinRepository coinRepository;



    public Iterable<Coin> findAll() {
        return coinRepository.findAll();
    }

    public Coin getById(long id) {
        return coinRepository.findById(id).orElseThrow(()->new ServiceException("get", "coin does not exist"));
    }

    public Coin getByName(String name) {
        try {
            return coinRepository.findByName(name);
        } catch (Exception e) {
            throw new ServiceException("get", "Coin does not exist");
        }

    }

    public void add(Coin coin) throws ServiceException {
        if(coinRepository.findByName(coin.getName()) != null) throw new ServiceException("post", "coin already exists");
            coinRepository.save(coin);


    }
    public Coin addCoin(Coin coin) throws ServiceException {
        if(coinRepository.findByName(coin.getName()) != null) throw new ServiceException("post", "coin already exists");

            return coinRepository.save(coin);

    }

    public List<Coin> getAllByCollection (Collection collection) {
        return coinRepository.findCoinByCollection(collection);
    }

    public void update(Coin coin) throws ServiceException {
        if(coin.getCollection() != null) {
            Collection c = coin.getCollection();
            if(!coin.getLand().equals(c.getLand()) || coin.getJaartal() > c.getJaartal()) throw new ServiceException("update", "coin has a different country or is more recent than the collection's year");
            for (Coin co: c.getCoins()) {
                if(Objects.equals(co.getJaartal(), coin.getJaartal()) && Objects.equals(co.getValue(), coin.getValue())) throw new ServiceException("update", "coin with same year and value already exists in this collection");
            }
        }
        coinRepository.save(coin);
    }
    public void updateCollection(Coin coin) throws ServiceException {
        coinRepository.save(coin);
    }

    public void delete(long id) {
        Coin coin = coinRepository.getById(id);

        coinRepository.deleteById(id);
    }

    public Iterable<Coin> getAllByJaartal(int jaartal) {
         return coinRepository.findAllByJaartal(jaartal);
    }

    public Iterable<Coin> getAllByLand(String land) {
        return coinRepository.findAllByLand(land);
    }
}
