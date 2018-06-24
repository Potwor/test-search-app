package m.bowzyk.shopsearch.searchapi.feignclient.google;

import lombok.Builder;
import lombok.Value;

import java.beans.ConstructorProperties;
import java.util.List;

@Value
@Builder
public class BookResponse {
    String kind;
    Integer totalItems;
    List<Book> items;

    @ConstructorProperties({"kind", "totalItems", "items"})
    private BookResponse(final String kind,
                         final Integer totalItems,
                         final List<Book> items) {
        this.kind = kind;
        this.totalItems = totalItems;
        this.items = items;
    }
}
