package kekmech.ru.update

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kekmech.ru.common_navigation.NewRoot
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.ShowDialog
import kekmech.ru.domain_force_update.ForceUpdateChecker
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_CURRENT_VERSION
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_MIN_REQUIRED_VERSION
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_UPDATE_DESCRIPTION
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_UPDATE_REQUIRED
import kekmech.ru.domain_force_update.ForceUpdateChecker.Companion.KEY_UPDATE_URL
import kekmech.ru.domain_force_update.dto.AppVersion
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo

internal class ForceUpdateCheckerImpl(
    private val router: Router
) : ForceUpdateChecker {

    override fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()

        val isUpdateRequired = remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)
        val actualVersion = AppVersion(remoteConfig.getString(KEY_CURRENT_VERSION))
        val appVersion = AppVersion(BuildConfig.VERSION_NAME)
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