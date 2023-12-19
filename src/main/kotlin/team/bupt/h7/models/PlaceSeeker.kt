package team.bupt.h7.models

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface PlaceSeeker : Entity<PlaceSeeker> {
    companion object : Entity.Factory<PlaceSeeker>()

    val seekerId: Long
    var user: User
    var destinationType: String
    var seekerTitle: String
    var seekerDescription: String
    var maxExpectedPrice: Int
    var seekerExpiryDate: Instant
    var createTime: Instant
    var updateTime: Instant
    var status: PlaceSeekerStatus
}

enum class PlaceSeekerStatus {
    Active, Completed, Expired, Cancelled
}

object PlaceSeekers : Table<PlaceSeeker>("place_seekers") {
    val seekerId = long("seeker_id").primaryKey().bindTo { it.seekerId }
    val userId = long("user_id").references(Users) { it.user }
    val destinationType = varchar("destination_type").bindTo { it.destinationType }
    val seekerTitle = varchar("seeker_title").bindTo { it.seekerTitle }
    val seekerDescription = varchar("seeker_description").bindTo { it.seekerDescription }
    val maxExpectedPrice = int("max_expected_price").bindTo { it.maxExpectedPrice }
    val seekerExpiryDate = timestamp("seeker_expiry_date").bindTo { it.seekerExpiryDate }
    val createTime = timestamp("create_time").bindTo { it.createTime }
    val updateTime = timestamp("update_time").bindTo { it.updateTime }
    val status = enum<PlaceSeekerStatus>("status").bindTo { it.status }
}

val Database.placeSeekers get() = this.sequenceOf(PlaceSeekers)
