package m.bowzyk.shopsearch.searchapi.controller.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import m.bowzyk.shopsearch.searchapi.exception.ErrorType;
import m.bowzyk.shopsearch.searchapi.exception.ShopSearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.beans.ConstructorProperties;

@Slf4j
@Value
@Builder
public class ErrorResponse {

    ErrorType errorType;
    String description;

    @ConstructorProperties({"errorType", "description"})
    ErrorResponse(final ErrorType errorType, final String description) {
        this.errorType = errorType;
        this.description = description;
    }

    public static ErrorResponse fromException(final ShopSearchException exception) {
        final ErrorResponse response = builder()
                .errorType(exception.getErrorType())
                .description(exception.getDescription())
                .build();
        log.error(response.toString());

        return response;
    }

    public ResponseEntity<ErrorResponse> toResponseEntityWithStatus(final HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(this);
    }
}
