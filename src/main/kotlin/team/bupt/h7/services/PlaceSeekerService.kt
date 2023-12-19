package team.bupt.h7.services

import kotlinx.datetime.toJavaInstant
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.dsl.like
import org.ktorm.entity.*
import team.bupt.h7.models.*
import team.bupt.h7.utils.filterWithConditions
import team.bupt.h7.utils.inRange
import team.bupt.h7.utils.toJavaInstantPair
import java.time.Instant

interface PlaceSeekerService {
    fun createPlaceSeeker(user: User, request: PlaceSeekerCreateRequest): PlaceSeeker
    fun getPlaceSeekerById(placeSeekerId: Long): PlaceSeeker?
    fun updatePlaceSeeker(placeSeekerId: Long, request: PlaceSeekerUpdateRequest): PlaceSeeker?
    fun cancelPlaceSeeker(placeSeekerId: Long): Boolean
    fun getPlaceSeekers(page: Int, pageSize: Int, params: PlaceSeekerQueryParams): List<PlaceSeeker>
}

class PlaceSeekerServiceImpl(private val database: Database) : PlaceSeekerService {
    override fun createPlaceSeeker(user: User, request: PlaceSeekerCreateRequest): PlaceSeeker {
        val placeSeeker = PlaceSeeker {
            this.user = user
            destinationType = request.destinationType
            seekerTitle = request.seekerTitle
            seekerDescription = request.seekerDescription
            maxExpectedPrice = request.maxExpectedPrice
            seekerExpiryDate = request.seekerExpiryDate.toJavaInstant()
            createTime = Instant.now()
            updateTime = Instant.now()
            status = PlaceSeekerStatus.Active
        }
        database.placeSeekers.add(placeSeeker)
        return placeSeeker
    }

    override fun getPlaceSeekerById(placeSeekerId: Long): PlaceSeeker? {
        return database.placeSeekers.find { it.seekerId eq placeSeekerId }
    }

    override fun updatePlaceSeeker(
        placeSeekerId: Long,
        request: PlaceSeekerUpdateRequest
    ): PlaceSeeker? {
        val placeSeeker =
            database.placeSeekers.find { it.seekerId eq placeSeekerId } ?: return null
        placeSeeker.apply {
            request.destinationType?.let { destinationType = it }
            request.seekerTitle?.let { seekerTitle = it }
            request.seekerDescription?.let { seekerDescription = it }
            request.maxExpectedPrice?.let { maxExpectedPrice = it }
            request.seekerExpiryDate?.let { seekerExpiryDate = it.toJavaInstant() }
            updateTime = Instant.now()
        }
        database.placeSeekers.update(placeSeeker)
        return placeSeeker
    }

    override fun cancelPlaceSeeker(placeSeekerId: Long): Boolean {
        val placeSeeker =
            database.placeSeekers.find { it.seekerId eq placeSeekerId } ?: return false
        placeSeeker.apply {
            status = PlaceSeekerStatus.Cancelled
            updateTime = Instant.now()
        }
        database.placeSeekers.update(placeSeeker)
        return true
    }

    override fun getPlaceSeekers(
        page: Int, pageSize: Int, params: PlaceSeekerQueryParams
    ): List<PlaceSeeker> {
        val offset = (page - 1) * pageSize
        return database.placeSeekers.filterWithConditions { conditions ->
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
            }
        }.drop(offset).take(pageSize).toList()
    }
}