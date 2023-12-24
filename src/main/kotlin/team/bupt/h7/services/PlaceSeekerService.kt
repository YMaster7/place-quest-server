package team.bupt.h7.services

import team.bupt.h7.models.entities.PlaceSeeker
import team.bupt.h7.models.requests.PlaceSeekerCreateRequest
import team.bupt.h7.models.requests.PlaceSeekerQueryParams
import team.bupt.h7.models.requests.PlaceSeekerUpdateRequest

interface PlaceSeekerService {
    fun createPlaceSeeker(userId: Long, request: PlaceSeekerCreateRequest): PlaceSeeker
    fun getPlaceSeekerById(placeSeekerId: Long): PlaceSeeker
    fun updatePlaceSeeker(
        userId: Long,
        placeSeekerId: Long,
        request: PlaceSeekerUpdateRequest
    ): PlaceSeeker

    fun cancelPlaceSeeker(userId: Long, placeSeekerId: Long): PlaceSeeker
    fun queryPlaceSeekers(
        page: Int,
        pageSize: Int,
        params: PlaceSeekerQueryParams
    ): List<PlaceSeeker>
}
