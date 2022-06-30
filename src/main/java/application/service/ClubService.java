package application.service;

import application.model.Club;
import application.model.Collector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private CollectorRepository collectorRepository;



    public Club add(Club club) throws ServiceException {
        return clubRepository.save(club);
    }

    public Club findById(long id) {
        return clubRepository.findById(id).orElseThrow(()-> new ServiceException("get", "Club not found"));
    }


    public Iterable<Club> deleteById(long id) {
       try {
           List<Club> clubs = clubRepository.findAll();
           clubRepository.deleteById(id);
           return findAll();
       } catch (Exception e) {
           throw new ServiceException("error", "Id does not exist");
       }
    }

    public Iterable<Club> findAll() {
        return clubRepository.findAll();
    }

    public Page<Club> findAll(Pageable pageable) {
        return clubRepository.findAll(pageable);
    }


    public Iterable<Club> getAllSortedByName() {
        return clubRepository.findAll(Sort.by("name"));
    }

    public Iterable<Club> getAllSortedByMaxLeden() {
        return clubRepository.findAll(Sort.by("maxLeden"));
    }

    public Page<Club> findAllSortedByMaxLedenPage(Pageable pageable) {
        return clubRepository.findAllSortedByMaxLedenPage(pageable);
    }

    public Page<Club> findByName(String name, Pageable pageable) {
        return clubRepository.findByName(name, pageable);
    }
    public Page<Club> findByNameContaining(String name, Pageable pageable) {
        return clubRepository.findByNameContaining(name, pageable);
    }
    public Page<Club> findAllSortedByNamePage(Pageable pageable) {
        return clubRepository.findAllSortedNamePage(pageable);
    }

    public Iterable<Club> findAllSortedByName() {
        return clubRepository.findAllSortedName();
    }
    public Page<Club> getAllWithMaxLeden(int amount, Pageable pageable) {
        if(!clubRepository.getAllByMaxLedenEquals(amount).iterator().hasNext()) throw new ServiceException("error", "No clubs found");
        return clubRepository.getAllByMaxLedenEquals(amount, pageable);
    }
    public Iterable<Club> getAllWithMaxLeden(String amount) {
        int amountNew;
        try {
           amountNew = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            throw new ServiceException("message", "Has to be a number");
        }
        return clubRepository.getAllByMaxLedenEquals(amountNew);
    }

    public Page<Club> getAllByMaxLedenAfter(int amount, Pageable pageable) {
        return clubRepository.getAllByMaxLedenAfter(amount-1, pageable);
    }

    public void addCollector(Collector collector,Club declub) {
        declub.addCollector(collector);
    }

    public Collector addCollectorToClub(long clubID, long collectorID) {
        Club club = clubRepository.findById(clubID).orElseThrow(()->new ServiceException("error", "Club not found"));
        Collector collector = collectorRepository.findById(collectorID).orElseThrow(()->new ServiceException("get", "Collector not found"));

        // kijken of collector al toegevoegd is aan club
        if (collector.getClubsNames().contains(club.getName())){
            throw new ServiceException("message", "Collector is already added to club");
        }

        //regio van collector moet het zelfde zijn van de club
        if(!club.getRegio().equals(collector.getRegio())){
            throw new ServiceException("message", "Region difference");
        }

        //max aantal leden moet gerespecteerd worden
        int clubMax = club.getMaxLeden();
        int aantalLeden = club.getAantalLeden();
        if(aantalLeden > clubMax){
            throw new ServiceException("message", "Club is full");
        }

        collector.addClub(club);
        collectorRepository.save(collector);
        return collector;
    }

    public Iterable<Collector> deleteCollectorFromClub(long clubID, long collectorID) {
        Club club = clubRepository.findById(clubID).orElseThrow(()->new ServiceException("error", "Club not found"));
        Collector collector = collectorRepository.findById(collectorID).orElseThrow(()->new ServiceException("get", "Collector not found"));

        collector.deleteClub(club);
        collectorRepository.save(collector);
        return getAllCollectorsFromClub(club);

    }

    public Iterable<Collector> getAllCollectorsFromClub(Club club) {
        List<Collector> collectors = new ArrayList<Collector>();
        for (Collector collector: collectorRepository.findAll()){
            for (String clubName :collector.getClubsNames()){
                if (clubName.equals(club.getName())){
                    collectors.add(collector);
                }
            }
        }
        return collectors;
    }
    public List<Collector> getAllCollectorsFromClubList(Club club) {
        List<Collector> collectors = new ArrayList<Collector>();
        for (Collector collector: collectorRepository.findAll()){
            for (String clubName :collector.getClubsNames()){
                if (clubName.equals(club.getName())){
                    collectors.add(collector);
                }
            }
        }
        return collectors;
    }
}
