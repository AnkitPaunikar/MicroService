package org.ankit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
class NumberResourceTest {
    @Test
    void testNumberEndpoint() {
        given()
          .when().get("/api/numbers")
          .then()
             .statusCode(200)
                .body("isbn_13", matchesPattern("13-\\d+"))
                .body("isbn_10", matchesPattern("10-\\d+"));

    }

}