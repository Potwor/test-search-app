package m.bowzyk.shopsearch.searchapi.controller.api;

import lombok.Builder;
import lombok.Value;

import java.beans.ConstructorProperties;

@Value
@Builder
public class SearchResponse {

    StoreName storeName;
    String url;

    @ConstructorProperties({"storeName", "url"})
    SearchResponse(final StoreName storeName, final String url) {
        this.storeName = storeName;
        this.url = url;
    }
}
