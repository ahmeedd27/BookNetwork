package com.ahmed.Spring_Security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info=@Info(
                contact=@Contact(
                        name="ahmedbrol" ,
                        email="ahmdblr811@gmail.com"

                ) ,
                description = "OpenApi Documentation for SpringProject" ,
                title="OpenApi Specification - AHMED BROL"

        ) ,
        servers={
                @Server(
                        description = "Local ENV" ,
                        url="http://localhost:8080/api/v1"
                )

        } ,
        security = {
                @SecurityRequirement(
                    name="bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth" ,
        description = "JWT auth description",
        scheme = "bearer" ,
           type= SecuritySchemeType.HTTP ,
         bearerFormat = "JWT" ,
        in= SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
