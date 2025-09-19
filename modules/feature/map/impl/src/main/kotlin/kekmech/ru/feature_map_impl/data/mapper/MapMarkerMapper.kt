package kekmech.ru.feature_map_impl.data.mapper

import kekmech.ru.feature_map_api.domain.model.Location
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_api.domain.model.MarkerType
import kekmech.ru.feature_map_impl.data.model.MapMarkerDto
import kekmech.ru.feature_map_impl.data.model.MarkerTypeDto

internal object MapMarkerMapper {

    fun dtoToDomain(dtoList: List<MapMarkerDto>): List<MapMarker> =
        dtoList.map { dtoToDomain(it) }

    fun dtoToDomain(dto: MapMarkerDto): MapMarker =
        MapMarker(
            uid = dto.uid,
            address = dto.address,
            location = Location(
                lat = dto.location.lat,
                lng = dto.location.lng,
            ),
            name = dto.name,
            type = when (dto.type) {
                MarkerTypeDto.HOSTEL -> MarkerType.HOSTEL
                MarkerTypeDto.FOOD -> MarkerType.FOOD
                MarkerTypeDto.BUILDING -> MarkerType.BUILDING
                MarkerTypeDto.OTHER -> MarkerType.OTHER
                MarkerTypeDto.STRUCTURE -> MarkerType.STRUCTURE
                MarkerTypeDto.UNDEFINED -> MarkerType.UNDEFINED
            },
            icon = dto.icon,
            tag = dto.tag
        )
}