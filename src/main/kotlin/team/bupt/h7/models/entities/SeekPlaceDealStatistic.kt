package team.bupt.h7.models.entities

import org.ktorm.entity.Entity

interface SeekPlaceDealStatistic : Entity<SeekPlaceDealStatistic> {
    companion object : Entity.Factory<SeekPlaceDealStatistic>()

    val yearMonth: String
    val region: String
    val destinationType: String
    val totalDeals: Int
    val totalBrokerage: Int
}
