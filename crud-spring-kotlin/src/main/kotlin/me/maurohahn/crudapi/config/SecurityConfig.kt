package me.maurohahn.crudapi.config

import me.maurohahn.crudapi.auth.AppAuthenticationManager
import me.maurohahn.crudapi.auth.JWTAuthenticationFilter
import me.maurohahn.crudapi.auth.JWTAuthorizationFilter
import me.maurohahn.crudapi.auth.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
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
    private val authManager: AppAuthenticationManager,
    private val tokenProvider: TokenProvider

) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
            .and()
            .authorizeRequests()
            /**
             * Difference between antMatcher and mvcMatcher
             * https://stackoverflow.com/questions/50536292/difference-between-antmatcher-and-mvcmatcher
             */

            // ADMIN full access
            // .mvcMatchers("/**").hasAnyAuthority("ADMIN")

            .anyRequest().authenticated()
            .and()
            .addFilter(JWTAuthenticationFilter(authManager, tokenProvider))
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
            allowedOrigins = listOf("*")
            allowedMethods = listOf("POST", "PUT", "DELETE", "GET", "OPTIONS", "HEAD")

            // setAllowCredentials(true) is important, otherwise:
            // The value of the 'Access-Control-Allow-Origin' header in the response
            // must not be the wildcard '*' when the request's credentials mode is 'include'.
            allowCredentials = true

            // setAllowedHeaders is important! Without it, OPTIONS preflight request
            // will fail with 403 Invalid CORS request
            // use @CrossOrigin in login endpoint --> fun login
            allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")

        }

        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }

        return source
    }
}