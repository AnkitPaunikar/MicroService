package org.ankit;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "This is a book")
public record Book(@Schema(required = true) @JsonbProperty("isbn_13") String Isbn13,
                   @Schema(required = true) String title,
                   @Schema(required = true) String author,
                   @JsonbProperty("year_of_publication") Integer yearOfPublication,
                   String genre,
                   @Schema(implementation = String.class, format = "date") @JsonbDateFormat("dd-MM-yyyy") @JsonbProperty("created_date") Instant createdDate) {

}
