package kekmech.ru.feature_app_update_api

public interface ForceUpdateChecker {

    public fun check()

    public companion object {
        public const val KEY_UPDATE_REQUIRED: String = "force_update_required"
        public const val KEY_CURRENT_VERSION: String = "force_update_version"
        public const val KEY_UPDATE_URL: String = "force_update_url"
        public const val KEY_UPDATE_DESCRIPTION: String = "force_update_description"
        public const val KEY_MIN_REQUIRED_VERSION: String = "force_update_min_version"
    }
}
