package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.LoginRequest
import QishloqHojalik.Paxtachi.Dtos.LoginResponse
import QishloqHojalik.Paxtachi.Dtos.RegisterRequest
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import QishloqHojalik.Paxtachi.Services.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService,
    private val userRepository: UserRepository
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): String {
        val user = User(
            username = request.username,
            password = request.password,
            direction = request.direction
        )
        userRepository.save(user)

        return "User saqlandi"
    }
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        return authService.login(request)
    }
}
