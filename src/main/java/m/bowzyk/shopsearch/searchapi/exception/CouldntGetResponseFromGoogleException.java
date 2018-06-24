package m.bowzyk.shopsearch.searchapi.exception;

import static m.bowzyk.shopsearch.searchapi.exception.ErrorType.COULDNT_GET_RESPONSE_FROM_GOOGLE;

public class CouldntGetResponseFromGoogleException extends ShopSearchException {

    public CouldntGetResponseFromGoogleException() {
        super(COULDNT_GET_RESPONSE_FROM_GOOGLE, "error occurred in google client");
    }
}
