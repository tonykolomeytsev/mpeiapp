package kekmech.ru.feature_bars_impl.data.mapper

import kekmech.ru.feature_bars_impl.data.model.RemoteBarsConfigDto
import kekmech.ru.feature_bars_impl.domain.RemoteBarsConfig

internal object RemoteBarsConfigMapper {

    fun dtoToDomain(dto: RemoteBarsConfigDto): RemoteBarsConfig =
        RemoteBarsConfig(
            loginUrl = dto.loginUrl,
            studentListUrl = dto.studentListUrl,
            marksListUrl = dto.marksListUrl,
            logoutUrl = dto.logoutUrl
        )
}