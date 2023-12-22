package team.bupt.h7.services

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

interface WelcomeOfferService {
    fun createWelcomeOffer(
        userId: Long, request: WelcomeOfferCreateRequest
    ): WelcomeOffer

    fun getWelcomeOfferById(welcomeOfferId: Long): WelcomeOffer?
    fun updateWelcomeOffer(welcomeOfferId: Long, request: WelcomeOfferUpdateRequest): WelcomeOffer?
    fun cancelWelcomeOffer(welcomeOfferId: Long): Boolean
    fun getWelcomeOffers(
        page: Int, pageSize: Int, params: WelcomeOfferQueryParams
    ): List<WelcomeOffer>
}

class WelcomeOfferServiceImpl(private val database: Database) : WelcomeOfferService {
    override fun createWelcomeOffer(
        userId: Long, request: WelcomeOfferCreateRequest
    ): WelcomeOffer {
        val user = database.users.find { it.id eq userId }
            ?: throw IllegalArgumentException("Invalid user id")
        val placeSeeker = database.placeSeekers.find { it.seekerId eq request.seekerId }
            ?: throw IllegalArgumentException("Invalid seeker id")
        if (placeSeeker.status != PlaceSeekerStatus.Active) {
            throw IllegalStateException("Cannot create welcome offer for inactive place seeker")
        }

        val welcomeOffer = WelcomeOffer {
            this.user = user
            this.seeker = placeSeeker
            offerDescription = request.offerDescription
            createTime = Instant.now()
            updateTime = Instant.now()
            status = WelcomeOfferStatus.Active
        }
        database.welcomeOffers.add(welcomeOffer)
        return welcomeOffer
    }

    override fun getWelcomeOfferById(welcomeOfferId: Long): WelcomeOffer? {
        return database.welcomeOffers.find { it.offerId eq welcomeOfferId }
    }

    override fun updateWelcomeOffer(
        welcomeOfferId: Long, request: WelcomeOfferUpdateRequest
    ): WelcomeOffer? {
        val welcomeOffer =
            database.welcomeOffers.find { it.offerId eq welcomeOfferId } ?: return null
        if (welcomeOffer.status != WelcomeOfferStatus.Active) {
            throw IllegalStateException("Cannot update inactive welcome offer")
        }

        welcomeOffer.apply {
            request.offerDescription?.let { offerDescription = it }
            updateTime = Instant.now()
        }
        database.welcomeOffers.update(welcomeOffer)
        return welcomeOffer
    }

    override fun cancelWelcomeOffer(welcomeOfferId: Long): Boolean {
        val welcomeOffer =
            database.welcomeOffers.find { it.offerId eq welcomeOfferId } ?: return false
        welcomeOffer.apply {
            status = WelcomeOfferStatus.Cancelled
            updateTime = Instant.now()
        }
        database.welcomeOffers.update(welcomeOffer)
        return true
    }

    override fun getWelcomeOffers(
        page: Int, pageSize: Int, params: WelcomeOfferQueryParams
    ): List<WelcomeOffer> {
        val offset = (page - 1) * pageSize
        return database.welcomeOffers.filterWithConditions { conditions ->
            with(params) {
                userId?.let { conditions += it eq WelcomeOffers.userId }
                seekerId?.let { conditions += it eq WelcomeOffers.seekerId }
                offerDescriptionPattern?.let { conditions += WelcomeOffers.offerDescription like "%$it%" }
                createTimeRange?.let { conditions += WelcomeOffers.createTime inRange it.toJavaInstantPair() }
                updateTimeRange?.let { conditions += WelcomeOffers.updateTime inRange it.toJavaInstantPair() }
                statusList?.let { conditions += WelcomeOffers.status inList it }
            }
        }.drop(offset).take(pageSize).toList()
    }
}