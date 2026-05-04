package QishloqHojalik.Paxtachi.Repositories

import QishloqHojalik.Paxtachi.Entity.PaxtachiBalance
import QishloqHojalik.Paxtachi.Enums.Specialization
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface PaxtachiBalanceRepository : JpaRepository<PaxtachiBalance, Long> {

    fun findBySeason(
        season: Int,
        pageable: Pageable
    ): Page<PaxtachiBalance>

    @Query("""
        SELECT p
        FROM PaxtachiBalance p
        WHERE p.name = 'Туман жами'
        AND p.season = :season
    """)
    fun findTumanJami(
        @Param("season") season: Int
    ): PaxtachiBalance?
    // Qidiruv (nom, stir, kadastr)
    @Query("""
        SELECT p
        FROM PaxtachiBalance p
        WHERE p.season = :season
        AND (
            LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%'))
            OR p.stir LIKE CONCAT('%',:q,'%')
            OR LOWER(p.cadastralNumber) LIKE LOWER(CONCAT('%',:q,'%'))
        )
    """)
    fun search(
        @Param("q") q: String,
        @Param("season") season: Int,
        pageable: Pageable
    ): Page<PaxtachiBalance>

    fun findByIdAndSeason(
        id: Long,
        season: Int
    ): PaxtachiBalance?

    @Query("""
SELECT p
FROM PaxtachiBalance p
WHERE p.id >= (
    SELECT p1.id
    FROM PaxtachiBalance p1
    WHERE p1.name = :startName
    ORDER BY p1.id
    LIMIT 1
)
AND p.id <= (
    SELECT p2.id
    FROM PaxtachiBalance p2
    WHERE p2.name = :endName
    AND p2.id > (
        SELECT p3.id
        FROM PaxtachiBalance p3
        WHERE p3.name = :startName
        ORDER BY p3.id
        LIMIT 1
    )
    ORDER BY p2.id
    LIMIT 1
)
ORDER BY p.id
""")
    fun findHududSection(
        @Param("startName") startName: String,
        @Param("endName") endName: String
    ): List<PaxtachiBalance>
    @Query(
        value = """
        SELECT *
        FROM paxtachi_balance
        WHERE season = :season
        AND specialization NOT IN (
            'PAHTA_GALLA',
            'ORCHARD',
            'GRAPE',
            'VEGETABLE',
            'GREENHOUSE',
            'LIVESTOCK',
            'POULTRY',
            'FISH',
            'BEEKEEPING',
            'TREE_NURSERY'
        )
        AND stir IS NOT NULL
        AND TRIM(stir) <> ''
        AND stir <> '000000000'
    """,
        countQuery = """
        SELECT COUNT(*)
        FROM paxtachi_balance
        WHERE season = :season
        AND specialization NOT IN (
            'PAHTA_GALLA',
            'ORCHARD',
            'GRAPE',
            'VEGETABLE',
            'GREENHOUSE',
            'LIVESTOCK',
            'POULTRY',
            'FISH',
            'BEEKEEPING',
            'TREE_NURSERY'
        )
        AND stir IS NOT NULL
        AND TRIM(stir) <> ''
        AND stir <> '000000000'
    """,
        nativeQuery = true
    )
    fun findOtherSpecializations(
        @Param("season") season: Int,
        pageable: Pageable
    ): Page<PaxtachiBalance>
    @Query("""
SELECT p
FROM PaxtachiBalance p
WHERE p.season = :season
AND p.stir IS NOT NULL
AND TRIM(p.stir) <> ''
AND p.stir <> '000000000'
""")
    fun findBySeasonWithStir(
        @Param("season") season: Int,
        pageable: Pageable
    ): Page<PaxtachiBalance>
    @Query("""
SELECT p
FROM PaxtachiBalance p
WHERE p.season = :season
AND p.specialization = :specialization
AND p.stir IS NOT NULL
AND TRIM(p.stir) <> ''
AND p.stir <> '000000000'
""")
    fun findBySpecializationAndSeason(
        @Param("specialization") specialization: Specialization,
        @Param("season") season: Int,
        pageable: Pageable
    ): Page<PaxtachiBalance>
}
