package me.maurohahn.crudapi.config

import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun springOpenAPI(): OpenAPI {

        val appContact = Contact().apply {
            this.name = AppInfo.contactName
            this.url = AppInfo.contactUrl
            this.email = AppInfo.contactEmail
        }

        val appLicense = License().apply {
            this.name = "MIT"
            this.url = ""
        }

        val server = Server().apply {
            url = AppInfo.serverUrl
        }

        return OpenAPI()
            .addServersItem(server)
            .components(
                Components().addSecuritySchemes(
                    "bearer-key",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .info(
                Info()
                    .title(AppInfo.name)
                    .description(AppInfo.description)
                    .version(AppInfo.version)
                    .contact(appContact)
                    .license(appLicense)
            ).addSecurityItem(
                SecurityRequirement()
                    .addList("bearer-key", emptyList())
            )
    }

//    @Bean
//    @Profile(value = ["hlg"])
//    fun clientApi(): GroupedOpenApi {
//        return GroupedOpenApi.builder().group("Client")
//            .pathsToMatch("/client**", "/auth/login")
//            .build()
//    }

}