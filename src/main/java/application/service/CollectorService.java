package application.service;

import application.model.Club;
import application.model.Collector;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectorService {

    //heb dit public gezet anders zou de test klasse er niet aankunnen
    //hopelijk zorgt dit niet voor problemen
    @Autowired
    public CollectorRepository collectorRepository;

    public Collector add(Collector collector) throws ServiceException{
        //mag niet dezelfde naam hebben
        try{

            return collectorRepository.save(collector);
        } catch (Exception e){
            throw new ServiceException("error", "could not save collector");
        }
        //return collectorRepository.save(collector);

    }
    public void addCollector(Collector collector) throws ServiceException{
        //mag niet dezelfde naam hebben
        try{
            collectorRepository.save(collector);
            //return collector;
        } catch (Exception e){
            throw new ServiceException("error", "could not save collector");
        }
       // collectorRepository.save(collector);

    }

    public Iterable<Collector> findAll(){
        return  collectorRepository.findAll();
    }

    public Collector findById(long id) {
        return collectorRepository.findById(id).orElseThrow(()->new ServiceException("get", "Collector not found"));
    }

    //update nog aanpassen
    public void update(Collector collector){
        System.out.println(collector.getId());
        collectorRepository.findById(collector.getId()).orElseThrow(() -> new DbException("verzamelaar.not.found"));





        collectorRepository.save(collector);
    }

    public void delete(long id){
        if(!collectorRepository.existsById(id)) throw new ServiceException("error", "Id not found");
        collectorRepository.deleteById(id);
    }

    public Iterable<Collector> getAllByRegio(String regio){
        return collectorRepository.findByRegio(regio);
    }
    public Iterable<Collector> getAllByRegioContainingString(String regio){
        return collectorRepository.findAllByRegioContaining(regio);
    }
    public Iterable<Collector> getAllByName(String name){
        return collectorRepository.findByName(name);
    }
    public Iterable<Collector> getAllByFirstname(String firstname){
        return collectorRepository.findByFirstname(firstname);
    }


    public Iterable<Collector> getAllByNameAndFirstname(String name, String firstname) {
        return  collectorRepository.findCollectorsByNameAndFirstname(name,firstname);
    }

    public void addToClub(Collector collector, Club club) {

        collector.addClub(club);
        collectorRepository.save(collector);

    }


}
