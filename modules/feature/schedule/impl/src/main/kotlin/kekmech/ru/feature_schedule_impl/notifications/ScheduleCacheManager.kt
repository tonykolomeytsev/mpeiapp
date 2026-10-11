package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Manages caching of schedules for change detection
 */
internal class ScheduleCacheManager(
    private val context: Context
) {
    
    private val cacheDir: File
        get() = File(context.cacheDir, "schedule_changes").apply {
            if (!exists()) mkdirs()
        }
    
    /**
     * Cache schedule for later comparison
     */
    suspend fun cacheSchedule(
        name: String,
        type: ScheduleType,
        schedule: Schedule
    ) = withContext(Dispatchers.IO) {
        try {
            val cacheFile = getCacheFile(name, type)
            ObjectOutputStream(cacheFile.outputStream().buffered()).use { it.writeObject(schedule) }
            Timber.d("ScheduleCacheManager: Cached schedule for $name ($type)")
        } catch (e: Exception) {
            Timber.e(e, "ScheduleCacheManager: Failed to cache schedule")
        }
    }
    
    /**
     * Get cached schedule
     */
    suspend fun getCachedSchedule(
        name: String,
        type: ScheduleType
    ): Schedule? = withContext(Dispatchers.IO) {
        try {
            val cacheFile = getCacheFile(name, type)
            if (!cacheFile.exists()) {
                Timber.d("ScheduleCacheManager: No cached schedule for $name ($type)")
                return@withContext null
            }
            val schedule = ObjectInputStream(cacheFile.inputStream().buffered()).use {
                it.readObject() as Schedule
            }
            Timber.d("ScheduleCacheManager: Retrieved cached schedule for $name ($type)")
            schedule
        } catch (e: Exception) {
            Timber.e(e, "ScheduleCacheManager: Failed to retrieve cached schedule")
            null
        }
    }
    
    /**
     * Check if cached schedule exists
     */
    suspend fun hasCachedSchedule(
        name: String,
        type: ScheduleType
    ): Boolean = withContext(Dispatchers.IO) {
        getCacheFile(name, type).exists()
    }
    
    /**
     * Clear cache for specific schedule
     */
    suspend fun clearCache(
        name: String,
        type: ScheduleType
    ) = withContext(Dispatchers.IO) {
        try {
            val cacheFile = getCacheFile(name, type)
            if (cacheFile.exists()) {
                cacheFile.delete()
                Timber.d("ScheduleCacheManager: Cleared cache for $name ($type)")
            }
        } catch (e: Exception) {
            Timber.e(e, "ScheduleCacheManager: Failed to clear cache")
        }
    }
    
    /**
     * Clear all cached schedules
     */
    suspend fun clearAllCache() = withContext(Dispatchers.IO) {
        try {
            cacheDir.deleteRecursively()
            cacheDir.mkdirs()
            Timber.d("ScheduleCacheManager: Cleared all cache")
        } catch (e: Exception) {
            Timber.e(e, "ScheduleCacheManager: Failed to clear all cache")
        }
    }
    
    /**
     * Get cache file for schedule
     */
    private fun getCacheFile(name: String, type: ScheduleType): File {
        val sanitizedName = name.replace(Regex("[^\\p{L}\\p{N}_-]"), "_")
        val fileName = "${type.pathName}_${sanitizedName}.bin"
        return File(cacheDir, fileName)
    }
}
