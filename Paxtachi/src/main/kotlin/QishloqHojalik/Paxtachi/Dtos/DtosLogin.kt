package QishloqHojalik.Paxtachi.Dtos
import QishloqHojalik.Paxtachi.Enums.Direction

data class LoginRequest(
    val username: String,
    val password: String
)
data class LoginResponse(
    val username: String,
    val direction: Direction,
    val panelUrl: String,
    val token: String
)

