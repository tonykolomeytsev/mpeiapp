package kekmech.ru.feature_app_settings_api

import kekmech.ru.library_feature_toggles.BooleanRemoteVariable
import kekmech.ru.library_feature_toggles.RemoteVariableValueHolder

class IsSnowFlakesEnabledFeatureToggle(valueHolder: RemoteVariableValueHolder) :
    BooleanRemoteVariable(
        name = "ft_show_flakes_effect",
        defaultValue = false,
        valueHolder = valueHolder,
    )
