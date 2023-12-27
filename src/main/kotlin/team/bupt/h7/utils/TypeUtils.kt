package team.bupt.h7.utils

import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.Serializable

fun Pair<kotlinx.datetime.Instant?, kotlinx.datetime.Instant?>.toJavaInstantPair(): Pair<java.time.Instant?, java.time.Instant?> {
    return Pair(this.first?.toJavaInstant(), this.second?.toJavaInstant())
}

@Serializable
data class PageStatWrapper<T>(
    val page: Int,
    val pageSize: Int,
    val pageNum: Int,
    val data: List<T>
)