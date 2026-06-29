package QishloqHojalik.Paxtachi.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "change_logs")
class ChangeLog(
    @Column(nullable = false) val tableName: String = "",
    @Column(nullable = false) val recordId: Long = 0,
    @Column(nullable = false) val username: String = "",
    @Column(length = 100) val direction: String? = null,
    @Column(length = 20)  val action: String? = null,
    @Column(columnDefinition = "TEXT") val reason: String = "",
    @Column(nullable = false) val changedAt: LocalDateTime = LocalDateTime.now(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0
)
