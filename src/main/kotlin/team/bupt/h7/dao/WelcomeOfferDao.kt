package team.bupt.h7.dao

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.like
import org.ktorm.entity.*
import org.ktorm.schema.*
import team.bupt.h7.models.entities.WelcomeOffer
import team.bupt.h7.models.entities.WelcomeOfferStatus
import team.bupt.h7.models.requests.WelcomeOfferQueryParams
import team.bupt.h7.utils.filterWithConditions
import team.bupt.h7.utils.inRange
import team.bupt.h7.utils.toJavaInstantPair

class WelcomeOfferDao(private val database: Database) {
    fun createWelcomeOffer(welcomeOffer: WelcomeOffer): WelcomeOffer {
        database.welcomeOffers.add(welcomeOffer)
        return welcomeOffer
    }

    fun getWelcomeOfferById(welcomeOfferId: Long): WelcomeOffer? {
        return database.welcomeOffers.find { it.offerId eq welcomeOfferId }
    }

    fun updateWelcomeOffer(welcomeOffer: WelcomeOffer): WelcomeOffer {
        database.welcomeOffers.update(welcomeOffer)
        return welcomeOffer
    }

    fun queryWelcomeOffers(
        page: Int,
        pageSize: Int,
        params: WelcomeOfferQueryParams
    ): List<WelcomeOffer> {
        val offset = (page - 1) * pageSize
        return database.welcomeOffers.filterWithConditions { conditions ->
            with(params) {
                userId?.let { conditions += it eq WelcomeOffers.userId }
                seekerId?.let { conditions += it eq WelcomeOffers.seekerId }
                offerDescriptionPattern?.let { conditions += WelcomeOffers.offerDescription like "%$it%" }
                createTimeRange?.let { conditions += WelcomeOffers.createTime inRange it.toJavaInstantPair() }
                updateTimeRange?.let { conditions += WelcomeOffers.updateTime inRange it.toJavaInstantPair() }
                statusList?.let { conditions += WelcomeOffers.status inList it }
            }
        }.drop(offset).take(pageSize).toList()
    }
}

object WelcomeOffers : Table<WelcomeOffer>("welcome_offers") {
    val offerId = long("offer_id").primaryKey().bindTo { it.offerId }
    val userId = long("user_id").references(Users) { it.user }
    val seekerId = long("seeker_id").references(PlaceSeekers) { it.seeker }
    val offerDescription = varchar("offer_description").bindTo { it.offerDescription }
    val createTime = timestamp("create_time").bindTo { it.createTime }
    val updateTime = timestamp("update_time").bindTo { it.updateTime }
    val status = enum<WelcomeOfferStatus>("status").bindTo { it.status }
}

val Database.welcomeOffers get() = this.sequenceOf(WelcomeOffers)