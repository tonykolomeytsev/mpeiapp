package kekmech.ru.feature_app_update_impl

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.feature_app_update_api.ForceUpdateChecker
import kekmech.ru.feature_app_update_api.ForceUpdateChecker.Companion.KEY_CURRENT_VERSION
import kekmech.ru.feature_app_update_api.ForceUpdateChecker.Companion.KEY_MIN_REQUIRED_VERSION
import kekmech.ru.feature_app_update_api.ForceUpdateChecker.Companion.KEY_UPDATE_DESCRIPTION
import kekmech.ru.feature_app_update_api.ForceUpdateChecker.Companion.KEY_UPDATE_REQUIRED
import kekmech.ru.feature_app_update_api.ForceUpdateChecker.Companion.KEY_UPDATE_URL
import kekmech.ru.feature_app_update_impl.domain.models.AppVersion
import kekmech.ru.feature_app_update_impl.domain.models.ForceUpdateInfo
import kekmech.ru.feature_app_update_impl.presentation.BlockingUpdateFragment
import kekmech.ru.feature_app_update_impl.presentation.ForceUpdateFragment
import kekmech.ru.library_app_info.AppVersionName
import kekmech.ru.library_navigation.NewRoot
import kekmech.ru.library_navigation.Router
import kekmech.ru.library_navigation.ShowDialog

internal class ForceUpdateCheckerImpl(
    private val router: Router,
    private val appVersionName: AppVersionName,
) : ForceUpdateChecker {

    override fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val isUpdateRequired = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)
        val actualVersion = AppVersion(remoteConfig.getString(KEY_CURRENT_VERSION))
        val appVersion = AppVersion(appVersionName.versionName)
        val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
        val description = remoteConfig.getString(KEY_UPDATE_DESCRIPTION)
        val minRequiredVersion = AppVersion(remoteConfig.getString(KEY_MIN_REQUIRED_VERSION))

        val forceUpdateInfo = ForceUpdateInfo(
            actualVersion = actualVersion,
            updateUrl = updateUrl,
            shortDescription = description,
            isUpdateRequired = (actualVersion > appVersion) and isUpdateRequired
        )
        if (appVersion < minRequiredVersion) {
            router.executeCommand(NewRoot { BlockingUpdateFragment.newInstance(forceUpdateInfo) })
        } else if (forceUpdateInfo.isUpdateRequired) {
            router.executeCommand(ShowDialog {
                ForceUpdateFragment.newInstance(forceUpdateInfo)
            })
        }
    }
}
