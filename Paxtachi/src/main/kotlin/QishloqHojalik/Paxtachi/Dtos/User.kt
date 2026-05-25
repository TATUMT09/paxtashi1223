package QishloqHojalik.Paxtachi.Dtos

import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Enums.Role

data class RegisterRequest(
    val username: String,
    val password: String,
    val direction: Direction
)
data class UserCreateRequest(
    val username: String,
    val password: String,
    val direction: Direction,
    val role: Role = Role.USER
)
data class UserResponse(
    val id: Long,
    val username: String,
    val direction: Direction,
    val role: Role
)
data class PageResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
data class UpdateMeRequest(
    val username: String,
    val password: String
)
