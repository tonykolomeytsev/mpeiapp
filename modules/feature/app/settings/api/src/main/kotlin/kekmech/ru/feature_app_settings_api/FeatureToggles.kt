package kekmech.ru.feature_app_settings_api

import kekmech.ru.lib_feature_toggles.BooleanRemoteVariable
import kekmech.ru.lib_feature_toggles.RemoteVariableValueHolder

public class IsSnowFlakesEnabledFeatureToggle(valueHolder: RemoteVariableValueHolder) :
    BooleanRemoteVariable(
        name = "ft_show_flakes_effect",
        defaultValue = false,
        valueHolder = valueHolder,
    )
