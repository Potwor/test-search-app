package m.bowzyk.shopsearch.searchapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import m.bowzyk.shopsearch.searchapi.controller.api.ErrorResponse;
import m.bowzyk.shopsearch.searchapi.controller.api.SearchResponse;
import m.bowzyk.shopsearch.searchapi.controller.api.StoreName;
import m.bowzyk.shopsearch.searchapi.exception.CouldntFindISBNForBookException;
import m.bowzyk.shopsearch.searchapi.exception.CouldntGetResponseFromGoogleException;
import m.bowzyk.shopsearch.searchapi.exception.ErrorType;
import m.bowzyk.shopsearch.searchapi.exception.SearchParamWereNotGivenException;
import m.bowzyk.shopsearch.searchapi.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static m.bowzyk.shopsearch.searchapi.controller.api.StoreName.AMAZON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchControllerTest {

    @Mock
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = standaloneSetup(new SearchController(searchService))
                .setControllerAdvice(new SearchErrorHandler())
                .build();
    }

    @Test
    public void onGetBooks_shouldReturnResult_whenTitleIsGiven() throws Exception {
        when(searchService.findBookByTitle(Fixtures.title))
                .thenReturn(Fixtures.correctSearchResponse);

        final MvcResult result = mockMvc.perform(
                get(SearchController.PATH)
                        .param(SearchController.BOOK_TITLE_PARAM_NAME, Fixtures.title))
                .andExpect(status().is(OK.value()))
                .andReturn();

        final SearchResponse mappedResponse = objectMapper.readerFor(SearchResponse.class)
                .readValue(result.getResponse().getContentAsString());

        assertThat(mappedResponse).isEqualToComparingFieldByField(Fixtures.correctSearchResponse);
    }

    @Test
    public void onGetBooks_shouldReturnErrorResponse_whenServiceThrowNoParamsException() throws Exception {
        when(searchService.findBookByTitle(null))
                .thenThrow(new SearchParamWereNotGivenException());

        final MvcResult result = mockMvc.perform(
                get(SearchController.PATH))
                .andExpect(status().is(BAD_REQUEST.value()))
                .andReturn();

        final ErrorResponse mappedResponse = objectMapper.readerFor(ErrorResponse.class)
                .readValue(result.getResponse().getContentAsString());

        assertThat(mappedResponse.getErrorType()).isEqualTo(ErrorType.SEARCH_PARAM_NOT_GIVEN);
    }

    @Test
    public void onGetBooks_shouldReturnErrorResponse_whenServiceThrowCouldntGetResponseFromGoogleException() throws Exception {
        when(searchService.findBookByTitle(Fixtures.title))
                .thenThrow(new CouldntGetResponseFromGoogleException());

        final MvcResult result = mockMvc.perform(
                get(SearchController.PATH)
                        .param(SearchController.BOOK_TITLE_PARAM_NAME, Fixtures.title))
                .andExpect(status().is(BAD_GATEWAY.value()))
                .andReturn();

        final ErrorResponse mappedResponse = objectMapper.readerFor(ErrorResponse.class)
                .readValue(result.getResponse().getContentAsString());

        assertThat(mappedResponse.getErrorType()).isEqualTo(ErrorType.COULDNT_GET_RESPONSE_FROM_GOOGLE);
    }

    @Test
    public void onGetBooks_shouldReturnErrorResponse_whenServiceThrowCouldntFindIsbnForBookException() throws Exception {
        when(searchService.findBookByTitle(Fixtures.title))
                .thenThrow(new CouldntFindISBNForBookException(Fixtures.title));

        final MvcResult result = mockMvc.perform(
                get(SearchController.PATH)
                        .param(SearchController.BOOK_TITLE_PARAM_NAME, Fixtures.title))
                .andExpect(status().is(CONFLICT.value()))
                .andReturn();

        final ErrorResponse mappedResponse = objectMapper.readerFor(ErrorResponse.class)
                .readValue(result.getResponse().getContentAsString());

        assertThat(mappedResponse.getErrorType()).isEqualTo(ErrorType.COULDNT_FIND_ISBN_NUMBER);
    }

    private static class Fixtures {
        private final static String title = "Pan Tadeusz";
        private final static StoreName storeName = AMAZON;
        private final static String url = "http://shop.com/book/buy-me";

        private final static SearchResponse correctSearchResponse =
                SearchResponse.builder()
                        .storeName(storeName)
                        .url(url)
                        .build();
    }
}