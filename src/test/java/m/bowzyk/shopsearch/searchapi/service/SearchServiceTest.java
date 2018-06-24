package m.bowzyk.shopsearch.searchapi.service;

import feign.FeignException;
import feign.Response;
import m.bowzyk.shopsearch.searchapi.controller.api.SearchResponse;
import m.bowzyk.shopsearch.searchapi.exception.CouldntFindISBNForBookException;
import m.bowzyk.shopsearch.searchapi.exception.CouldntGetResponseFromGoogleException;
import m.bowzyk.shopsearch.searchapi.exception.SearchParamWereNotGivenException;
import m.bowzyk.shopsearch.searchapi.feignclient.google.Book;
import m.bowzyk.shopsearch.searchapi.feignclient.google.BookResponse;
import m.bowzyk.shopsearch.searchapi.feignclient.google.GoogleApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    @Mock
    private GoogleApi googleApi;
    @InjectMocks
    private SearchService searchService;

    @Test(expected = SearchParamWereNotGivenException.class)
    public void onFindingBook_throwException_whenNoParams() {
        searchService.findBookByTitle(null);
    }

    @Test(expected = CouldntGetResponseFromGoogleException.class)
    public void onFindingBook_throwException_whenGoogleWillNotRespond() {
        when(googleApi.getBooksByTitle(anyString(), any()))
                .thenThrow(Fixture.feignException);

        searchService.findBookByTitle(Fixture.title);
    }

    @Test(expected = CouldntFindISBNForBookException.class)
    public void onFindingBook_throwException_whenGoogleWillNotFindBook() {
        when(googleApi.getBooksByTitle(anyString(), any()))
                .thenReturn(Fixture.incorrectBookResponse);

        searchService.findBookByTitle(Fixture.title);
    }

    @Deprecated //todo change after amazon and apress give access to api
    @Test
    public void onFindingBook_returnMockedResponse_whenFindIsbnNumbers() {
        when(googleApi.getBooksByTitle(anyString(), any()))
                .thenReturn(Fixture.correctBookResponse);

        final SearchResponse response = searchService.findBookByTitle(Fixture.title);

        assertThat(response).isEqualToComparingFieldByField(SearchService.mockedResponse);
    }

    private static class Fixture {
        private final static String title = "lubie placki";
        private final static FeignException feignException =
                FeignException.errorStatus("getBooksByTitle",
                        Response.builder()
                                .status(500)
                                .headers(emptyMap())
                                .build());
        private final static String isbnNumber = "1234567890123";
        private final static String notIsbnNumber = "xyz1234xyz";

        private final static Book.Identifiers goodIdentifier = Book.Identifiers.builder()
                .type("ISBN_13")
                .identifier(isbnNumber).build();
        private final static Book.Identifiers badIdentifier = Book.Identifiers.builder()
                .type("other")
                .identifier(notIsbnNumber).build();

        private final static Book correctBook = Book.builder()
                .volumeInfo(Book.VolumeInfo.builder()
                        .title(title)
                        .industryIdentifiers(asList(goodIdentifier, badIdentifier))
                        .build()).build();
        private final static BookResponse correctBookResponse = BookResponse.builder()
                .items(singletonList(correctBook)).build();

        private final static Book incorrectBook = Book.builder()
                .volumeInfo(Book.VolumeInfo.builder()
                        .title(title)
                        .industryIdentifiers(singletonList(badIdentifier))
                        .build()).build();
        private final static BookResponse incorrectBookResponse = BookResponse.builder()
                .items(singletonList(incorrectBook)).build();
    }
}