package application.controller;

import application.model.Collection;
import application.model.Collector;
import application.service.CollectorService;
import application.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/collector")
public class CollectorRestController {

    @Autowired
    private CollectorService collectorService;

    @GetMapping("/overview")
    public Iterable<Collector> getAll(){return collectorService.findAll();}

//    {
//    "id": 5,
//        "name": "naam2",
//            "firstname": "firstname2",
//            "regio" : "land2",
//            "age" : 22,
//              "clubs" : []
//
//    }
    @PostMapping("/add")
    public Map<String,String> add(@Valid @RequestBody Collector collector){
        System.out.println("collector die toegevoegd gaat worden" + collector.toString());
        collectorService.addCollector(collector);
        HashMap<String, String> map = new HashMap<>();
        map.put("succes", "collector saved");
        return map;
       // throw new ServiceException("succes", "collector saved");
        //dit is leleik maar het werkt wel
        //~Marlene
    }

    @PutMapping("/update/{id}")
    public Iterable<Collector> update(@Valid @RequestBody Collector collector, @PathVariable("id") long id) {
        //collector.getClubsNames() == leeg
        //throw new error
        System.out.println("in update van collector");
        List<String> lijst = collectorService.findById(id).getClubsNames();
        if (lijst != null ){
            throw new application.service.ServiceException("error", "Update niet toegestaan als je behoort tot een club");
        }


        Collector n = collectorService.findById(id);
        n.setAttributes(collector);
        try {
            collectorService.update(n);
        } catch (DataIntegrityViolationException e) {
            throw new application.service.ServiceException("error", "Land en jaartal moeten uniek zijn");
        }

        return collectorService.findAll();

    }



    @DeleteMapping("/delete/{id}")
    public Iterable<Collector> delete(@PathVariable("id") long id){
        List<String> lijst = collectorService.findById(id).getClubsNames();
        if (lijst != null ){
            throw new application.service.ServiceException("error", "Delete niet toegestaan als je behoort tot een club");
        }

        collectorService.delete(id);
        return collectorService.findAll();
    }

    /*
    @Transactional
    @PostMapping("/update/{id}")
    public Iterable<Collector> update(@Valid @RequestBody Collector collector){

        collectorService.update(collector);
        return collectorService.findAll();
    }*/
    @GetMapping("/search/region")
    public Iterable<Collector> getAllByRegio(@RequestParam("value") String region){
        return collectorService.getAllByRegioContainingString(region);
    }
    @GetMapping("/search/nameAndFirstname")
    public Iterable<Collector> getAllByName(@RequestParam("name") String name, @RequestParam("firstname") String firstname){
        System.out.println(name + firstname);
        return  collectorService.getAllByNameAndFirstname(name,firstname);
    }

    /* niet meer nodig aangezien we werken met 1 methode ipv 2 aparte
    @GetMapping("/allByName/{name}")
    public Iterable<Verzamelaar> getAllByName(@PathVariable("name") String name){ return  verzamenlaarService.getAllByName(name);}

    @GetMapping("/allByFirstname/{firstname}")
    public Iterable<Verzamelaar> getAllByFirstname(@PathVariable("firstname") String firstname){return verzamenlaarService.getAllByFirstname(firstname);}
    */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            ServiceException se = (ServiceException) ex;
            errors.put(se.getAction(), se.getMessage());
        }
        else {
            errors.put(((ResponseStatusException)ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }
}
