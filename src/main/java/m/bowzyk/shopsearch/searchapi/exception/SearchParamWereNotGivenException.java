package m.bowzyk.shopsearch.searchapi.exception;

import static m.bowzyk.shopsearch.searchapi.exception.ErrorType.SEARCH_PARAM_NOT_GIVEN;

public class SearchParamWereNotGivenException extends ShopSearchException {

    public SearchParamWereNotGivenException() {
        super(SEARCH_PARAM_NOT_GIVEN, "Search params were not given");
    }
}
