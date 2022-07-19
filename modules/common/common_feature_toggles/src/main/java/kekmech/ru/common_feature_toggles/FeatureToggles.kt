package kekmech.ru.common_feature_toggles

interface FeatureToggles {
    val isWorkInProgressBannerEnabled: Boolean
    val isSnowFlakesEnabled: Boolean
}