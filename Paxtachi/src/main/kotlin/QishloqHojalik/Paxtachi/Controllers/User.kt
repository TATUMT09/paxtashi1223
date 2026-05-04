package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.UserCreateRequest
import QishloqHojalik.Paxtachi.Dtos.UserResponse
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val userRepository: UserRepository
) {
    @GetMapping
    fun getMe(request: HttpServletRequest): UserResponse {
        val username = request.getAttribute("username") as String
        val user = userRepository.findByUsernameAndDeletedFalse(username)
            ?: throw RuntimeException("User topilmadi")

        return mapToResponse(user)
    }
    @GetMapping("/all")
    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAllByDeletedFalse()
            .map { mapToResponse(it) }
    }
    @PutMapping
    fun updateMe(
        request: HttpServletRequest,
        @RequestBody dto: UserCreateRequest
    ): UserResponse {
        val username = request.getAttribute("username") as String
        val user = userRepository.findByUsernameAndDeletedFalse(username)
            ?: throw RuntimeException("User topilmadi")
        user.username = dto.username
        user.password = dto.password
        val saved = userRepository.save(user)
        return mapToResponse(saved)
    }

    private fun mapToResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            direction = user.direction,
            role = user.role
        )
    }
}
