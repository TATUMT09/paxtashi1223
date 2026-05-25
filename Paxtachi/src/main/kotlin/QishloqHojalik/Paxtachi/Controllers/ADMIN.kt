package QishloqHojalik.Paxtachi.Controllers

import QishloqHojalik.Paxtachi.Dtos.PageResponse
import QishloqHojalik.Paxtachi.Dtos.UserCreateRequest
import QishloqHojalik.Paxtachi.Dtos.UserResponse
import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Security.AccessService
import QishloqHojalik.Paxtachi.Services.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/users")
class AdminController(
    private val userService: UserService,
    private val accessService: AccessService
) {

    private fun check(request: HttpServletRequest) {
        accessService.checkAccess(request, Direction.ADMIN)
    }

    @PostMapping
    fun create(
        request: HttpServletRequest,
        @RequestBody dto: UserCreateRequest
    ): UserResponse {

        check(request)

        return userService.createUser(request, dto)
    }

    @GetMapping
    fun getAll(
        request: HttpServletRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?
    ): PageResponse<UserResponse> {

        check(request)

        return userService.getAll(request, page, size, search)
    }

    @GetMapping("/{id}")
    fun getById(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): UserResponse {

        check(request)

        return userService.getById(request, id)
    }

    @PutMapping("/{id}")
    fun update(
        request: HttpServletRequest,
        @PathVariable id: Long,
        @RequestBody dto: UserCreateRequest
    ): UserResponse {

        check(request)

        return userService.updateUser(request, id, dto)
    }

    @DeleteMapping("/{id}")
    fun delete(
        request: HttpServletRequest,
        @PathVariable id: Long
    ): String {

        check(request)

        return userService.deleteUser(request, id)
    }
}