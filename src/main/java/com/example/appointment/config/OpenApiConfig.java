package com.example.appointment.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Appointment Booking API",
                version = "v1",
                description = """
                        Demo users for Swagger authorization:

                        ADMIN - ID: admin@example.com, Password: admin123

                        USER - ID: user@example.com, Password: user123
                        """
        )
)
// Registers HTTP Basic authentication in Swagger UI.
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenApiConfig {
}
