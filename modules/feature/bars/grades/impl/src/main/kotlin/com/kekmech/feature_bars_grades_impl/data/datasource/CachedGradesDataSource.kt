package com.kekmech.feature_bars_grades_impl.data.datasource

import com.kekmech.feature_bars_grades_api.Grades
import com.kekmech.feature_bars_grades_impl.data.dto.GradesDto
import com.kekmech.feature_bars_grades_impl.data.mapper.toDomain
import com.kekmech.feature_bars_grades_impl.data.mapper.toDto
import com.tencent.mmkv.MMKV
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

@OptIn(ExperimentalSerializationApi::class)
internal class CachedGradesDataSource(
    private val cache: MMKV = MMKV.mmkvWithID("grades_cache")
) {

    fun put(grades: Grades) {
        runCatching {
            val dto = grades.toDto()
            val bytes = Cbor.encodeToByteArray(dto)
            val semesterCode = grades.semester?.code
            if (semesterCode != null) {
                cache.encode("${SEMESTER_KEY_PREFIX}_$semesterCode", bytes)
            }
            cache.encode("${SEMESTER_KEY_PREFIX}_latest", bytes)
        }
    }

    fun get(semesterCode: String? = null): Grades? {
        return runCatching {
            val key = if (semesterCode == null) {
                "${SEMESTER_KEY_PREFIX}_latest"
            } else {
                "${SEMESTER_KEY_PREFIX}_$semesterCode"
            }
            val bytes = cache.decodeBytes(key) ?: return null
            val dto = Cbor.decodeFromByteArray<GradesDto>(bytes)
            dto.toDomain()
        }.getOrNull()
    }

    private companion object {
        private const val SEMESTER_KEY_PREFIX = "semester"
    }
}