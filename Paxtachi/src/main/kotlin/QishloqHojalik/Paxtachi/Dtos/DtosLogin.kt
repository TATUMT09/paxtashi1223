package QishloqHojalik.Paxtachi.Dtos
import QishloqHojalik.Paxtachi.Enums.Direction

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginUserDto(
    val id: Long,
    val username: String,
    val direction: String,
    val role: String
)

data class LoginResponse(
    val accessToken: String,
    val panelUrl: String,
    val user: LoginUserDto
)

