package kekmech.ru.mpeiapp

import kekmech.ru.common_feature_toggles.BooleanRemoteVariable
import kekmech.ru.common_feature_toggles.RemoteVariableValueHolder

internal class ComposeEnabledFeatureToggle(valueHolder: RemoteVariableValueHolder) :
    BooleanRemoteVariable(
        name = "ft_compose_enabled",
        defaultValue = false,
        valueHolder = valueHolder,
    )
