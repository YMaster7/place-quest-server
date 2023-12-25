package team.bupt.h7.services.impl

import team.bupt.h7.dao.SeekPlaceDealStatisticDao
import team.bupt.h7.models.entities.SeekPlaceDealStatistic
import team.bupt.h7.models.requests.SeekPlaceDealStatisticQueryParams
import team.bupt.h7.services.SeekPlaceDealService

class SeekPlaceDealServiceImpl(private val seekPlaceDealStatisticDao: SeekPlaceDealStatisticDao) :
    SeekPlaceDealService {
    override fun querySeekPlaceDealStatistics(params: SeekPlaceDealStatisticQueryParams): List<SeekPlaceDealStatistic> {
        return seekPlaceDealStatisticDao.querySeekPlaceDealStatistics(params)
    }
}
