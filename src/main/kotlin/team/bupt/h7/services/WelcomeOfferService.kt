package team.bupt.h7.services

import team.bupt.h7.models.entities.WelcomeOffer
import team.bupt.h7.models.requests.WelcomeOfferCreateRequest
import team.bupt.h7.models.requests.WelcomeOfferQueryParams
import team.bupt.h7.models.requests.WelcomeOfferUpdateRequest

interface WelcomeOfferService {
    fun createWelcomeOffer(
        userId: Long, request: WelcomeOfferCreateRequest
    ): WelcomeOffer

    fun getWelcomeOfferById(welcomeOfferId: Long): WelcomeOffer
    fun updateWelcomeOffer(
        userId: Long,
        welcomeOfferId: Long,
        request: WelcomeOfferUpdateRequest
    ): WelcomeOffer

    fun cancelWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer
    fun queryWelcomeOffers(
        page: Int,
        pageSize: Int,
        params: WelcomeOfferQueryParams
    ): Pair<List<WelcomeOffer>, Int>

    fun acceptWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer
    fun declineWelcomeOffer(userId: Long, welcomeOfferId: Long): WelcomeOffer
}
