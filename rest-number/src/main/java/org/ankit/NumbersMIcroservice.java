package org.ankit;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@ApplicationPath("/")
@OpenAPIDefinition(
        info = @Info(title = "Numbers Microservice", version = "0.1"),
        tags = {
        @Tag(name = "api" ,description = "API Generates ISBN Numbers")
})
public class NumbersMIcroservice extends Application {
}
