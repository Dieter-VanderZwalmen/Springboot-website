package application.controller;

import application.model.Coin;
import application.model.Collection;
import application.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/collections")
public class CollectionController implements pageCounter {
    private final CollectionRepository collectionRepository;


    //We autowire via a constructor because autowiring via field is bad practice
    //and the autowiring is done implicitly here (look at left for the injection icon)
    public CollectionController(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private AppService appService;

    //Strings Constants
    private static final String COLLECTION = "collecties";
    private static final String TITLE = "titel";
    private static final String YEAR = "jaartal";
    private static final String MUNT_COLLECTION_OVERVIEW = "collection-overview";
    private static final String ADD_MUNT_COLLECTION = "add-collection";
    private static final String UPDATE_MUNT_COLLECTIE = "update-collection";
    private static final String MUNT_COLLECTION_OVERVIEW_REDIRECT = "redirect:/collections/overview";
    private static final String SEARCH_ERROR = "searchError";

    private static final int ITEMS_PER_PAGE = 5; //je kan ook een static final FIVE gebruiken maar dit lijkt mij minder?


    @GetMapping("/overview")
    public String overview(Model model) {


        Pageable pageable = PageRequest.of(0, ITEMS_PER_PAGE);

        Page<Collection> pagee = collectionService.findAll(pageable);



        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= pagee.getTotalPages(); i++) {
            pages.add(i);
        }


        model.addAttribute("currentPage", 1);
        model.addAttribute("pages", pages);
        model.addAttribute(COLLECTION, pagee.getContent()); //geeft de lijst mee nr de html



        return MUNT_COLLECTION_OVERVIEW;
    }
    @GetMapping("/overview/page={page}")
    public String overview(Model model, @PathVariable("page") String page) {
        int xd = Integer.parseInt(page) - 1;
        if(xd < 0) {
            xd = 1;
        }
        Pageable pageable = PageRequest.of(xd, 5);
        Page<Collection> pagee = collectionService.findAll(pageable);

        //gebruik maken van de interface
        List<Integer> pages = pageCounter.pageCounter(pagee);

        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", xd+1);
        model.addAttribute(COLLECTION, pagee.getContent()); //geeft de lijst mee nr de html

        return MUNT_COLLECTION_OVERVIEW;
    }

    //code is lelijk maar werkt wel
    @GetMapping("/overview/{comparator}/{order}/page={page}")
    public String overview(@PathVariable("comparator") String comparator,@PathVariable("order") String order, Model model) {
        Pageable pageable = null;

        if (order.equals("DESC")){
           pageable = PageRequest.of(0, ITEMS_PER_PAGE,Sort.by(Sort.Direction.DESC,comparator));
        }else{
            pageable = PageRequest.of(0, ITEMS_PER_PAGE,Sort.by(Sort.Direction.ASC,comparator));

        }

        Page<Collection> pagee = collectionService.findAll(pageable);

        List<Integer> pages = pageCounter.pageCounter(pagee);

        if (comparator.equals("land")){
            if (order.equals("ASC")){
                model.addAttribute("orderCountry", "DESC");

            }else if(order.equals("DESC")){
                model.addAttribute("orderCountry", "ASC");
            }
        }else if (comparator.equals("jaartal"))
        {
            if (order.equals("ASC")){
                model.addAttribute("orderYear", "DESC");

            }else if(order.equals("DESC")){
                model.addAttribute("orderYear", "ASC");
            }
        }


        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", 0);
        model.addAttribute(COLLECTION, pagee.getContent());

        return MUNT_COLLECTION_OVERVIEW;
    }



    @GetMapping("/add")
    public String add(Collection collection, Model model) {
        String notu = null;
        String un = null;
        model.addAttribute(YEAR, notu);
        model.addAttribute(TITLE, un);
        return ADD_MUNT_COLLECTION;
    }

