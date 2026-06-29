package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Dtos.LoginRequest
import QishloqHojalik.Paxtachi.Dtos.LoginResponse
import QishloqHojalik.Paxtachi.Dtos.LoginUserDto
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Enums.Role
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {

    fun login(request: LoginRequest): LoginResponse {

        val user = userRepository.findByUsernameAndDeletedFalse(request.username)
            ?: throw RuntimeException("User topilmadi")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw RuntimeException("Parol xato")
        }

        val token = jwtService.generateToken(user)

        return LoginResponse(
            accessToken = token,
            panelUrl = user.direction.panelUrl,
            user = LoginUserDto(
                id = user.id,
                username = user.username,
                direction = user.direction.name,
                role = user.role.name
            )
        )
    }
}
@Configuration
class DataLoader(
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun loadUsers(userRepository: UserRepository): CommandLineRunner {
        return CommandLineRunner {
            createUserIfNotExists(userRepository, "paxtachi", Direction.PAXTACHILIK)
            createUserIfNotExists(userRepository, "tutchi", Direction.TUTCHILIK)
            createUserIfNotExists(userRepository, "gallachi", Direction.GALLACHILIK)
            createUserIfNotExists(userRepository, "bogbon", Direction.BOGDORCHILIK)
            createUserIfNotExists(userRepository, "chorvador", Direction.CHORVACHILIK)
            createUserIfNotExists(userRepository, "pump", Direction.PUMP)
            createUserIfNotExists(userRepository, "agrotexnik", Direction.AGRO_TECHNIC)
            createUserIfNotExists(userRepository, "kadastr", Direction.CADASTRE)
            createUserIfNotExists(userRepository, "issiqxona", Direction.GREENHOUSE)
            createUserIfNotExists(userRepository, "vakil", Direction.FARM_REPRESENTATIVE)
            createUserIfNotExists(userRepository, "yerkontur", Direction.LAND_CONTOUR)
            createSuperAdminIfNotExists(userRepository, "superadmin", Direction.SUPER_ADMIN)
            createUserIfNotExists(userRepository, "hokim", Direction.HOKIM)
        }
    }

    fun createUserIfNotExists(
        userRepository: UserRepository,
        username: String,
        direction: Direction
    ) {
        val existing = userRepository.findByUsernameAndDeletedFalse(username)

        if (existing == null) {
            val user = User(
                username = username,
                password = passwordEncoder.encode("1234")!!,
                direction = direction
            )
            userRepository.save(user)
            println("✅ $username qo’shildi ($direction)")
        } else if (!existing.password.startsWith("\$2")) {
            existing.password = passwordEncoder.encode(existing.password)!!
            userRepository.save(existing)
            println("🔄 $username paroli encode qilindi")
        }
    }

    fun createSuperAdminIfNotExists(
        userRepository: UserRepository,
        username: String,
        direction: Direction
    ) {
        val existing = userRepository.findByUsernameAndDeletedFalse(username)

        if (existing == null) {
            val user = User(
                username = username,
                password = passwordEncoder.encode("admin1234")!!,
                direction = direction,
                role = Role.SUPER_ADMIN
            )
            userRepository.save(user)
            println("✅ $username (SUPER_ADMIN) qo’shildi ($direction)")
        } else if (!existing.password.startsWith("\$2")) {
            existing.password = passwordEncoder.encode(existing.password)!!
            userRepository.save(existing)
            println("🔄 $username paroli encode qilindi")
        }
    }
}