package team.bupt.h7.models.entities

import org.ktorm.entity.Entity
import java.time.Instant

interface PlaceSeeker : Entity<PlaceSeeker> {
    companion object : Entity.Factory<PlaceSeeker>()

    val seekerId: Long
    var user: User
    var destinationType: String
    var seekerTitle: String
    var seekerDescription: String
    var attachmentUrl: String
    var maxExpectedPrice: Int
    var seekerExpiryDate: Instant
    var createTime: Instant
    var updateTime: Instant
    var status: PlaceSeekerStatus
}

enum class PlaceSeekerStatus {
    Active, Completed, Expired, Cancelled
}
