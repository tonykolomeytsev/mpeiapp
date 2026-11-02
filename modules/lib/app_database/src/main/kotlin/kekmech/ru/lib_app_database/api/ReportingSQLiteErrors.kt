package kekmech.ru.lib_app_database.api

import android.database.sqlite.SQLiteException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException

/**
 * Runs database [block] and reports any [SQLiteException] (locked, busy, can't open, corrupt, etc.)
 * to Crashlytics as a non-fatal, then rethrows it, so the caller's error handling is unchanged.
 *
 * Wrap every DAO call from repositories with it:
 * ```kotlin
 * suspend fun getAll() = reportingSQLiteErrors("favorite_schedule.getAll") { dao.getAll() }
 * ```
 *
 * @param operation short name of the database operation, sent as the `db_operation` key
 */
public suspend fun <T> reportingSQLiteErrors(operation: String, block: suspend () -> T): T =
    try {
        block()
    } catch (e: SQLiteException) {
        FirebaseCrashlytics.getInstance().recordException(e) {
            key("db_operation", operation)
        }
        throw e
    }
