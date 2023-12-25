package team.bupt.h7.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import team.bupt.h7.dao.*
import team.bupt.h7.database.DatabaseManager
import team.bupt.h7.services.*
import team.bupt.h7.services.impl.*

fun Application.configureDependencyInjection() {
    val appModule = module {
        single { environment }
        single { DatabaseManager.create() }
        single { UserDao(get()) }
        single<UserService> { UserServiceImpl(get()) }
        single { PlaceSeekerDao(get()) }
        single<PlaceSeekerService> { PlaceSeekerServiceImpl(get(), get(), get()) }
        single { WelcomeOfferDao(get()) }
        single<WelcomeOfferService> { WelcomeOfferServiceImpl(get(), get(), get(), get()) }
        single { SeekPlaceDealDao(get()) }
        single { SeekPlaceDealStatisticDao(get()) }
        single<SeekPlaceDealService> { SeekPlaceDealServiceImpl(get()) }
        single<AuthService> { AuthServiceImpl(get()) }
    }

    install(Koin) {
        modules(appModule)
    }
}
