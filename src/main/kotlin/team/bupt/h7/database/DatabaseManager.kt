package team.bupt.h7.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database

object DatabaseManager {
    fun create(): Database {
        val dbHost = System.getenv("DB_HOST") ?: "localhost"
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://${dbHost}:5432/postgres"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "postgres"
            validate()
        }
        val dataSource = HikariDataSource(hikariConfig)
        return Database.connect(dataSource)
    }
}
