package m.bowzyk.shopsearch.searchapi.exception;

import lombok.Getter;

public class ShopSearchException extends RuntimeException {

    @Getter
    private final ErrorType errorType;
    @Getter
    private final String description;

    ShopSearchException(final ErrorType errorType,
                        final String description) {
        this.errorType = errorType;
        this.description = description;
    }
}
