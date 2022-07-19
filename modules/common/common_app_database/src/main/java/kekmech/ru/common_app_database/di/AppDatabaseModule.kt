package kekmech.ru.common_app_database.di

import kekmech.ru.common_app_database.AppDatabase
import kekmech.ru.common_app_database.AppDatabaseImpl
import kekmech.ru.common_app_database.helpers.DBHelper
import kekmech.ru.common_di.ModuleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind

object AppDatabaseModule : ModuleProvider({
    single { AppDatabaseImpl(get()) } bind AppDatabase::class
    single { DBHelper(androidContext(), AppDatabase.NAME, AppDatabase.VERSION) } bind DBHelper::class
})