package org.ankit;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import java.time.Instant;
import java.util.Random;

@Path("/api/numbers")
@Tag(name = "Number End Point")
public class NumberResource {

    @Inject
    Logger logger;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Generates book Numbers",
        description = "ISBN-13 and ISBN-10"
    )
    public IsbnNumbers generateIsbnNumbers(){
            String isbn13 = "13-" + new Random().nextInt(100_000_000);
            String isbn10 = "10-" + new Random().nextInt(100_000);
            Instant generationDate = Instant.now();
            logger.info("ISBN Number is generated " + isbn13 + " " + isbn10 + " " + generationDate);
            return new IsbnNumbers(isbn13, isbn10, generationDate);
        };
    }

