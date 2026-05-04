package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Dtos.LoginRequest
import QishloqHojalik.Paxtachi.Dtos.LoginResponse
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService   // 🔥 SHUNI QO‘SH
) {

    fun login(request: LoginRequest): LoginResponse {

        val user = userRepository.findByUsernameAndDeletedFalse(request.username)
            ?: throw RuntimeException("User topilmadi")

        if (user.password != request.password) {
            throw RuntimeException("Parol xato")
        }

        val token = jwtService.generateToken(user)   // 🔥 TOKEN YARATILDI

        val panelUrl = when(user.direction) {
            Direction.PAXTACHILIK -> "/paxtachilik"
            Direction.TUTCHILIK -> "/tutchilik"
            Direction.GALLACHILIK -> "/gallachilik"
            Direction.BOGDORCHILIK -> "/bogdorchilik"
            Direction.CHORVACHILIK -> "/chorvachilik"
        }

        return LoginResponse(
            username = user.username,
            direction = user.direction,
            panelUrl = panelUrl,
            token = token   // 🔥 ENDII TO‘G‘RI
        )
    }
}
@Configuration
class DataLoader {

    @Bean
    fun loadUsers(userRepository: UserRepository): CommandLineRunner {
        return CommandLineRunner {

            createUserIfNotExists(userRepository, "paxtachi", Direction.PAXTACHILIK)
            createUserIfNotExists(userRepository, "tutchi", Direction.TUTCHILIK)
            createUserIfNotExists(userRepository, "gallachi", Direction.GALLACHILIK)
            createUserIfNotExists(userRepository, "bogbon", Direction.BOGDORCHILIK)
            createUserIfNotExists(userRepository, "chorvador", Direction.CHORVACHILIK)

        }
    }

    fun createUserIfNotExists(
        userRepository: UserRepository,
        username: String,
        direction: Direction
    ) {
        val exists = userRepository.findByUsernameAndDeletedFalse(username)

        if (exists == null) {
            val user = User(
                username = username,
                password = "1234",
                direction = direction
            )

            userRepository.save(user)

            println("✅ $username qo‘shildi ($direction)")
        }
    }
}