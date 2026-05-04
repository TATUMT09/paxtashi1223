package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.UserCreateRequest
import QishloqHojalik.Paxtachi.Dtos.UserResponse
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/super-admin/users")
class SuperAdminController(
    private val userRepository: UserRepository
) {

    @PostMapping
    fun create(
        request: HttpServletRequest,
        @RequestBody dto: UserCreateRequest
    ): UserResponse {

        checkSuperAdmin(request)

        val user = User(
            username = dto.username,
            password = dto.password,
            direction = dto.direction,
            role = dto.role   // 🔥 admin yoki user
        )

        return mapToResponse(userRepository.save(user))
    }

    @GetMapping
    fun getAll(request: HttpServletRequest): List<UserResponse> {

        checkSuperAdmin(request)

        return userRepository.findAll()
            .filter { !it.deleted }
            .map { mapToResponse(it) }
    }

    @GetMapping("/{id}")
    fun getById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): UserResponse {

        checkSuperAdmin(request)

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User topilmadi") }

        return mapToResponse(user)
    }

    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: UserCreateRequest
    ): UserResponse {

        checkSuperAdmin(request)

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User topilmadi") }

        user.username = dto.username
        user.password = dto.password
        user.direction = dto.direction
        user.role = dto.role   // 🔥 role ham o‘zgartiradi

        return mapToResponse(userRepository.save(user))
    }

    @DeleteMapping("/{id}")
    fun delete(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): String {

        checkSuperAdmin(request)

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User topilmadi") }

        user.deleted = true
        userRepository.save(user)

        return "User o‘chirildi"
    }

    private fun checkSuperAdmin(request: HttpServletRequest) {
        val role = request.getAttribute("role") as String

        if (role != "SUPER_ADMIN") {
            throw RuntimeException("Faqat super admin ruxsat ❌")
        }
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