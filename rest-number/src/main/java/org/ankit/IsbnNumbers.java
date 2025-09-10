package org.ankit;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "ISBN numbers for Books")
public record IsbnNumbers(@Schema(required = true) @JsonbProperty("isbn_13") String isbn13,
                          @Schema(required = true) @JsonbProperty("isbn_10") String isbn10,
                          @JsonbTransient Instant generationDate) {

}