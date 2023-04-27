package kekmech.ru.common_app_database.di

import kekmech.ru.common_app_database.AppDatabase
import kekmech.ru.common_app_database.AppDatabaseImpl
import kekmech.ru.common_app_database.helpers.DBHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CommonAppDatabaseModule = module {
    singleOf(::AppDatabaseImpl) bind AppDatabase::class
    single {
        DBHelper(
            context = androidContext(),
            databaseName = AppDatabase.NAME,
            databaseVersion = AppDatabase.VERSION,
        )
    } bind DBHelper::class
}
