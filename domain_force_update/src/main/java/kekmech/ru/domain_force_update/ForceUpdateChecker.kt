package kekmech.ru.domain_force_update

import io.reactivex.Single
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo

interface ForceUpdateChecker {

    fun isNeedToUpdate(): Single<ForceUpdateInfo>

    companion object {
        const val KEY_UPDATE_REQUIRED = "force_update_required"
        const val KEY_CURRENT_VERSION = "force_update_version"
        const val KEY_UPDATE_URL = "force_update_url"
        const val KEY_UPDATE_DESCRIPTION = "force_update_description"
    }
}