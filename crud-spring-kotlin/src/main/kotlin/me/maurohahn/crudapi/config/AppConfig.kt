package me.maurohahn.crudapi.config

import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import javax.annotation.PostConstruct

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class AppConfig(
    private val env: Environment,
    private val buildProperties: BuildProperties,
) {

    @Bean
    fun appInfo(): AppInfo {

        return AppInfo.apply {
            name = buildProperties.name
            version = buildProperties.version
            description = env.getProperty("api.description") ?: ""
            contactName = env.getProperty("api.contact.name") ?: ""
            contactUrl = env.getProperty("api.contact.url") ?: ""
            contactEmail = env.getProperty("api.contact.email") ?: ""
            serverUrl = env.getProperty("api.server.url") ?: "http://localhost:8080/api"
            originRoot = env.getProperty("api.origin.root") ?: "cloud"
        }
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder(10)
    }

    @PostConstruct
    private fun init() {
        Locale.setDefault(Locale("pt", "BR"))
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"))
    }

}