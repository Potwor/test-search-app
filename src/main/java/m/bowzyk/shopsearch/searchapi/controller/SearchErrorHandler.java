package m.bowzyk.shopsearch.searchapi.controller;

import m.bowzyk.shopsearch.searchapi.controller.api.ErrorResponse;
import m.bowzyk.shopsearch.searchapi.exception.CouldntFindISBNForBookException;
import m.bowzyk.shopsearch.searchapi.exception.CouldntGetResponseFromGoogleException;
import m.bowzyk.shopsearch.searchapi.exception.SearchParamWereNotGivenException;
import m.bowzyk.shopsearch.searchapi.exception.ShopSearchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class SearchErrorHandler {

    @ExceptionHandler(SearchParamWereNotGivenException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(final ShopSearchException exception) {
        return ErrorResponse
                .fromException(exception)
                .toResponseEntityWithStatus(BAD_REQUEST);
    }

    @ExceptionHandler(CouldntFindISBNForBookException.class)
    public ResponseEntity<ErrorResponse> handleConflict(final ShopSearchException exception) {
        return ErrorResponse
                .fromException(exception)
                .toResponseEntityWithStatus(CONFLICT);
    }

    @ExceptionHandler(CouldntGetResponseFromGoogleException.class)
    public ResponseEntity<ErrorResponse> handleBadGateway(final ShopSearchException exception) {
        return ErrorResponse
                .fromException(exception)
                .toResponseEntityWithStatus(BAD_GATEWAY);
    }
}
