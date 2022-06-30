package application.controller;

import application.model.Collector;
import application.service.CollectorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;




//Deze controller is niet nodig
//zitten ook fouten in
//maar ik laat hem wel staan
//~Dieter

@Controller
@RequestMapping("/collectors")
public class CollectorController {

    private static final String ERROR = "error";
    private static final String VERZAMELAAR_NOT_FOUND = "verzamelaar.not.found";
    private static final String COLLECTION = "verzamelaars";
    private static final String SEARCH_ERROR = "searchError";
    private static final String VERZAMELAAR_OVERVIEW = "collector-overview";
    private final CollectorService collectorService;

    public CollectorController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @GetMapping("/overview")
    public String overview(Model model){
        model.addAttribute("collectors", collectorService.findAll());
        return "collector-overview";
    }

    @GetMapping("/add")
    public String add(Collector collector){
        return "add-collector";
    }

    @PostMapping("/add")
    public String add(@Valid Collector collector, BindingResult result, Model model){
        if (result.hasErrors()){
            return "add-collector";
        }
        collectorService.add(collector);
        return "redirect:/collectors/overview";
    }


    //update

    //remove
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") long id, Model model) {

        //spreek api
        //json
        //return "remove-collector";
        //error
        //return error return VERZAMELAAR_OVERVIEW
        try {
            Collector verzamelaar = collectorService.findById(id);
            model.addAttribute(verzamelaar);

        } catch (Exception exc) {
            model.addAttribute(ERROR, VERZAMELAAR_NOT_FOUND);
            model.addAttribute(COLLECTION, collectorService.findAll()); //geeft de lijst mee nr de html
            return VERZAMELAAR_OVERVIEW;
        }
        return "remove-collector";
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable("id") long id) {

        collectorService.delete(id);

        return "redirect:/collectors/overview";
    }

    //search
    @GetMapping("/search")
    public String search(@RequestParam("way") String way, @RequestParam("value") String value, Model model) {
        model.addAttribute("preway", way);
        if ("not-selected".equals(way)) {
            model.addAttribute(SEARCH_ERROR, "munt.search.option.label");
            return overview(model);
        }

        if (value.isBlank()) {
            model.addAttribute(SEARCH_ERROR, "munt.search.empty.field");
            return VERZAMELAAR_OVERVIEW;
        }

        if ("regio".equals(way)) {
            model.addAttribute(COLLECTION, collectorService.getAllByRegio(value));
        } else if ("name".equals(way)) {
            model.addAttribute(COLLECTION, collectorService.getAllByName(value));
        } else if ("firstname".equals(way)) {
            model.addAttribute(COLLECTION, collectorService.getAllByFirstname(value));
        }

        model.addAttribute("prevalue", value);
        model.addAttribute("preway", way);
        return VERZAMELAAR_OVERVIEW;
    }
}
