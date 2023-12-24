package team.bupt.h7.services.impl

import team.bupt.h7.dao.PlaceSeekerDao
import team.bupt.h7.dao.UserDao
import team.bupt.h7.dao.WelcomeOfferDao
import team.bupt.h7.exceptions.PlaceSeekerNotFoundException
import team.bupt.h7.exceptions.UserNotFoundException
import team.bupt.h7.exceptions.UserNotOwnerException
import team.bupt.h7.exceptions.WelcomeOfferNotFoundException
import team.bupt.h7.models.entities.WelcomeOffer
import team.bupt.h7.models.entities.WelcomeOfferStatus
import team.bupt.h7.models.requests.WelcomeOfferCreateRequest
import team.bupt.h7.models.requests.WelcomeOfferQueryParams
import team.bupt.h7.models.requests.WelcomeOfferUpdateRequest
import team.bupt.h7.services.WelcomeOfferService
import java.time.Instant

class WelcomeOfferServiceImpl(
    private val welcomeOfferDao: WelcomeOfferDao,
    private val userDao: UserDao,
    private val placeSeekerDao: PlaceSeekerDao
) : WelcomeOfferService {
    override fun createWelcomeOffer(
        userId: Long,
        request: WelcomeOfferCreateRequest
    ): WelcomeOffer {
        val user = userDao.getUserById(userId)
            ?: throw UserNotFoundException()
        val seeker = placeSeekerDao.getPlaceSeekerById(request.seekerId)
            ?: throw PlaceSeekerNotFoundException()
        val now = Instant.now()
        val welcomeOffer = WelcomeOffer {
            this.user = user
            this.seeker = seeker
            offerDescription = request.offerDescription
            createTime = now
            updateTime = now
            status = WelcomeOfferStatus.Active
        }
        return welcomeOfferDao.createWelcomeOffer(welcomeOffer)
    }

    override fun getWelcomeOfferById(welcomeOfferId: Long): WelcomeOffer {
        return welcomeOfferDao.getWelcomeOfferById(welcomeOfferId)
            ?: throw WelcomeOfferNotFoundException()
    }

    override fun updateWelcomeOffer(
        userId: Long,
        welcomeOfferId: Long,
        request: WelcomeOfferUpdateRequest
    ): WelcomeOffer {
        val welcomeOffer = welcomeOfferDao.getWelcomeOfferById(welcomeOfferId)
            ?: throw WelcomeOfferNotFoundException()

        // check if the user is the owner of the welcome offer
        if (welcomeOffer.user.userId != userId) {
            throw UserNotOwnerException()
        }

        val now = Instant.now()
        welcomeOffer.apply {
            request.offerDescription?.let { offerDescription = it }
            updateTime = now
        }
        return welcomeOfferDao.updateWelcomeOffer(welcomeOffer)
    }

    override fun cancelWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer {
        val welcomeOffer = welcomeOfferDao.getWelcomeOfferById(welcomeOfferId)
            ?: throw WelcomeOfferNotFoundException()
        if (welcomeOffer.user.userId != userId) {
            throw UserNotOwnerException()
        }
        welcomeOffer.status = WelcomeOfferStatus.Cancelled
        return welcomeOfferDao.updateWelcomeOffer(welcomeOffer)
    }

    override fun queryWelcomeOffers(
        page: Int,
        pageSize: Int,
        params: WelcomeOfferQueryParams
    ): List<WelcomeOffer> {
        return welcomeOfferDao.queryWelcomeOffers(page, pageSize, params)
    }
}