package team.bupt.h7.services.impl

import team.bupt.h7.dao.*
import team.bupt.h7.exceptions.*
import team.bupt.h7.models.entities.PlaceSeekerStatus
import team.bupt.h7.models.entities.SeekPlaceDeal
import team.bupt.h7.models.entities.WelcomeOffer
import team.bupt.h7.models.entities.WelcomeOfferStatus
import team.bupt.h7.models.requests.WelcomeOfferCreateRequest
import team.bupt.h7.models.requests.WelcomeOfferQueryParams
import team.bupt.h7.models.requests.WelcomeOfferUpdateRequest
import team.bupt.h7.services.WelcomeOfferService

class WelcomeOfferServiceImpl(
    private val welcomeOfferDao: WelcomeOfferDao,
    private val userDao: UserDao,
    private val placeSeekerDao: PlaceSeekerDao,
    private val seekPlaceDealDao: SeekPlaceDealDao,
    private val transactionDao: TransactionDao
) : WelcomeOfferService {
    override fun createWelcomeOffer(
        userId: Long,
        request: WelcomeOfferCreateRequest
    ): WelcomeOffer {
        val user = userDao.getUserById(userId)
            ?: throw UserNotFoundException()
        val seeker = placeSeekerDao.getPlaceSeekerById(request.seekerId)
            ?: throw PlaceSeekerNotFoundException()
        val welcomeOffer = WelcomeOffer {
            this.user = user
            this.seeker = seeker
            offerDescription = request.offerDescription
            attachmentUrl = request.attachmentUrl
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
        val offer = transactionDao.transaction {
            val welcomeOffer = welcomeOfferDao.getWelcomeOfferById(welcomeOfferId)
                ?: throw WelcomeOfferNotFoundException()

            // check if the user is the owner of the welcome offer
            if (welcomeOffer.user.userId != userId) {
                throw UserNotOwnerException()
            }
            // check if the welcome offer is active
            if (welcomeOffer.status != WelcomeOfferStatus.Active) {
                throw WelcomeOfferNotActiveException()
            }

            welcomeOffer.apply {
                request.offerDescription?.let { offerDescription = it }
            }
            welcomeOfferDao.updateWelcomeOffer(welcomeOffer)
        }
        return offer
    }

    override fun cancelWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer {
        val offer = transactionDao.transaction {
            val welcomeOffer = welcomeOfferDao.getWelcomeOfferById(welcomeOfferId)
                ?: throw WelcomeOfferNotFoundException()
            if (welcomeOffer.user.userId != userId) {
                throw UserNotOwnerException()
            }
            if (welcomeOffer.status != WelcomeOfferStatus.Active) {
                throw WelcomeOfferNotActiveException()
            }
            welcomeOffer.status = WelcomeOfferStatus.Cancelled
            welcomeOfferDao.updateWelcomeOffer(welcomeOffer)
        }
        return offer
    }

    override fun queryWelcomeOffers(
        page: Int,
        pageSize: Int,
        params: WelcomeOfferQueryParams
    ): Pair<List<WelcomeOffer>, Int> {
        return welcomeOfferDao.queryWelcomeOffers(page, pageSize, params)
    }

    override fun acceptWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer {
        val offer = transactionDao.transaction {
            val offer = getAndValidateOffer(userId, welcomeOfferId)
            val seeker = offer.seeker

            // mark the offer as accepted
            offer.status = WelcomeOfferStatus.Accepted
            welcomeOfferDao.updateWelcomeOffer(offer)

            // mark the seeker as completed
            seeker.status = PlaceSeekerStatus.Completed
            placeSeekerDao.updatePlaceSeeker(seeker)

            // mark all other active offers as expired
            welcomeOfferDao.updateWelcomeOfferStatusBySeekerId(
                seeker.seekerId, mapOf(
                    WelcomeOfferStatus.Active to WelcomeOfferStatus.Expired
                )
            )

            // make a deal
            val deal = SeekPlaceDeal {
                this.seeker = seeker
                this.offer = offer
                // TODO: calculate the price
                seekerPrice = 200
                offerPrice = 200
            }
            seekPlaceDealDao.createSeekPlaceDeal(deal)
            offer
        }
        return offer
    }

    override fun declineWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer {
        val offer = transactionDao.transaction {
            val offer = getAndValidateOffer(userId, welcomeOfferId)

            offer.status = WelcomeOfferStatus.Declined
            welcomeOfferDao.updateWelcomeOffer(offer)
        }
        return offer
    }

    private fun getAndValidateOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer {
        val offer = welcomeOfferDao.getWelcomeOfferById(welcomeOfferId)
            ?: throw WelcomeOfferNotFoundException()
        val seeker = offer.seeker

        if (userId != seeker.user.userId) {
            throw UserNotOwnerException()
        }
        if (offer.status != WelcomeOfferStatus.Active) {
            throw WelcomeOfferNotActiveException()
        }
        if (seeker.status != PlaceSeekerStatus.Active) {
            throw PlaceSeekerNotActiveException()
        }

        return offer
    }
}
