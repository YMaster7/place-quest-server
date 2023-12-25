package team.bupt.h7.dao

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.*
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.timestamp
import team.bupt.h7.models.entities.SeekPlaceDeal
import team.bupt.h7.models.requests.SeekPlaceDealQueryParams
import team.bupt.h7.utils.filterWithConditions
import team.bupt.h7.utils.inRange
import team.bupt.h7.utils.toJavaInstantPair

class SeekPlaceDealDao(private val database: Database) {
    fun createSeekPlaceDeal(seekPlaceDeal: SeekPlaceDeal): SeekPlaceDeal {
        database.seekPlaceDeals.add(seekPlaceDeal)

        // to retrieve the auto-generated columns
        return database.seekPlaceDeals.find { it.dealId eq seekPlaceDeal.dealId }!!
    }

    fun getSeekPlaceDealById(seekPlaceDealId: Long): SeekPlaceDeal? {
        return database.seekPlaceDeals.find { it.dealId eq seekPlaceDealId }
    }

    fun querySeekPlaceDeals(
        page: Int,
        pageSize: Int,
        params: SeekPlaceDealQueryParams
    ): List<SeekPlaceDeal> {
        val offset = (page - 1) * pageSize
        return database.seekPlaceDeals.filterWithConditions { conditions ->
            with(params) {
                seekerId?.let { conditions += it eq SeekPlaceDeals.seekerId }
                offerId?.let { conditions += it eq SeekPlaceDeals.offerId }
                seekerPriceRange?.let { conditions += SeekPlaceDeals.seekerPrice inRange it }
                offerPriceRange?.let { conditions += SeekPlaceDeals.offerPrice inRange it }
                createTimeRange?.let { conditions += SeekPlaceDeals.createTime inRange it.toJavaInstantPair() }
                seekerUserRegion?.let { conditions += SeekPlaceDeals.seeker.user.region eq it }
            }
        }.drop(offset).take(pageSize).toList()
    }
}

object SeekPlaceDeals : Table<SeekPlaceDeal>("seek_place_deals") {
    val dealId = long("deal_id").primaryKey().bindTo { it.dealId }
    val seekerId = long("seeker_id").references(PlaceSeekers) { it.seeker }
    val offerId = long("offer_id").references(WelcomeOffers) { it.offer }
    val seekerPrice = int("seeker_price").bindTo { it.seekerPrice }
    val offerPrice = int("offer_price").bindTo { it.offerPrice }
    val createTime = timestamp("create_time").bindTo { it.createTime }

    val seeker get() = seekerId.referenceTable as PlaceSeekers
    val offer get() = offerId.referenceTable as WelcomeOffers
}

val Database.seekPlaceDeals get() = this.sequenceOf(SeekPlaceDeals)
