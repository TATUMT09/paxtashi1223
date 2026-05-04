package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jdk.internal.org.jline.keymap.KeyMap.key
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

import java.nio.charset.StandardCharsets


@Service
class JwtService {

    private val secret = "secret-key-12345678901234567890123456789012" // kamida 32 belgi
    private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

    fun generateToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.username)
            .claim("role", user.role.name)
            .claim("direction", user.direction.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(key)
            .compact()
    }

    fun extractUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)   // 🔥 SHU TO‘G‘RI
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }
}


class JwtFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)

            try {
                val username = jwtService.extractUsername(token)

                val auth = UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    emptyList()
                )

                SecurityContextHolder.getContext().authentication = auth

            } catch (e: Exception) {
                println("JWT xato: ${e.message}")
            }
        }

        filterChain.doFilter(request, response)
    }
}
@Bean
fun corsConfigurationSource(): CorsConfigurationSource {

    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("http://localhost:3000")
    configuration.allowedMethods = listOf("GET","POST","PUT","DELETE","OPTIONS")
    configuration.allowedHeaders = listOf("*")
    configuration.allowCredentials = true

    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)

    return source
}


