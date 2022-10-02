package me.maurohahn.crudapi.config

import me.maurohahn.crudapi.auth.*
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val authManager: AuthManager,
    private val tokenProvider: TokenProvider,
    private val authService: AuthService
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
            .and()
            .authorizeRequests()
            /**
             * Difference between antMatcher and mvcMatcher
             * https://stackoverflow.com/questions/50536292/difference-between-antmatcher-and-mvcmatcher
             */

            .mvcMatchers("/**").hasAnyRole("AUTH_USER")
            .mvcMatchers("/login/2fa").hasAnyRole("PRE_AUTH_USER")

            .anyRequest().authenticated()
            .and()
            .addFilter(JWTAuthenticationFilter(authManager, authService))
            .addFilter(JWTAuthorizationFilter(authManager, tokenProvider))
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().mvcMatchers(

                // Remove comment below for test without auth
                // "/**",

                // Login endpoint (jwt token)
                "/auth/login",

                // OpenApi
                "/swagger-ui/**",
                "/api-docs/**",
            )
        }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {

        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOriginPatterns = listOf("*")
            allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
            allowedMethods = listOf("POST", "PUT", "DELETE", "GET", "OPTIONS", "HEAD")
        }

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }

        return source
    }
}