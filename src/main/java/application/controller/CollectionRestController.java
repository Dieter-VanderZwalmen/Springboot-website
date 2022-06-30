package application.controller;

import application.model.Club;
import application.model.Coin;
import application.model.Collection;
import application.service.AppService;
import application.service.CoinService;
import application.service.CollectionService;
import application.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
    @RequestMapping("/api/collection")
public class CollectionRestController {

    @Autowired
    private CollectionService collectionService;


    @Autowired
    private AppService appService;

    //http://localhost:8080/api/collecties/overview  test via postman  (GET)
    @GetMapping("/overview")
    public Iterable<Collection> getAll(){
        System.out.println("overview api");
        return  collectionService.findAll();
    }

    // http://localhost:8080/api/collecties/delete/1    (DELETE)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") String id){
        long lid;
        try {
            lid = Long.parseLong(id);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }
        Collection collection = collectionService.findById(lid);

        appService.removeAllCoinsFromCollection(collection.getId());


        collectionService.deleteWithId(lid);
    }

    @PostMapping("/add")
    public  Iterable<Collection> add(@Valid @RequestBody Collection collection){
        collectionService.add(collection);
        return collectionService.findAll();
    }

    @GetMapping("/sort/country")
    public Iterable<Collection> getAllSortedByCountry() {
        return collectionService.findMuntCollectiesSortedByLand();
    }

    //http://localhost:8080/api/collection/search/year/1897
    @GetMapping("/search/year/{year}")
    public Iterable<Collection> getAllSortedByName(@PathVariable("year") String year) {
        int yeari;
        try {
            yeari = Integer.parseInt(year);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }
        return collectionService.findMuntCollectiesByJaartal(yeari);
    }

    @PutMapping("/update/{id}")
    public Iterable<Collection> update(@Valid @RequestBody Collection collection, @PathVariable("id") String id) {
       long lid;
        try {
            lid = Long.parseLong(id);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }
        Collection n = collectionService.findById(lid);
        n.setAttributes(collection);
        try {
            collectionService.update(n);
        } catch (DataIntegrityViolationException e) {
            throw new application.service.ServiceException("error", "Land en jaartal moeten uniek zijn");
        }

        return collectionService.findAll();

    }

    @PostMapping("/addCoin/{collection_id}")
    public Collection addCoin(@PathVariable("collection_id") String collection_id, @RequestParam("coinId") String coin_id) throws ServiceException {
        Long myl;
        Long myc;
        try{
            myl = Long.parseLong(coin_id);
            myc = Long.parseLong(collection_id);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }
        Collection collection = collectionService.findById(myc);
        return appService.addCoinToCollection(myl, collection);
    }

    @GetMapping("/{collection_id}/getCoins")
    public Iterable<Coin> getAllCoinsOfCollection(@PathVariable("collection_id") String collection_id) {

        Long myc;
        try{

            myc = Long.parseLong(collection_id);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }
        Collection collection = collectionService.findById(myc);
        return collection.getCoins();
    }
    
    @DeleteMapping("/removeCoin/{collection_id}")
    public Collection removeCoin(@PathVariable("collection_id") String collection_id, @RequestParam("coinId") String coin_id) {
        Long myl;
        Long myc;
        try{
            myl = Long.parseLong(coin_id);
            myc = Long.parseLong(collection_id);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }

        //Collection collection = collectionService.findById(myc);
        return appService.removeCoinFromCollection(myl, myc);
    }






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
            errors.put(((application.service.ServiceException) ex).getAction(), ex.getMessage());
        }
        else {
            errors.put(((ResponseStatusException)ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }

}
