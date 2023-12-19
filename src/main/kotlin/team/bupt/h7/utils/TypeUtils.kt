package team.bupt.h7.utils

import kotlinx.datetime.toJavaInstant

fun Pair<kotlinx.datetime.Instant?, kotlinx.datetime.Instant?>.toJavaInstantPair(): Pair<java.time.Instant?, java.time.Instant?> {
    return Pair(this.first?.toJavaInstant(), this.second?.toJavaInstant())
}