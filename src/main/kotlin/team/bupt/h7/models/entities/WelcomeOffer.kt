package team.bupt.h7.models.entities

import org.ktorm.entity.Entity
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

