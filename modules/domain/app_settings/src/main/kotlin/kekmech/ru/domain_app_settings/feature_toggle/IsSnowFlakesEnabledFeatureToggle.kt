package kekmech.ru.domain_app_settings.feature_toggle

import kekmech.ru.common_feature_toggles.BooleanRemoteVariable
import kekmech.ru.common_feature_toggles.RemoteVariableValueHolder

class IsSnowFlakesEnabledFeatureToggle(valueHolder: RemoteVariableValueHolder) :
    BooleanRemoteVariable(
        name = "ft_show_flakes_effect",
        defaultValue = false,
        valueHolder = valueHolder,
    )
