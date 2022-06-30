package application.service;

import application.model.Coin;
import application.model.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CoinService coinService;

    public Collection add(Collection collection) throws ServiceException{
/*
        for (MuntCollectie muntCollectie1: muntCollectieRepository.findAll()){
            if (muntCollectie.getJaartal() != null){
                if (muntCollectie.getJaartal().equals(muntCollectie1.getJaartal())){
                    throw new ServiceException(muntCollectie.getJaartal(), "jaartal.land.niet.uniek");
                }
            }else if (muntCollectie.getTitel()!= null){
                if (muntCollectie.getTitel().equals(muntCollectie1.getTitel())){
                    throw new ServiceException(muntCollectie.getTitel(), "titel.niet.uniek");
                }
            }
        }*/
        return collectionRepository.save(collection);
    }

   /* public Collection addCoin (Coin coin, Long collection_id) {
        Collection collection = collectionRepository.findById(collection_id).orElseThrow(()->new ServiceException("add", "Coin Collection not found"));
        collection.addCoin(coin);
        collectionRepository.save(collection);
        return collection;
    }*/

    public Collection findById(long id) {
        return collectionRepository.findById(id).orElseThrow(()->new ServiceException("get", "Collection does not exist"));
    }



    public Page<Collection> findMuntCollectiesByJaartal(int jaartal, Pageable pageable) {
        return collectionRepository.findCollectionsByJaartal(jaartal, pageable);

    }

    public Iterable<Collection> findMuntCollectiesByJaartal(int jaartal) {
        Iterable<Collection> collections = collectionRepository.findMuntCollectiesByJaartal(jaartal);

        if (StreamSupport.stream(collections.spliterator(), false).count() == 0){
            throw new ServiceException("message", "No collection from this year");
        }

        return collectionRepository.findMuntCollectiesByJaartal(jaartal);

    }

    public Page<Collection> findMuntCollectiesByLand(String land, Pageable pageable) {
        return collectionRepository.findCollectionsByLand(land, pageable);
    }
    public Iterable<Collection> findMuntCollectiesSortedByLand(String land) {
        return collectionRepository.findCollectionsSortedByLand(land);
    }

    public Page<Collection> findMuntCollectiesByJaartalBefore(int jaartal, Pageable pageable) {
        return collectionRepository.findCollectionsByJaartalBefore(jaartal, pageable);
    }


    public List<Collection> findAll(){
        return  collectionRepository.findAll();
    }
    public Page<Collection> findAll(Pageable pageable){
        return  collectionRepository.findAll(pageable);
    }

    public void deleteWithId(long id){
        Collection c = collectionRepository.getById(id);
       /* List<Coin> coins = coinService.getAllByCollection(c);
        for (Coin coin: coins
             ) {
            coin.setCollection(null);
            coinService.update(coin);
        }*/
        collectionRepository.deleteById(id);
    }
    public void update(Collection collection) throws ServiceException {
        List<Coin> coinsInCollection = collection.getCoins();
        for (Coin c: coinsInCollection
        ) {
            if(c.getJaartal() > collection.getJaartal() || !c.getLand().equals(collection.getLand())) throw new ServiceException("update", "the collection contains coins with different year or country");
        }
        collectionRepository.save(collection);
    }
    public void updateCoins(Collection collection) throws ServiceException {
        collectionRepository.save(collection);
    }


    public Iterable<Collection> findMuntCollectiesSortedByLand() {
        return collectionRepository.findAllByOrderByLand();
    }
}
