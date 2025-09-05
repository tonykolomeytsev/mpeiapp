package kekmech.ru.common_app_database.di

import androidx.room.Room
import androidx.room.migration.Migration
import kekmech.ru.common_app_database.AppDatabase
import kekmech.ru.common_app_database.migrations.MigrationV6V7Impl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

@Suppress("SpreadOperator")
public val CommonAppDatabaseModule: Module = module {
    single {
        val builder = Room
            .databaseBuilder(
                context = androidContext(),
                klass = AppDatabase::class.java,
                name = AppDatabase.Name,
            )
        // collect all partial migrations from all domain modules
        // in order to run them all further
        builder.addMigrations(*getAll<Migration>().toTypedArray())
        builder.build()
    }

    // DAOs
    factory { get<AppDatabase>().favoriteScheduleDao() }
    factory { get<AppDatabase>().noteDao() }

    // Migrations
    single { MigrationV6V7Impl(getAll()) } bind Migration::class
}
