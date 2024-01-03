package team.bupt.h7.services.impl

import kotlinx.datetime.toJavaInstant
import team.bupt.h7.dao.PlaceSeekerDao
import team.bupt.h7.dao.TransactionDao
import team.bupt.h7.dao.UserDao
import team.bupt.h7.dao.WelcomeOfferDao
import team.bupt.h7.exceptions.PlaceSeekerNotActiveException
import team.bupt.h7.exceptions.PlaceSeekerNotFoundException
import team.bupt.h7.exceptions.UserNotFoundException
import team.bupt.h7.exceptions.UserNotOwnerException
import team.bupt.h7.models.entities.PlaceSeeker
import team.bupt.h7.models.entities.PlaceSeekerStatus
import team.bupt.h7.models.entities.WelcomeOfferStatus
import team.bupt.h7.models.requests.PlaceSeekerCreateRequest
import team.bupt.h7.models.requests.PlaceSeekerQueryParams
import team.bupt.h7.models.requests.PlaceSeekerUpdateRequest
import team.bupt.h7.services.PlaceSeekerService

class PlaceSeekerServiceImpl(
    private val placeSeekerDao: PlaceSeekerDao,
    private val userDao: UserDao,
    private val welcomeOfferDao: WelcomeOfferDao,
    private val transactionDao: TransactionDao
) : PlaceSeekerService {
    override fun createPlaceSeeker(userId: Long, request: PlaceSeekerCreateRequest): PlaceSeeker {
        val user = userDao.getUserById(userId)
            ?: throw UserNotFoundException()
        val placeSeeker = PlaceSeeker {
            this.user = user
            destinationType = request.destinationType
            seekerTitle = request.seekerTitle
            seekerDescription = request.seekerDescription
            attachmentUrl = request.attachmentUrl
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
        val seeker = transactionDao.transaction {
            val placeSeeker = placeSeekerDao.getPlaceSeekerById(placeSeekerId)
                ?: throw PlaceSeekerNotFoundException()

            // check if the user is the owner of the place seeker
            if (placeSeeker.user.userId != userId) {
                throw UserNotOwnerException()
            }
            // check if the place seeker is active
            if (placeSeeker.status != PlaceSeekerStatus.Active) {
                throw PlaceSeekerNotActiveException()
            }

            placeSeeker.apply {
                request.destinationType?.let { destinationType = it }
                request.seekerTitle?.let { seekerTitle = it }
                request.seekerDescription?.let { seekerDescription = it }
                request.attachmentUrl?.let { attachmentUrl = it }
                request.maxExpectedPrice?.let { maxExpectedPrice = it }
                request.seekerExpiryDate?.let { seekerExpiryDate = it.toJavaInstant() }
            }
            placeSeekerDao.updatePlaceSeeker(placeSeeker)
        }

        return seeker
    }

    override fun cancelPlaceSeeker(userId: Long, placeSeekerId: Long): PlaceSeeker {
        val seeker = transactionDao.transaction {
            val seeker = placeSeekerDao.getPlaceSeekerById(placeSeekerId)
                ?: throw PlaceSeekerNotFoundException()
            if (seeker.user.userId != userId) {
                throw UserNotOwnerException()
            }
            if (seeker.status != PlaceSeekerStatus.Active) {
                throw PlaceSeekerNotActiveException()
            }

            // mark the place seeker as cancelled
            seeker.status = PlaceSeekerStatus.Cancelled
            placeSeekerDao.updatePlaceSeeker(seeker)

            // mark all the related welcome offers as expired
            welcomeOfferDao.updateWelcomeOfferStatusBySeekerId(
                placeSeekerId, mapOf(
                    WelcomeOfferStatus.Active to WelcomeOfferStatus.Expired
                )
            )
            seeker
        }

        return seeker
    }

    override fun queryPlaceSeekers(
        page: Int,
        pageSize: Int,
        params: PlaceSeekerQueryParams
    ): Pair<List<PlaceSeeker>, Int> {
        return placeSeekerDao.queryPlaceSeekers(page, pageSize, params)
    }
}
