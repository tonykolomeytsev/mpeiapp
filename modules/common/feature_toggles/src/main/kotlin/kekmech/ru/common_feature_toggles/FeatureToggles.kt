package kekmech.ru.common_feature_toggles

@Deprecated("Use `BooleanRemoteVariable` instead")
interface FeatureToggles {
    val isWorkInProgressBannerEnabled: Boolean
    val isSnowFlakesEnabled: Boolean
}
