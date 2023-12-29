package team.bupt.h7.dao

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.lessEq
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import team.bupt.h7.models.entities.SeekPlaceDealStatistic
import team.bupt.h7.models.requests.SeekPlaceDealStatisticQueryParams

class SeekPlaceDealStatisticDao(private val database: Database) {
    fun querySeekPlaceDealStatistics(params: SeekPlaceDealStatisticQueryParams): List<SeekPlaceDealStatistic> {
        val query = database.seekPlaceDealStatistics
            .filter { it.yearMonth greaterEq params.startMonth }
            .filter { it.yearMonth lessEq params.endMonth }
        params.region?.let {
            query.filter { it.region eq params.region }
        }
        params.destinationType?.let {
            query.filter { it.destinationType eq params.destinationType }
        }
        return query.toList()
    }
}

object SeekPlaceDealStatistics : Table<SeekPlaceDealStatistic>("v_seek_place_deal_statistics") {
    val yearMonth = varchar("year_month").bindTo { it.yearMonth }
    val region = varchar("region").bindTo { it.region }
    val destinationType = varchar("destination_type").bindTo { it.destinationType }
    val totalDeals = int("total_deals").bindTo { it.totalDeals }
    val totalBrokerage = int("total_brokerage").bindTo { it.totalBrokerage }
}

val Database.seekPlaceDealStatistics get() = this.sequenceOf(SeekPlaceDealStatistics)
