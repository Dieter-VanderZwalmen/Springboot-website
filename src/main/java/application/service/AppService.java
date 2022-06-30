package application.service;

import application.model.Coin;
import application.model.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.CollationElementIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class AppService {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private CoinService coinService;

    public Collection addCoinToCollection(Long coin_id, Collection collection) {

        Coin coin = coinService.getById(coin_id);
        if (coin.getCollection() != null) throw new ServiceException("add", "coin already belongs to a collection");

        if (!coin.getLand().equals(collection.getLand()) || coin.getJaartal() > collection.getJaartal())
            throw new ServiceException("add", "coin has a different country or is more recent than the collection's year");
        for (Coin c : collection.getCoins()) {
            if (Objects.equals(c.getJaartal(), coin.getJaartal()) && Objects.equals(c.getValue(), coin.getValue()))
                throw new ServiceException("add", "coin with same year and value already exists in this collection");
        }
        collection.addCoin(coin);
        coin.setCollection(collection);
        coin.setCollectionName();
        coinService.updateCollection(coin);
        collectionService.updateCoins(collection);
        return collection;
    }

    public Collection removeCoinFromCollection(Long coin_id, Long collection_id) {
        Coin coin = coinService.getById(coin_id);
        Collection collection = collectionService.findById(collection_id);

        List<Coin> coins = collection.getCoins();

        if (!coins.contains(coin)) throw new ServiceException("remove", "The collection does not contain this coin");
        coin.setCollection(null);
        coin.setCollectionName();
        collection.deleteCoin(coin);
        coinService.update(coin);
        collectionService.update(collection);
        return collection;
    }

    public Collection removeAllCoinsFromCollection(Long collection_id) {

        Collection collection = collectionService.findById(collection_id);

        List<Coin> coins = collection.getCoins();

        for (Coin coin : coins
        ) {
            coin.setCollection(null);
            coin.setCollectionName();
            coinService.update(coin);

        }
        collection.deleteAllCoins();
        collectionService.update(collection);


        return collection;


    }



    /*
    public List<Coin> getAllCoinsOfCollection (Long collection_id) {
        Collection collection = collectionService.findById(collection_id).get();
        List<Coin> coins = collection.getCoins();
        return coins;
    }

    public Coin removeCoinFromCollection (Coin coin, Long collection_id) {
        Collection collection = collectionService.findById(collection_id).get();
        collection.deleteCoin(coin);
        collectionService.update(collection);
        return coin;
    }*/

}
