package QishloqHojalik.Paxtachi.Entity

import QishloqHojalik.Paxtachi.Enums.Direction
import QishloqHojalik.Paxtachi.Enums.Role
import jakarta.persistence.*
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,
    open val createdAt: LocalDateTime = LocalDateTime.now(),
    open var updatedAt: LocalDateTime? = null,
    open var deleted: Boolean = false
)

@Entity
@Table(name = "users")
open class User(
    open var username: String = "",
    open var password: String = "",
    @Enumerated(EnumType.STRING)
    open var direction: Direction = Direction.PAXTACHILIK,
    @Enumerated(EnumType.STRING)
    open var role: Role = Role.USER

) : BaseEntity()