package dev.codebusters.code_busters.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;


@Configuration
public class SwaggerConfig {

    String schemeName = "bearerAuth";
    String bearerFormat = "JWT";
    String scheme = "bearer";
    @Value("${server.servlet.context-path}")
    String contextPath;


    @Bean
    public OpenAPI openApiSpec() {
        return new OpenAPI()
                .info(new Info()
                        .title("Code Buster API")
                        .version("1.0"))
                .addServersItem(new Server().url(contextPath))
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                .components(new Components()
                        .addSecuritySchemes(schemeName, new SecurityScheme()
                                .name(schemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat(bearerFormat)
                                .in(SecurityScheme.In.HEADER)
                                .scheme(scheme)
                        )
                        .addSchemas("ApiErrorResponse", new ObjectSchema()
                            .addProperty("status", new IntegerSchema())
                            .addProperty("code", new StringSchema())
                            .addProperty("message", new StringSchema())
                            .addProperty("fieldErrors", new ArraySchema().items(
                                    new Schema<ArraySchema>().$ref("ApiFieldError")))
                        )
                        .addSchemas("ApiFieldError", new ObjectSchema()
                            .addProperty("code", new StringSchema())
                            .addProperty("message", new StringSchema())
                            .addProperty("property", new StringSchema())
                            .addProperty("rejectedValue", new ObjectSchema())
                            .addProperty("path", new StringSchema())
                        )
                );
        }

    @Bean
    public OperationCustomizer operationCustomizer() {
        // add error type to each operation
        return (operation, handlerMethod) -> {
            operation.getResponses().addApiResponse("4xx/5xx", new ApiResponse()
                    .description("Error")
                    .content(new Content().addMediaType("*/*", new MediaType().schema(
                            new Schema<MediaType>().$ref("ApiErrorResponse")))));
            return operation;
        };
    }


    @Bean
    public OperationCustomizer operationCustomizerSecurity() {
        return (operation, handlerMethod) -> {
            if (handlerMethod.hasMethodAnnotation(PreAuthorize.class)) {
                String expression = handlerMethod.getMethodAnnotation(PreAuthorize.class).value();

                operation.setDescription(expression.isEmpty() ? "Authorization: permitAll()" : "Authorization: " + expression);
            } else {
                operation.setDescription("Authorization: permitAll()");
            }
            return operation;
        };
    }

}
