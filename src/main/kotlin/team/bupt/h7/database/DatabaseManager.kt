package team.bupt.h7.database

import org.ktorm.database.Database

object DatabaseManager {
    val database: Database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/postgres",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
}