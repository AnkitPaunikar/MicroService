package org.ankit;

import jakarta.json.bind.annotation.JsonbProperty;

public record IsbnThirteen(@JsonbProperty("isbn_13") String isbn13 ) {
}
