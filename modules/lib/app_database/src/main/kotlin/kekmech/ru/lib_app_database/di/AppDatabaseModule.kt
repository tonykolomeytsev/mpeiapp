package kekmech.ru.lib_app_database.di

import androidx.room.Room
import androidx.room.migration.Migration
import kekmech.ru.lib_app_database.AppDatabase
import kekmech.ru.lib_app_database.migrations.MigrationV6V7Impl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

@Suppress("SpreadOperator")
public val LibraryAppDatabaseModule: Module = module {
    single {
        val builder = Room
            .databaseBuilder(
                context = get(),
                klass = AppDatabase::class.java,
                name = AppDatabase.Name,
            )
            .enableMultiInstanceInvalidation()
            .fallbackToDestructiveMigration(dropAllTables = true)
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
