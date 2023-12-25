package team.bupt.h7.services

import team.bupt.h7.models.entities.SeekPlaceDealStatistic
import team.bupt.h7.models.requests.SeekPlaceDealStatisticQueryParams

interface SeekPlaceDealService {
    fun querySeekPlaceDealStatistics(params: SeekPlaceDealStatisticQueryParams): List<SeekPlaceDealStatistic>
}