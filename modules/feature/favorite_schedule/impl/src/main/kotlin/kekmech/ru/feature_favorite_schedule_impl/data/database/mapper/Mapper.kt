package kekmech.ru.feature_favorite_schedule_impl.data.database.mapper

import kekmech.ru.ext_kotlin.fromBase64
import kekmech.ru.ext_kotlin.toBase64
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.lib_app_database.entity.NormalFavoriteSchedule

internal fun NormalFavoriteSchedule.toDomain(): FavoriteSchedule =
    FavoriteSchedule(
        name = name,
        type = type.toDomain(),
        description = description.fromBase64(),
        order = order,
    )

internal fun FavoriteSchedule.toNormal(): NormalFavoriteSchedule =
    NormalFavoriteSchedule(
        name = name,
        type = type.toEntity(),
        description = description.toBase64(),
        order = order,
    )

private fun String.toDomain(): ScheduleType =
    ScheduleType.values().first { it.name.equals(this, ignoreCase = true) }

private fun ScheduleType.toEntity(): String = name
