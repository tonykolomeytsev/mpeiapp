package kekmech.ru.library_app_database.api

import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * # Partial migration
 *
 * Migration interface, the implementation of which can be provided by each specific domain module.
 *
 * ### Usage:
 *
 * Create "version-specific" migration interface in `library_app_database_api`:
 * ```kotlin
 * interface MigrationV6V7 : PartialMigration
 * ```
 *
 * Implement this interface in some domain module:
 * ```kotlin
 * internal class MigrationV6V7Impl : MigrationV6V7 {
 *    override fun migrate(database: SupportSQLiteDatabase) { /* implementation */ }
 * }
 * ```
 *
 * Provide "domain-specific" + "version-specific" migration via DI:
 * ```kotlin
 * factoryOf(::MigrationV6V7Impl) bind MigrationV6V7::class
 * ```
 *
 * **Profit!**
 */
interface PartialMigration {

    fun migrate(database: SupportSQLiteDatabase)
}
