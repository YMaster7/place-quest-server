package team.bupt.h7.utils

import org.ktorm.dsl.and
import org.ktorm.dsl.combineConditions
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.lessEq
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.filter
import org.ktorm.expression.ArgumentExpression
import org.ktorm.schema.BaseTable
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring

inline fun <E : Any, T : BaseTable<E>> EntitySequence<E, T>.filterWithConditions(
    block: (MutableList<ColumnDeclaring<Boolean>>) -> Unit
): EntitySequence<E, T> {
    val conditions: List<ColumnDeclaring<Boolean>> =
        ArrayList<ColumnDeclaring<Boolean>>().apply(block)
    return this.filter { conditions.combineConditions() }
}

infix fun <T : Comparable<T>> ColumnDeclaring<T>.inRange(range: Pair<T?, T?>): ColumnDeclaring<Boolean> {
    val lowerCondition =
        range.first?.let { this greaterEq it } ?: ArgumentExpression(true, BooleanSqlType)
    val upperCondition =
        range.second?.let { this lessEq it } ?: ArgumentExpression(true, BooleanSqlType)
    return lowerCondition and upperCondition
}
