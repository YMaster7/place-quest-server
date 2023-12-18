package team.bupt.h7.plugins

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import team.bupt.h7.database.DatabaseManager
import team.bupt.h7.services.UserService
import team.bupt.h7.services.UserServiceImpl

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {
    single { DatabaseManager.database }
    single<UserService> { UserServiceImpl(get()) }
    single<Dotenv> { dotenv() }
}
