package kekmech.ru.core.usecases

import kekmech.ru.core.dto.AppVersion

interface GetAppVersionUseCase {
    operator fun invoke(): AppVersion
}