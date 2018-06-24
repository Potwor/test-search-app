package m.bowzyk.shopsearch.searchapi.exception;

public class CouldntFindISBNForBookException extends ShopSearchException {

    public CouldntFindISBNForBookException(final String bookTitle) {
        super(ErrorType.COULDNT_FIND_ISBN_NUMBER,
                "Could't find ISBN for book title " + bookTitle);
    }
}
