package team.bupt.h7.models.entities

import org.ktorm.entity.Entity
import java.time.Instant

interface SeekPlaceDeal : Entity<SeekPlaceDeal> {
    companion object : Entity.Factory<SeekPlaceDeal>()

    val dealId: Long
    var seeker: PlaceSeeker
    var offer: WelcomeOffer
    var seekerPrice: Int
    var offerPrice: Int
    var createTime: Instant
}