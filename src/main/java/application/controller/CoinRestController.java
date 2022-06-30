package application.controller;

import application.model.Coin;
import application.model.Collector;
import application.service.AppService;
import application.service.CoinService;
import application.service.ServiceException;
import org.h2.api.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/coin")
public class CoinRestController {
    @Autowired
    private CoinService coinService;

    @Autowired
    private AppService appService;

    @GetMapping("/overview")
    public Iterable<Coin> getAll() {
        return coinService.findAll();
    }

    //{
    //    "name" : "naam1",
    //    "land" : "land1",
    //    "value" : 1,
    //    "unit" : "unit1",
    //    "jaartal" : 1990
    //}
    @PostMapping("/add")
    public Iterable<Coin> add(@Valid @RequestBody Coin coin) {

        System.out.println(coin);
        try {
            coinService.add(coin);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("error", "Name not unique");
        }

        return coinService.findAll();
    }
    @PutMapping("/update/{id}")
    public Iterable<Coin> update(@Valid @RequestBody Coin coin, @PathVariable("id") String id) {
            long lid;
            try {
                lid = Long.parseLong(id);
            } catch (Exception e) {
                throw new ServiceException("error", "please enter numbers only");
            }
           Coin n = coinService.getById(lid);
           n.setAttributes(coin);
           try {
               coinService.update(n);
           } catch (DataIntegrityViolationException e) {
               throw new ServiceException("error", "Name not unique");
           }

            return coinService.findAll();

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
            errors.put(((ServiceException) ex).getAction(), ex.getMessage());
        }
        else {
            errors.put(((ResponseStatusException)ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }


    @DeleteMapping("/delete/{id}")
    public Iterable<Coin> delete(@PathVariable("id") String id) {
        long lid;
        try {
            lid = Long.parseLong(id);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }
        Coin coin = coinService.getById(lid);
        if(coin.getCollection() != null) {
            appService.removeCoinFromCollection(coin.getId(), coin.getCollection().getId());
        }
        coinService.delete(lid);
        return coinService.findAll();
    }

    @GetMapping("/search/year/{jaartal}")
    public Iterable<Coin> getByJaartal(@PathVariable("jaartal") String jaartal) {
        int jaartali;
        try {
            jaartali = Integer.parseInt(jaartal);
        } catch (Exception e) {
            throw new ServiceException("error", "please enter numbers only");
        }

        return coinService.getAllByJaartal(jaartali);
    }

    @GetMapping("/search/country")
    public Iterable<Coin> getByLand(@RequestParam("value") String land) {

        return coinService.getAllByLand(land);
    }

}