    @PostMapping("/add")
    public String add(@Valid Collection collection, BindingResult result, Model model) {
        System.out.println("in postmapping add collection:");
        String notu = null;
        String un = null;
        if (result.hasErrors()) {
            return ADD_MUNT_COLLECTION;
        }


        try {
            collectionService.add(collection);
        } catch (DataIntegrityViolationException e) {
            System.out.println("message" + e.getMessage());
            //System.out.println("de lelijke oplossing???" + e.getMessage().contains("VALUES 1") );
            //hoe werkt dit?
            //als je title niet uniek is staat er in de getMessage values 1
            //als je country en year niet uniek zijn staat er values 2
            // dus deze oplossing is lelijk maar werkt wel
            //
            //private static final String TITLE = "titel";
            //    private static final String YEAR = "jaartal";
            //
            //
            if (e.getMessage().contains("LAND, JAARTAL")) {
                notu = "jaartal.land.niet.uniek";
            }
            if(e.getMessage().contains("TITEL")){
                un = "titel.niet.uniek";
            }
        }
        model.addAttribute(YEAR, notu);
        model.addAttribute(TITLE, un);

        //deze ifs zijn super lelijk maar wel duidelijk
        if (notu != null || un != null){
            return ADD_MUNT_COLLECTION;
        }
        if(notu == null && un == null){
            return MUNT_COLLECTION_OVERVIEW_REDIRECT;
        }

        //dit zou nooit mogen gebeuren
        return "error.html";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Collection collection = collectionService.findById(id);
            model.addAttribute(collection);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            //model.addAttribute(COLLECTION, muntCollectieService.findAll());
            return overview(model);
        }
        return UPDATE_MUNT_COLLECTIE;
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Collection collection, BindingResult result, Model model) {
        String notu = null;
        String un = null;
        if (result.hasErrors()) {
            return UPDATE_MUNT_COLLECTIE;
        }

        try {
            List<Coin> coinsInCollection = coinService.getAllByCollection(collection);
            for (Coin c: coinsInCollection
                 ) {
                if(c.getJaartal() > collection.getJaartal() || !c.getLand().equals(collection.getLand())) throw new ServiceException("message", "jaartal.of.land.niet.geldig");
            }
            collectionService.add(collection);
        } catch (DataIntegrityViolationException e) {
            System.out.println("update collection fout message: " + e.getMessage());

            if (Objects.requireNonNull(e.getMessage()).contains("LAND, JAARTAL")) {
                notu = "jaartal.land.niet.uniek";
                model.addAttribute(YEAR, notu);
                return UPDATE_MUNT_COLLECTIE;
            } else {
                un = "titel.niet.uniek";
                model.addAttribute(TITLE, un);
                return UPDATE_MUNT_COLLECTIE;
            }
        } catch (ServiceException e) {
            un = e.getMessage();
            model.addAttribute(TITLE, un);
            return UPDATE_MUNT_COLLECTIE;
        }
        model.addAttribute(YEAR, notu);
        model.addAttribute(TITLE, un);

        return MUNT_COLLECTION_OVERVIEW_REDIRECT;
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") long id, Model model) {
        System.out.println("getMapping");
        System.out.println(id);
        try {
            Collection collection = collectionService.findById(id);
            model.addAttribute("collection", collection);

        } catch (ServiceException e) {
            model.addAttribute("error", "collection_could_not_be_deleted");
            return overview(model);
        }
        return "remove-collection";
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable("id") long id) {
        System.out.println("postmapping");
        System.out.println(id);
        Collection collection = collectionService.findById(id);

        appService.removeAllCoinsFromCollection(collection.getId());
        collectionService.deleteWithId(id);
        return MUNT_COLLECTION_OVERVIEW_REDIRECT;
    }




    @GetMapping("/search/page={page}")
    public String search(@RequestParam("way") String way, @RequestParam("value") String value,@PathVariable("page") int page, Model model) {
        model.addAttribute("preway", way);
        if(page < 1) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, 5);

        if ("not-selected".equals(way)) {
            model.addAttribute(SEARCH_ERROR, "munt.search.option.label");
            return overview(model);
        }
        if (value.isBlank()) {
            model.addAttribute(SEARCH_ERROR, "munt.search.empty.field");
            return overview(model);
        }

        if ("land".equals(way)) {
            Page<Collection> pagee = collectionService.findMuntCollectiesByLand(value, pageable);
            List<Integer> pages;
            pages  = pageCounter.pageCounter(pagee);

            model.addAttribute(COLLECTION, pagee.getContent());
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);

            //model.addAttribute(COLLECTION, muntCollectieService.findMuntCollectiesByLand(value,pageable));
        } else {
            if (!Pattern.compile("-?\\d+(\\.\\d+)?").matcher(value).matches()) {
                model.addAttribute(SEARCH_ERROR, "must.be.number");
                return overview(model);
            }
            if (YEAR.equals(way)) {
                Page<Collection> pagee2 = collectionService.findMuntCollectiesByJaartal(Integer.parseInt(value), pageable);

                List<Integer> pages2 = pageCounter.pageCounter(pagee2);
                model.addAttribute(COLLECTION, pagee2.getContent());
                model.addAttribute("pages", pages2);
                model.addAttribute("currentPage", page);

            } else if ("jaartal-voor".equals(way)) {
                Page<Collection> pagee3 = collectionService.findMuntCollectiesByJaartalBefore(Integer.parseInt(value), pageable);

                List<Integer> pages3 = pageCounter.pageCounter(pagee3);

                model.addAttribute(COLLECTION, pagee3.getContent());
                model.addAttribute("pages", pages3);
                model.addAttribute("currentPage", page);


            }
        }
        model.addAttribute("prevalue", value);
        model.addAttribute("preway", way);
        return MUNT_COLLECTION_OVERVIEW;

    }




}
