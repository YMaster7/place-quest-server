package team.bupt.h7.plugins

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import team.bupt.h7.dao.PlaceSeekerDao
import team.bupt.h7.dao.UserDao
import team.bupt.h7.dao.WelcomeOfferDao
import team.bupt.h7.database.DatabaseManager
import team.bupt.h7.services.PlaceSeekerService
import team.bupt.h7.services.UserService
import team.bupt.h7.services.WelcomeOfferService
import team.bupt.h7.services.impl.PlaceSeekerServiceImpl
import team.bupt.h7.services.impl.UserServiceImpl
import team.bupt.h7.services.impl.WelcomeOfferServiceImpl

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {
    single { DatabaseManager.create() }
    single { dotenv() }
    single { UserDao(get()) }
    single<UserService> { UserServiceImpl(get()) }
    single { PlaceSeekerDao(get()) }
    single<PlaceSeekerService> { PlaceSeekerServiceImpl(get(), get()) }
    single { WelcomeOfferDao(get()) }
    single<WelcomeOfferService> { WelcomeOfferServiceImpl(get(), get(), get()) }
}
