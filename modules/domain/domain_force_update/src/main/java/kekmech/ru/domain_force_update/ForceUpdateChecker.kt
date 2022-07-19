package kekmech.ru.domain_force_update

interface ForceUpdateChecker {

    fun check()

    companion object {
        const val KEY_UPDATE_REQUIRED = "force_update_required"
        const val KEY_CURRENT_VERSION = "force_update_version"
        const val KEY_UPDATE_URL = "force_update_url"
        const val KEY_UPDATE_DESCRIPTION = "force_update_description"
        const val KEY_MIN_REQUIRED_VERSION = "force_update_min_version"
    }
}