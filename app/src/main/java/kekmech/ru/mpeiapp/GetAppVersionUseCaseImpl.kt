package kekmech.ru.mpeiapp

import kekmech.ru.core.dto.AppVersion
import kekmech.ru.core.usecases.GetAppVersionUseCase

class GetAppVersionUseCaseImpl : GetAppVersionUseCase {
    override fun invoke() = AppVersion(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
}