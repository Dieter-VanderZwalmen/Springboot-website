package application.controller;

import application.model.Club;

import application.service.ClubService;
import application.service.DbException;
import application.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import application.service.ClubRepository;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    private static final String SEARCH_ERROR = "searchError";
    private static final String CLUB_COLLECTION_OVERVIEW = "club-overview";
    private static final String CLUB_SEARCH_OVERVIEW = "club-overview";
    private static final String CLUBS = "clubs";
    private static final String CLUB_OVERVIEW_REDIRECT = "redirect:/clubs/overview";
    private static final String ADD_CLUB = "add-club";
    private static final String MAX_AANTAL = "maxAantal";
    private static final String EMAINU = "emainu";
    private static final String NAMENU = "namenu";
    private static final String ERROR = "error";
    private static final String CLUB_NOT_FOUND = "club.not.found";


    @GetMapping("/overview")
    public String overview(Model model) {

        Pageable pageable = PageRequest.of(0, 5);
        Page<Club> pagee = clubService.findAll(pageable);


        List<Integer> pages = pageCounter.pageCounter(pagee);
        model.addAttribute("currentPage", 1);
        model.addAttribute("pages", pages);
        model.addAttribute("paginatielink", "/clubs/overview/page=");
        model.addAttribute(CLUBS, pagee.getContent()); //geeft de lijst mee nr de html

        return CLUB_COLLECTION_OVERVIEW;
    }
    @GetMapping("/overview/page={page}")
    public String overview(Model model, @PathVariable("page") int page) {


        Pageable pageable = createPageable(page);
        Page<Club> pagee = clubService.findAll(pageable);

        List<Integer> pages = pageCounter.pageCounter(pagee);
        model.addAttribute("pages", pages);
        model.addAttribute("paginatielink", "/clubs/overview/page=");
        model.addAttribute("currentPage", page);
        model.addAttribute(CLUBS, pagee.getContent()); //geeft de lijst mee nr de html

        return CLUB_COLLECTION_OVERVIEW;
    }

    private Pageable createPageable(int page) {
        page--;
        if(page < 0) {
            page = 0;
        }
        return PageRequest.of(page, 5);
    }



    @GetMapping("/overview/{comparator}/page={page}")
    public String overview(@PathVariable("comparator") String comparator, @PathVariable("page") int page, Model model) {

        Pageable pageable = createPageable(page);
        switch (comparator) {
            case "naam":

                Page<Club> pagee = clubService.findAllSortedByNamePage(pageable);
                List<Integer> pages = pageCounter.pageCounter(pagee);
                model.addAttribute("paginatielink", "/clubs/overview/naam/page=");
                model.addAttribute(CLUBS, pagee.getContent());
                model.addAttribute("pages", pages);
                model.addAttribute("currentPage", page);

                break;

            case MAX_AANTAL:
                Page<Club> pageem = clubService.findAllSortedByMaxLedenPage(pageable);
                List<Integer> pagesm = pageCounter.pageCounter(pageem);
                model.addAttribute("paginatielink", "/clubs/overview/maxAantal/page=");
                model.addAttribute(CLUBS, pageem.getContent());
                model.addAttribute("pages", pagesm);
                model.addAttribute("currentPage", page);
                break;

            default:
                //error message meegeven?
        }

        return CLUB_COLLECTION_OVERVIEW;
    }

    @GetMapping("/add")
    public String add(Club club, Model model) {
        String nameError = null;
        String emailregio = null;
        model.addAttribute(EMAINU, emailregio);
        model.addAttribute(NAMENU, nameError);

        return ADD_CLUB;
    }


    @PostMapping("/add")
    public String add(@Valid Club club, BindingResult result, Model model) {

        String nameError = null;
        String emailregio = null;

        if (result.hasErrors()) {
            return ADD_CLUB;
        }
        try {
            clubService.add(club);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            if (Objects.requireNonNull(e.getMessage()).contains("PUBLIC.CLUB(EMAIL, REGIO)")) {
                emailregio = "email.region.not.unique";
                model.addAttribute(EMAINU, emailregio);
                return ADD_CLUB;
            } else {
                nameError = "name.not.unique";
                model.addAttribute(NAMENU, nameError);
                return ADD_CLUB;
            }
        }
        model.addAttribute(EMAINU, emailregio);
        model.addAttribute(NAMENU, nameError);
        return CLUB_OVERVIEW_REDIRECT;
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {

        try {
            Club club = clubService.findById(id);
            model.addAttribute(club);
        } catch (Exception exc) {
            model.addAttribute(ERROR, CLUB_NOT_FOUND);
            model.addAttribute(CLUBS, clubService.findAll()); //geeft de lijst mee nr de html
            return CLUB_COLLECTION_OVERVIEW;
        }
        return "update-club";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Club club, BindingResult result, Model model) {
        if (clubService.getAllCollectorsFromClubList(clubService.findById(id)).size() != 0) {
//            ObjectError error = new ObjectError("error","Update niet toegestaan als club collectors heeft");
//            result.addError(error);

            String collectorError = "Club.has.collectors";
            model.addAttribute("collectorError", collectorError);
            return "update-club";
        }
        String nameError = null;
        String emailregio = null;

        if (result.hasErrors()) {
            return "update-club";
        }



        try {
            clubService.add(club);
        } catch (DataIntegrityViolationException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("PUBLIC.CLUB(EMAIL, REGIO)")) {
                emailregio = "email.region.not.unique";
                model.addAttribute("emainu", emailregio);
                return "update-club";
            } else {
                nameError = "name.not.unique";
                model.addAttribute("namenu", nameError);
                return "update-club";
            }
        }
        model.addAttribute("emainu", emailregio);
        model.addAttribute("namenu", nameError);

        return "redirect:/clubs/overview";

    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") long id, Model model) {

        System.out.println("delete pagina laden");

        try {
            Club club = clubService.findById(id);
            model.addAttribute(club);

        } catch (Exception exc) {
            model.addAttribute(ERROR, CLUB_NOT_FOUND);
            model.addAttribute(CLUBS, clubService.findAll()); //geeft de lijst mee nr de html
            return CLUB_COLLECTION_OVERVIEW;
        }
        return "remove-club";
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable("id") long id) {


        clubService.deleteById(id);
        System.out.println("club gedelete");
        return CLUB_OVERVIEW_REDIRECT;
    }


    @GetMapping("/search/page={page}")
    public String search(@RequestParam("way") String filter, @RequestParam("value") String query, @PathVariable("page") int page, Model model) {
        System.out.println(filter);
        model.addAttribute("preway", filter);

        if(page < 1) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, 5);


        if ("not-selected".equals(filter)) {
            model.addAttribute(SEARCH_ERROR, "munt.search.option.label");
            return overview(model);
        }

        if (query.isBlank()) {
            model.addAttribute(SEARCH_ERROR, "munt.search.empty.field");
            return overview(model);
        }
        if ((filter.equals(MAX_AANTAL) || filter.equals("maxAantalEnMeer")) && !Pattern.compile("-?\\d+(\\.\\d+)?").matcher(query).matches()) {
            model.addAttribute(SEARCH_ERROR, "must.be.number");
            return overview(model);
        }

        switch (filter) {
            case "naam":
                Page<Club> pagee = clubService.findByNameContaining(query, pageable);

                List<Integer> pages = new ArrayList<>();
                for(int i = 1; i <= pagee.getTotalPages(); i++) {
                    pages.add(i);
                }

                model.addAttribute(CLUBS, pagee.getContent());
                model.addAttribute("pages", pages);
                model.addAttribute("currentPage", page);
                break;
            case MAX_AANTAL:
                try {
                    Page<Club> pagee2 = clubService.getAllWithMaxLeden(Integer.parseInt(query), pageable);

                    List<Integer> pages2 = new ArrayList<>();
                    for(int i = 1; i <= pagee2.getTotalPages(); i++) {
                        pages2.add(i);
                    }
                    model.addAttribute(CLUBS, pagee2.getContent());
                    model.addAttribute("pages", pages2);
                    model.addAttribute("currentPage", page);
                } catch (Exception e) {
                    model.addAttribute(ERROR, "not.a.number");
                    return overview(model);
                }
                break;
            case "maxAantalEnMeer":
                try {
                    Page<Club> pagee2 = clubService.getAllByMaxLedenAfter(Integer.parseInt(query), pageable);

                    List<Integer> pages2 = new ArrayList<>();
                    for(int i = 1; i <= pagee2.getTotalPages(); i++) {
                        pages2.add(i);
                    }
                    model.addAttribute(CLUBS, pagee2.getContent());
                    model.addAttribute("pages", pages2);
                    model.addAttribute("currentPage", page);
                } catch (Exception e) {
                    model.addAttribute(ERROR, "not.a.number");
                    return overview(model);
                }
                break;
        }
        model.addAttribute("prevalue", query);
        model.addAttribute("preway", filter);
        return CLUB_SEARCH_OVERVIEW;
    }
}
