package m.bowzyk.shopsearch.searchapi.feignclient.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleApi", url = "https://www.googleapis.com")
public interface GoogleApi {

    @GetMapping("/books/v1/volumes")
    BookResponse getBooksByTitle(@RequestParam("q") String query, @RequestParam("key") String apiKey);
}
