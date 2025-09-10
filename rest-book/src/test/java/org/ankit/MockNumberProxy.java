package org.ankit;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Mock
@ApplicationScoped
@RestClient
public class MockNumberProxy implements NumberProxy{

    @Override
    public IsbnThirteen generateIsbnNumbers() {
        String isbn13 ="13-45555555";
        return new IsbnThirteen(isbn13);
    };
}
