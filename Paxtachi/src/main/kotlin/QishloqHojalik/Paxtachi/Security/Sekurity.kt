package QishloqHojalik.Paxtachi.Security

import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Services.JwtFilter
import QishloqHojalik.Paxtachi.Services.JwtService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Service
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class SecurityConfig(
    private val jwtService: JwtService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() }
            .cors { }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/**").permitAll()
                it.requestMatchers("/api/auth/**").permitAll()
                it.requestMatchers("/uploads/**").permitAll()
                it.requestMatchers("/api/v1/auth/**").permitAll()
                it.requestMatchers("/api/v1/**").permitAll()
                // Excel upload endpointlar
                it.requestMatchers("/api/pump-stations/import").authenticated()
                it.requestMatchers("/api/v1/farmers/upload").authenticated()
                it.requestMatchers("/land/upload").authenticated()

                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtFilter(jwtService),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {

        val config = CorsConfiguration()

        config.allowCredentials = true
        config.allowedOrigins = listOf(
            "http://localhost:3000",
            "http://localhost:5173"
        )
        config.allowedHeaders = listOf("*")
        config.allowedMethods = listOf(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        )

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)

        return CorsFilter(source)
    }
}

@Service
class AccessService {

    fun checkAccess(
        request: HttpServletRequest,
        targetDirection: Direction
    ) {
        val role = request.getAttribute("role") as? String
            ?: throw RuntimeException("Token ichida role yo‘q")

        val userDirection = request.getAttribute("direction") as? String
            ?: throw RuntimeException("Token ichida direction yo‘q")

        if (role == "SUPER_ADMIN") return

        if (role == "ADMIN" && userDirection == targetDirection.name) return

        throw RuntimeException("Sizda ruxsat yo‘q ❌")
    }
}