package org.ankit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class BookResourceTest {
    @Test
    void testBookEndpoint() {
        given()
                .formParam("title", "Ankit Is Great")
                .formParam("author", "Ankit the Great")
                .formParam("yearOfPublication", 2020)
                .formParam("genre", "Inspiration")
                .when()
                .post("/api/book")
                .then()
                .statusCode(201)
                .body("isbn_13", matchesPattern("^13.*"))
                .body("title", is("Ankit Is Great"))
                .body("author", is("Ankit the Great"))
                .body("year_of_publication", is(2020))
                .body("genre", is("Inspiration"))
                .body("created_date", matchesPattern("\\d{2}-\\d{2}-\\d{4}"))

        ;
    }
}
