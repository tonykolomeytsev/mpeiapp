package kekmech.ru.feature_schedule_impl.data.mapper

import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_schedule_impl.data.model.ScheduleTypeDto
import kekmech.ru.feature_schedule_impl.data.model.SearchResultDto

internal object SearchResultMapper {

    fun dtoToDomain(dtos: List<SearchResultDto>): List<SearchResult> =
        dtos.map { dtoToDomain(it) }

    fun dtoToDomain(dto: SearchResultDto): SearchResult =
        SearchResult(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            type = when (dto.type) {
                ScheduleTypeDto.GROUP -> ScheduleType.GROUP
                ScheduleTypeDto.PERSON -> ScheduleType.PERSON
            }
        )
}