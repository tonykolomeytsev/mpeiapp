package kekmech.ru.repository.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.core.gateways.UserCacheGateway
import kekmech.ru.core.repositories.*
import kekmech.ru.repository.*
import kekmech.ru.repository.auth.BaseKeyStore
import kekmech.ru.repository.auth.BaseKeyStoreV21
import kekmech.ru.repository.gateways.UserCacheGatewayImpl
import kekmech.ru.repository.room.AppDatabase
import org.koin.dsl.bind

object RepositoryModule : ModuleProvider({
    // repos
    single { ScheduleRepositoryImpl(get(), get()) } bind ScheduleRepository::class
    single { UserRepositoryImpl(get(), get()) } bind UserRepository::class
    single { PlacesRepositoryImpl() } bind PlacesRepository::class
    single { BarsRepositoryImpl(get(), get()) } bind BarsRepository::class
    single { NotesRepositoryImpl(get()) } bind NotesRepository::class
    single { FeedRepositoryImpl(get()) } bind FeedRepository::class

    // additional dependencies
    single { AppDatabaseModule.provideAppDatabase(get()) } bind AppDatabase::class
    single { BaseKeyStoreV21() } bind BaseKeyStore::class
    single { UserCacheGatewayImpl(get()) } bind UserCacheGateway::class
})