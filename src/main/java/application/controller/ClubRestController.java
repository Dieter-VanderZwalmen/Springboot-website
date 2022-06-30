package application.controller;


import application.model.Club;
import application.model.Collector;
import application.service.*;
import net.bytebuddy.implementation.bytecode.Throw;
import org.aspectj.weaver.Iterators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;

@RestController
@RequestMapping("/api/club")
public class ClubRestController {

    @Autowired
    private ClubService clubService;

    @Autowired//nodig voor een collector toe te voegen
    private CollectorService collectorService;


    //http://localhost:8080/api/club/all test via postman
    @GetMapping("/overview")
    public Iterable<Club> getAll(){return clubService.findAll();}

    @GetMapping("/sortByName")
    public Iterable<Club> getAllSortedByName() {
        return clubService.findAllSortedByName();
    }

    //http://localhost:8080/api/club/search/maxNumber/8
    @GetMapping("/search/maxNumber/{maxaantal}")
    public Iterable<Club> getByMaxAantal(@PathVariable("maxaantal") String aantal) {

        return clubService.getAllWithMaxLeden(aantal);
    }

    //http://localhost:8080/api/club/delete/4
    @DeleteMapping("/delete/{id}")
    public Iterable<Club> delete(@PathVariable("id")long id){

        return clubService.deleteById(id);
    }

    //http://localhost:8080/api/club/2/addCollector/1
    @PostMapping("/{club_id}/addCollector/{collector_id}")
    public Collector addCollector(@PathVariable("club_id") long clubID,@PathVariable("collector_id") long collectorID) {
        //System.out.println("In de methode" + clubID + collectorID);
        return clubService.addCollectorToClub(clubID, collectorID);
    }

    //http://localhost:8080/api/club/2/removeCollector/1
    @DeleteMapping("/{club_id}/removeCollector/{collector_id}")
    public Iterable<Collector> deleteCollectorFromClub(@PathVariable("club_id") long clubID, @PathVariable("collector_id") long collectorID) {
        return clubService.deleteCollectorFromClub(clubID, collectorID);
    }


    @GetMapping("/getCollectors")
    public Iterable<Collector> getCollectorsOfClub(@RequestParam("clubId") Long club_id) {
        System.out.println("in methode");
        Club club = clubService.findById(club_id);
        return clubService.getAllCollectorsFromClub(club);
    }


        //denk dat dit een foute manier is
    /*
    @PostMapping("/{club_id}/addCollector/{collector_id}")
    public void add(@PathVariable("club_id") long clubID,@PathVariable("collector_id") long collectorID){
        //deze code is echt lelijk maar hopelijk wel duidelijk

        //club
        Club club = clubService.findById(clubID);
        String clubRegio = club.getRegio();

        //collector
        Collector collector =  collectorService.findById(collectorID);
        String collectorRegio = collector.getRegio();

        //regio van collector moet het zelfde zijn van de club
        if(!clubRegio.equals(collectorRegio)){
            throw new ServiceException("message", "Region difference");
        }
        //max aantal leden moet gerespecteerd worden
        int clubMax = club.getMaxLeden();
        int aantalLeden = club.getAantalLeden();
        if(aantalLeden < clubMax){
            throw new ServiceException("message", "Club is full");
        }

        clubService.addCollector(collector,club);
        collectorService.addToClub(collector,club);

    }

     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public Map<String, String> handleInvalidInputExceptions(Exception ex) {
        System.out.println("method not allowed");
        Map<String, String> errors = new HashMap<>();
        errors.put("error","foute link: "+ ex);
        return errors;
//        Map<String, String> errors = new HashMap<>();
//        ServiceException se = (ServiceException) ex;
//        errors.put(se.getAction(), se.getMessage());
//        return errors;
    }
//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//    @ExceptionHandler(ServiceException.class)
//    public Map<String, String> handleFouteLink(Exception ex) {
//        System.out.println("method not allowed");
//        Map<String, String> errors = new HashMap<>();
//        errors.put("error","foute link: "+ ex);
//        return errors;
//    }
//    @ResponseStatus(HttpStatusIN)
//    @ExceptionHandler(ServiceException.class)
//    public Map<String, String> handleFouteLink(Exception ex) {
//        System.out.println("method not allowed");
//        Map<String, String> errors = new HashMap<>();
//        errors.put("error","foute link: "+ ex);
//        return errors;
//    }



}
