package team.bupt.h7.dao

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.schema.*
import team.bupt.h7.models.entities.WelcomeOffer
import team.bupt.h7.models.entities.WelcomeOfferStatus
import team.bupt.h7.models.requests.WelcomeOfferQueryParams
import team.bupt.h7.utils.filterWithConditions
import team.bupt.h7.utils.inRange
import team.bupt.h7.utils.toJavaInstantPair
import java.time.Instant

class WelcomeOfferDao(private val database: Database) {
    fun createWelcomeOffer(welcomeOffer: WelcomeOffer): WelcomeOffer {
        database.welcomeOffers.add(welcomeOffer)

        // to retrieve the auto-generated columns
        val offer = database.welcomeOffers.find { it.offerId eq welcomeOffer.offerId }!!
        checkAndUpdateStatusIfExpired(offer)
        return offer
    }

    fun getWelcomeOfferById(welcomeOfferId: Long): WelcomeOffer? {
        val offer = database.welcomeOffers.find { it.offerId eq welcomeOfferId }
        offer?.let { checkAndUpdateStatusIfExpired(it) }
        return offer
    }

    fun updateWelcomeOffer(welcomeOffer: WelcomeOffer): WelcomeOffer {
        // update welcomeOffer's updateTime
        welcomeOffer.updateTime = Instant.now()

        database.welcomeOffers.update(welcomeOffer)
        return welcomeOffer
    }

    fun queryWelcomeOffers(
        page: Int,
        pageSize: Int,
        params: WelcomeOfferQueryParams
    ): Pair<List<WelcomeOffer>, Int> {
        val offset = (page - 1) * pageSize
        val offers = database.welcomeOffers.filterWithConditions { conditions ->
            with(params) {
                userId?.let { conditions += it eq WelcomeOffers.userId }
                seekerId?.let { conditions += it eq WelcomeOffers.seekerId }
                offerDescriptionPattern?.let { conditions += WelcomeOffers.offerDescription like "%$it%" }
                createTimeRange?.let { conditions += WelcomeOffers.createTime inRange it.toJavaInstantPair() }
                updateTimeRange?.let { conditions += WelcomeOffers.updateTime inRange it.toJavaInstantPair() }
                statusList?.let { conditions += WelcomeOffers.status inList it }
            }
        }
        val pageNumber = (offers.count() + pageSize - 1) / pageSize
        val pagedOffers = offers.drop(offset).take(pageSize).toList()
        pagedOffers.forEach { checkAndUpdateStatusIfExpired(it) }
        return pagedOffers to pageNumber
    }

    fun updateWelcomeOfferStatusBySeekerId(
        seekerId: Long,
        statusMap: Map<WelcomeOfferStatus, WelcomeOfferStatus>
    ) {
        // use DSL for efficiency
        statusMap.forEach { (oldStatus, newStatus) ->
            database.update(WelcomeOffers) {
                where { it.seekerId eq seekerId and (it.status eq oldStatus) }
                set(it.status, newStatus)
            }
        }
    }

    private fun checkAndUpdateStatusIfExpired(welcomeOffer: WelcomeOffer) {
        if (welcomeOffer.status != WelcomeOfferStatus.Active) {
            return
        }
        if (welcomeOffer.seeker.seekerExpiryDate.isBefore(Instant.now())) {
            welcomeOffer.status = WelcomeOfferStatus.Expired
            database.welcomeOffers.update(welcomeOffer)
        }
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

    val user get() = userId.referenceTable as Users
    val seeker get() = seekerId.referenceTable as PlaceSeekers
}

val Database.welcomeOffers get() = this.sequenceOf(WelcomeOffers)