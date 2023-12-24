package team.bupt.h7.services.impl

import kotlinx.datetime.toJavaInstant
import team.bupt.h7.dao.PlaceSeekerDao
import team.bupt.h7.dao.UserDao
import team.bupt.h7.exceptions.PlaceSeekerNotFoundException
import team.bupt.h7.exceptions.UserNotFoundException
import team.bupt.h7.exceptions.UserNotOwnerException
import team.bupt.h7.models.entities.PlaceSeeker
import team.bupt.h7.models.entities.PlaceSeekerStatus
import team.bupt.h7.models.requests.PlaceSeekerCreateRequest
import team.bupt.h7.models.requests.PlaceSeekerQueryParams
import team.bupt.h7.models.requests.PlaceSeekerUpdateRequest
import team.bupt.h7.services.PlaceSeekerService

class PlaceSeekerServiceImpl(
    private val placeSeekerDao: PlaceSeekerDao,
    private val userDao: UserDao
) : PlaceSeekerService {
    override fun createPlaceSeeker(userId: Long, request: PlaceSeekerCreateRequest): PlaceSeeker {
        val user = userDao.getUserById(userId)
            ?: throw UserNotFoundException()
        val placeSeeker = PlaceSeeker {
            this.user = user
            destinationType = request.destinationType
            seekerTitle = request.seekerTitle
            seekerDescription = request.seekerDescription
            maxExpectedPrice = request.maxExpectedPrice
            seekerExpiryDate = request.seekerExpiryDate.toJavaInstant()
            status = PlaceSeekerStatus.Active
        }
        return placeSeekerDao.createPlaceSeeker(placeSeeker)
    }

    override fun getPlaceSeekerById(placeSeekerId: Long): PlaceSeeker {
        return placeSeekerDao.getPlaceSeekerById(placeSeekerId)
            ?: throw PlaceSeekerNotFoundException()
    }

    override fun updatePlaceSeeker(
        userId: Long,
        placeSeekerId: Long,
        request: PlaceSeekerUpdateRequest
    ): PlaceSeeker {
        val placeSeeker = placeSeekerDao.getPlaceSeekerById(placeSeekerId)
            ?: throw PlaceSeekerNotFoundException()

        // check if the user is the owner of the place seeker
        if (placeSeeker.user.userId != userId) {
            throw UserNotOwnerException()
        }

        placeSeeker.apply {
            request.destinationType?.let { destinationType = it }
            request.seekerTitle?.let { seekerTitle = it }
            request.seekerDescription?.let { seekerDescription = it }
            request.maxExpectedPrice?.let { maxExpectedPrice = it }
            request.seekerExpiryDate?.let { seekerExpiryDate = it.toJavaInstant() }
        }

        return placeSeekerDao.updatePlaceSeeker(placeSeeker)
    }

    override fun cancelPlaceSeeker(userId: Long, placeSeekerId: Long): PlaceSeeker {
        val placeSeeker = placeSeekerDao.getPlaceSeekerById(placeSeekerId)
            ?: throw PlaceSeekerNotFoundException()
        if (placeSeeker.user.userId != userId) {
            throw UserNotOwnerException()
        }
        placeSeeker.status = PlaceSeekerStatus.Cancelled
        return placeSeekerDao.updatePlaceSeeker(placeSeeker)
    }

    override fun queryPlaceSeekers(
        page: Int,
        pageSize: Int,
        params: PlaceSeekerQueryParams
    ): List<PlaceSeeker> {
        return placeSeekerDao.queryPlaceSeekers(page, pageSize, params)
    }
}
