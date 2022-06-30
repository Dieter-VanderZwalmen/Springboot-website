package application.controller;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public interface pageCounter {
    static  List<Integer> pageCounter(Page page){
        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= page.getTotalPages(); i++) {
            pages.add(i);
        }
        return pages;

    }
}
