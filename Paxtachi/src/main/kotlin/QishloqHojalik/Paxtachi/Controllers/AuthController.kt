package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.LoginRequest
import QishloqHojalik.Paxtachi.Dtos.LoginResponse
import QishloqHojalik.Paxtachi.Dtos.RegisterRequest
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Enums.Role
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import QishloqHojalik.Paxtachi.Services.AuthService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): String {

        if (userRepository.findByUsernameAndDeletedFalse(request.username) != null) {
            throw RuntimeException("Bu username mavjud")
        }

        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password)!!,
            direction = request.direction,
            role = Role.ADMIN
        )

        userRepository.save(user)

        return "User saqlandi"
    }
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): LoginResponse {

        return authService.login(request)
    }
}