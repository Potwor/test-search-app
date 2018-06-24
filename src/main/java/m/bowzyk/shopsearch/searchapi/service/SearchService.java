package m.bowzyk.shopsearch.searchapi.service;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import m.bowzyk.shopsearch.searchapi.controller.api.SearchResponse;
import m.bowzyk.shopsearch.searchapi.controller.api.StoreName;
import m.bowzyk.shopsearch.searchapi.exception.CouldntFindISBNForBookException;
import m.bowzyk.shopsearch.searchapi.exception.CouldntGetResponseFromGoogleException;
import m.bowzyk.shopsearch.searchapi.exception.SearchParamWereNotGivenException;
import m.bowzyk.shopsearch.searchapi.feignclient.google.Book;
import m.bowzyk.shopsearch.searchapi.feignclient.google.BookResponse;
import m.bowzyk.shopsearch.searchapi.feignclient.google.GoogleApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class SearchService {

    private final GoogleApi googleApi;
    private final String googleApiKey;
    private final static String ISBN_IDENTIFIER_NAME = "ISBN";
    private final static String GOOGLE_BOOK_TITLE_PARAM_NAME = "intitle ";
    final static SearchResponse mockedResponse =
            SearchResponse.builder()
                    .storeName(StoreName.AMAZON)
                    .url("http://not-real-url/book")
                    .build();

    public SearchService(final GoogleApi googleApi,
                         @Value("${app.googleApiKey}") final String googleApiKey) {
        this.googleApi = googleApi;
        this.googleApiKey = googleApiKey;
    }

    public SearchResponse findBookByTitle(final String bookTitle) {
        if (bookTitle == null) {
            throw new SearchParamWereNotGivenException();
        }

        final List<String> isbns = getIsbnsNubersForBook(bookTitle);

        return getSearchResultsFromShops(isbns);
    }

    private SearchResponse getSearchResultsFromShops(final List<String> isbns) {
        //todo get from Amazon, wait for access to advertising product api
        final CompletableFuture resultsFromAmazon = new CompletableFuture();
        //todo get from Apress, wait for response about associate
        final CompletableFuture resultsFromApress = new CompletableFuture();

        final CompletableFuture combine = CompletableFuture.allOf(resultsFromAmazon, resultsFromApress);

        if (combine.isDone()) {
            //todo compare and return result from searches
        }

        return mockedResponse;
    }

    private List<String> getIsbnsNubersForBook(final String bookTitle) {
        final BookResponse response;
        try {
            response = googleApi
                    .getBooksByTitle(GOOGLE_BOOK_TITLE_PARAM_NAME + bookTitle, googleApiKey);
        } catch (final FeignException exception) {
            throw new CouldntGetResponseFromGoogleException();
        }

        log.debug(response.toString());
        final List<Book> books = response.getItems();
        final List<String> filteredIsbns =
                books.stream()
                        .map(Book::getVolumeInfo)
                        .filter(b -> b.getTitle().toUpperCase().contains(bookTitle.toUpperCase()))
                        .flatMap(b -> b.getIndustryIdentifiers().stream())
                        .filter(i -> i.getType().contains(ISBN_IDENTIFIER_NAME))
                        .map(Book.Identifiers::getIdentifier)
                        .collect(toList());
        log.debug("filtered isbns of books: {}", filteredIsbns);

        if (filteredIsbns.isEmpty()) {
            throw new CouldntFindISBNForBookException(bookTitle);
        }
        return filteredIsbns;
    }
}
