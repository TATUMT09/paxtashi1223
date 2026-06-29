package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.UserCreateRequest
import QishloqHojalik.Paxtachi.Dtos.UserResponse
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import QishloqHojalik.Paxtachi.Security.AccessService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/super-admin/users")
class SuperAdminController(
    private val userRepository: UserRepository,
    private val accessService: AccessService,
    private val passwordEncoder: PasswordEncoder
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(request, Direction.SUPER_ADMIN)
    }

    @PostMapping
    fun create(request: HttpServletRequest, @RequestBody dto: UserCreateRequest): UserResponse {
        check(request)
        val user = User(
            username = dto.username,
            password = passwordEncoder.encode(dto.password)!!,
            direction = dto.direction,
            role = dto.role
        )
        return mapToResponse(userRepository.save(user))
    }

    @GetMapping
    fun getAll(request: HttpServletRequest): List<UserResponse> {
        check(request)
        return userRepository.findAll()
            .filter { !it.deleted }
            .map { mapToResponse(it) }
    }

    @GetMapping("/{id}")
    fun getById(request: HttpServletRequest, @PathVariable id: Long): UserResponse {
        check(request)
        val user = userRepository.findById(id).orElseThrow { RuntimeException("User topilmadi") }
        return mapToResponse(user)
    }

    @PutMapping("/{id}")
    fun update(request: HttpServletRequest, @PathVariable id: Long, @RequestBody dto: UserCreateRequest): UserResponse {
        check(request)
        val user = userRepository.findById(id).orElseThrow { RuntimeException("User topilmadi") }
        user.username = dto.username
        if (dto.password.isNotBlank()) {
            user.password = passwordEncoder.encode(dto.password)!!
        }
        user.direction = dto.direction
        user.role = dto.role
        return mapToResponse(userRepository.save(user))
    }

    @DeleteMapping("/{id}")
    fun delete(request: HttpServletRequest, @PathVariable id: Long): String {
        check(request)
        val user = userRepository.findById(id).orElseThrow { RuntimeException("User topilmadi") }
        user.deleted = true
        userRepository.save(user)
        return "User o'chirildi"
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
