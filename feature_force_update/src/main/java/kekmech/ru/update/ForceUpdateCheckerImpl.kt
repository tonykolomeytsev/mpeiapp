package kekmech.ru.update

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.Single
import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_CURRENT_VERSION
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_UPDATE_DESCRIPTION
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_UPDATE_REQUIRED
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_UPDATE_URL
import kekmech.ru.domain_force_update.dto.AppVersion
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo

internal class ForceUpdateCheckerImpl : ForceUpdateChecker {

    override fun isNeedToUpdate(): Single<ForceUpdateInfo> = Single.create { emitter ->
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val isUpdateRequired = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)
        val actualVersion = AppVersion(remoteConfig.getString(KEY_CURRENT_VERSION))
        val appVersion = AppVersion(BuildConfig.VERSION_NAME)
        val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
        val description = remoteConfig.getString(KEY_UPDATE_DESCRIPTION)

        emitter.onSuccess(
            ForceUpdateInfo(
                actualVersion = actualVersion,
                updateUrl = updateUrl,
                shortDescription = description,
                isUpdateRequired = (actualVersion > appVersion) and isUpdateRequired
            )
        )
    }
}