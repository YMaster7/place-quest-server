package team.bupt.h7.models

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface WelcomeOffer : Entity<WelcomeOffer> {
    companion object : Entity.Factory<WelcomeOffer>()

    val offerId: Long
    var user: User
    var seeker: PlaceSeeker
    var offerDescription: String
    var createTime: Instant
    var updateTime: Instant
    var status: WelcomeOfferStatus
}

enum class WelcomeOfferStatus {
    Active, Accepted, Declined, Cancelled
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
