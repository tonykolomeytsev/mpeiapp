package kekmech.ru.common_feature_toggles

internal class FeatureTogglesImpl(
    private val wrapper: RemoteConfigWrapper,
) : FeatureToggles {

    override val isWorkInProgressBannerEnabled get() = wrapper["ft_banner_work_in_progress"]

    override val isSnowFlakesEnabled get() = wrapper["ft_show_flakes_effect"]
}
