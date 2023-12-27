package team.bupt.h7.dao

import org.ktorm.database.Database

class TransactionDao(private val database: Database) {
    fun <T> transaction(block: () -> T): T {
        return database.useTransaction {
            block()
        }
    }
}
