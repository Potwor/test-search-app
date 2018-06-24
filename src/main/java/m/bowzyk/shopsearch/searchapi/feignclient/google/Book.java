package m.bowzyk.shopsearch.searchapi.feignclient.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

import java.beans.ConstructorProperties;
import java.util.List;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    VolumeInfo volumeInfo;

    @ConstructorProperties({"volumeInfo"})
    private Book(final VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    @Value
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeInfo {
        String title;
        List<String> authors;
        List<Identifiers> industryIdentifiers;

        @ConstructorProperties({"title", "authors", "industryIdentifiers"})
        private VolumeInfo(final String title,
                           final List<String> authors,
                           final List<Identifiers> industryIdentifiers) {
            this.title = title;
            this.authors = authors;
            this.industryIdentifiers = industryIdentifiers;
        }
    }

    @Value
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identifiers {
        String type;
        String identifier;

        @ConstructorProperties({"type", "identifier"})
        private Identifiers(final String type, final String identifier) {
            this.type = type;
            this.identifier = identifier;
        }
    }
}
