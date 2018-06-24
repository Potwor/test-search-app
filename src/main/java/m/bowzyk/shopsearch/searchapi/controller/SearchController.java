package m.bowzyk.shopsearch.searchapi.controller;

import m.bowzyk.shopsearch.searchapi.controller.api.SearchResponse;
import m.bowzyk.shopsearch.searchapi.service.SearchService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = SearchController.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {

    final static String PATH = "/book";
    final static String BOOK_TITLE_PARAM_NAME = "bookTitle";
    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public SearchResponse getBookByTitle(@RequestParam(required = false, name = BOOK_TITLE_PARAM_NAME) final String bookTitle) {
        return searchService.findBookByTitle(bookTitle);
    }
}
