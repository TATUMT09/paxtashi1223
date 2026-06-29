package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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
        return extractAllClaims(token).subject
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
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
                val claims = jwtService.extractAllClaims(token)
                val username = claims.subject
                val role = claims["role"] as? String
                val direction = claims["direction"] as? String

                request.setAttribute("username", username)
                if (role != null) request.setAttribute("role", role)
                if (direction != null) request.setAttribute("direction", direction)

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
