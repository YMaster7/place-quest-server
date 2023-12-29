package team.bupt.h7.dao

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.like
import org.ktorm.entity.*
import org.ktorm.schema.*
import team.bupt.h7.models.entities.PlaceSeeker
import team.bupt.h7.models.entities.PlaceSeekerStatus
import team.bupt.h7.models.requests.PlaceSeekerQueryParams
import team.bupt.h7.utils.filterWithConditions
import team.bupt.h7.utils.inRange
import team.bupt.h7.utils.toJavaInstantPair
import java.time.Instant

class PlaceSeekerDao(private val database: Database) {
    fun createPlaceSeeker(placeSeeker: PlaceSeeker): PlaceSeeker {
        database.placeSeekers.add(placeSeeker)

        // to retrieve the auto-generated columns
        val seeker = database.placeSeekers.find { it.seekerId eq placeSeeker.seekerId }!!
        checkAndUpdateStatusIfExpired(seeker)
        return seeker
    }

    fun getPlaceSeekerById(placeSeekerId: Long): PlaceSeeker? {
        val seeker = database.placeSeekers.find { it.seekerId eq placeSeekerId }
        seeker?.let { checkAndUpdateStatusIfExpired(it) }
        return seeker
    }

    fun updatePlaceSeeker(placeSeeker: PlaceSeeker): PlaceSeeker {
        // update placeSeeker's updateTime
        placeSeeker.updateTime = Instant.now()

        database.placeSeekers.update(placeSeeker)
        return placeSeeker
    }

    fun queryPlaceSeekers(
        page: Int,
        pageSize: Int,
        params: PlaceSeekerQueryParams
    ): Pair<List<PlaceSeeker>, Int> {
        val offset = (page - 1) * pageSize
        val seekers = database.placeSeekers.filterWithConditions { conditions ->
            with(params) {
                userId?.let { conditions += it eq PlaceSeekers.userId }
                destinationTypeList?.let { conditions += PlaceSeekers.destinationType inList it }
                seekerTitlePattern?.let { conditions += PlaceSeekers.seekerTitle like "%$it%" }
                seekerDescriptionPattern?.let { conditions += PlaceSeekers.seekerDescription like "%$it%" }
                maxExpectedPriceRange?.let { conditions += PlaceSeekers.maxExpectedPrice inRange it }
                seekerExpiryDateRange?.let { conditions += PlaceSeekers.seekerExpiryDate inRange it.toJavaInstantPair() }
                createdAtRange?.let { conditions += PlaceSeekers.createTime inRange it.toJavaInstantPair() }
                updatedAtRange?.let { conditions += PlaceSeekers.updateTime inRange it.toJavaInstantPair() }
                statusList?.let { conditions += PlaceSeekers.status inList it }
                userRegion?.let { conditions += PlaceSeekers.user.region eq it }
            }
        }
        val pageNumber = (seekers.count() + pageSize - 1) / pageSize
        val pagedSeekers = seekers.drop(offset).take(pageSize).toList()
        pagedSeekers.forEach { checkAndUpdateStatusIfExpired(it) }
        return pagedSeekers to pageNumber
    }

    private fun checkAndUpdateStatusIfExpired(placeSeeker: PlaceSeeker) {
        if (placeSeeker.status != PlaceSeekerStatus.Active) {
            return
        }
        if (placeSeeker.seekerExpiryDate.isBefore(Instant.now())) {
            placeSeeker.status = PlaceSeekerStatus.Expired
            database.placeSeekers.update(placeSeeker)
        }
    }
}

object PlaceSeekers : Table<PlaceSeeker>("place_seekers") {
    val seekerId = long("seeker_id").primaryKey().bindTo { it.seekerId }
    val userId = long("user_id").references(Users) { it.user }
    val destinationType = varchar("destination_type").bindTo { it.destinationType }
    val seekerTitle = varchar("seeker_title").bindTo { it.seekerTitle }
    val seekerDescription = varchar("seeker_description").bindTo { it.seekerDescription }
    val attachmentUrl = varchar("attachment_url").bindTo { it.attachmentUrl }
    val maxExpectedPrice = int("max_expected_price").bindTo { it.maxExpectedPrice }
    val seekerExpiryDate = timestamp("seeker_expiry_date").bindTo { it.seekerExpiryDate }
    val createTime = timestamp("create_time").bindTo { it.createTime }
    val updateTime = timestamp("update_time").bindTo { it.updateTime }
    val status = enum<PlaceSeekerStatus>("status").bindTo { it.status }

    val user get() = userId.referenceTable as Users
}

val Database.placeSeekers get() = this.sequenceOf(PlaceSeekers)
