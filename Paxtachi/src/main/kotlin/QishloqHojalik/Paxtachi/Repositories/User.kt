package QishloqHojalik.Paxtachi.Repositories

import QishloqHojalik.Paxtachi.Entity.User
import QishloqHojalik.Paxtachi.Enums.Direction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

    fun findByUsernameAndDeletedFalse(username: String): User?

    fun findAllByDeletedFalse(pageable: Pageable): Page<User>

    fun findAllByDeletedFalseAndDirection(
        direction: Direction,
        pageable: Pageable
    ): Page<User>
    fun findAllByDeletedFalseAndUsernameContainingIgnoreCase(
        username: String,
        pageable: Pageable
    ): Page<User>

    fun findAllByDeletedFalseAndDirectionAndUsernameContainingIgnoreCase(
        direction: Direction,
        username: String,
        pageable: Pageable
    ): Page<User>
    fun findAllByDeletedFalse(): List<User>
}