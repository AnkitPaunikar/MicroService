package org.ankit;

import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;


@Path("/api/book")
@Tag(name = "Book Rest Endpoint")
public class BookResource {

    @Inject
    @RestClient
    NumberProxy proxy;

    @Inject
    Logger logger;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(
            summary = "Creates a Book",
            description = "Creates a book with ISBN number"
    )
    @Retry(maxRetries = 3, delay = 2000)
    @Fallback(fallbackMethod = "fallbackCreateBook")
    public Response createBook(@FormParam("title") String title, @FormParam("author") String author,
                               @FormParam("yearOfPublication") Integer yearOfPublication,
                               @FormParam("genre") String genre) {
        String Isbn13 = proxy.generateIsbnNumbers().isbn13();
        Instant createdDate = Instant.now();
        Book book = new Book(Isbn13, title, author, yearOfPublication, genre, createdDate);
        logger.info("Book is created: " + book);
        return Response.status(201).entity(book).build();
    }

    public Response fallbackCreateBook( String title, String author, Integer yearOfPublication, String genre)
            throws FileNotFoundException {
        String Isbn13 = "will be set later";
        Instant createdDate = Instant.now();
        Book book = new Book(Isbn13, title, author, yearOfPublication, genre, createdDate);
        saveBookOnDisk(book);
        logger.warn("Book is created on disk" + book);
        return Response.status(206).entity(book).build();
    }

    private void saveBookOnDisk(Book book) throws FileNotFoundException {
        String bookJson = JsonbBuilder.create().toJson(book);
        try(PrintWriter out = new PrintWriter("book" + Instant.now().toEpochMilli()+".json")){
               out.println(bookJson);
        }

    }
}
