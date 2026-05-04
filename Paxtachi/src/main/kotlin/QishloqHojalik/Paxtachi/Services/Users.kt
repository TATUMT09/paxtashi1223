package QishloqHojalik.Paxtachi.Services

import QishloqHojalik.Paxtachi.Dtos.PageResponse
import QishloqHojalik.Paxtachi.Dtos.UserCreateRequest
import QishloqHojalik.Paxtachi.Dtos.UserResponse
import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Repositories.UserRepository
import QishloqHojalik.Paxtachi.Security.AccessService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

interface UserService {
    fun createUser(request: HttpServletRequest, dto: UserCreateRequest): UserResponse
    fun getAll(
        request: HttpServletRequest,
        page: Int,
        size: Int,
        search: String?
    ): PageResponse<UserResponse>
    fun getById(request: HttpServletRequest, id: Long): UserResponse
    fun updateUser(request: HttpServletRequest, id: Long, dto: UserCreateRequest): UserResponse
    fun deleteUser(request: HttpServletRequest, id: Long): String
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val accessService: AccessService
) : UserService {

    override fun createUser(
        request: HttpServletRequest,
        dto: UserCreateRequest
    ): UserResponse {

        accessService.checkAccess(request, dto.direction)

        val user = User(
            username = dto.username,
            password = dto.password,
            direction = dto.direction,
            role = dto.role
        )

        return mapToResponse(userRepository.save(user))
    }

    override fun getAll(
        request: HttpServletRequest,
        page: Int,
        size: Int,
        search: String?
    ): PageResponse<UserResponse> {

        val role = request.getAttribute("role") as String
        val direction = request.getAttribute("direction") as String

        val pageable = PageRequest.of(page, size)

        val result = if (role == "SUPER_ADMIN") {

            if (search.isNullOrBlank()) {
                userRepository.findAllByDeletedFalse(pageable)
            } else {
                userRepository.findAllByDeletedFalseAndUsernameContainingIgnoreCase(
                    search,
                    pageable
                )
            }

        } else {

            if (search.isNullOrBlank()) {
                userRepository.findAllByDeletedFalseAndDirection(
                    Direction.valueOf(direction),
                    pageable
                )
            } else {
                userRepository.findAllByDeletedFalseAndDirectionAndUsernameContainingIgnoreCase(
                    Direction.valueOf(direction),
                    search,
                    pageable
                )
            }
        }

        return PageResponse(
            content = result.content.map { mapToResponse(it) },
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalPages = result.totalPages
        )
    }

    override fun getById(request: HttpServletRequest, id: Long): UserResponse {

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User topilmadi") }

        accessService.checkAccess(request, user.direction)

        return mapToResponse(user)
    }

    override fun updateUser(
        request: HttpServletRequest,
        id: Long,
        dto: UserCreateRequest
    ): UserResponse {

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User topilmadi") }

        accessService.checkAccess(request, user.direction)

        user.username = dto.username
        user.password = dto.password
        user.direction = dto.direction
        user.role = dto.role

        return mapToResponse(userRepository.save(user))
    }

    override fun deleteUser(request: HttpServletRequest, id: Long): String {

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User topilmadi") }

        accessService.checkAccess(request, user.direction)

        user.deleted = true
        userRepository.save(user)

        return "User o‘chirildi"
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