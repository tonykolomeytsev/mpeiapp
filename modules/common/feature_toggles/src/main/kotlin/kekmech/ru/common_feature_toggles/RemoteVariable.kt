package kekmech.ru.common_feature_toggles

/**
 * An abstraction that describes a "remote" variable, hosted in FIrebase Remote Config or otherwise.
 *
 * @see BooleanRemoteVariable
 */
sealed class RemoteVariable<T : Any>(
    val name: String,
    private val defaultValue: T,
    private val valueHolder: RemoteVariableValueHolder,
    private val deserializationStrategy: (String) -> T,
) {

    val value get() =
        valueHolder.get(name)
            ?.let(deserializationStrategy)
            ?: defaultValue
}

/**
 * An abstraction describing a "remote" Boolean variable. Usually it means feature toggle.
 *
 * Usage:
 * ```kotlin
 * // 1. Create feature toggle
 * internal class MyFeatureToggle(valueHolder: RemoteVariableValueHolder) :
 *     BooleanRemoteVariable(
 *         name = "my_feature_toggle",
 *         defaultValue = false,
 *         valueHolder = valueHolder,
 *     )
 *
 * // 2. Bind feature toggle via DI
 * factoryOf(::MyFeatureToggle) bindIntoList RemoteVariable::class
 * //                           ^ It's important to call `bindIntoList`
 * //                             if you want to overwrite toggle in debug menu
 *
 * // 3. Inject where the toggle is needed
 * internal class MyStoreFactory(private val myFeatureToggle: MyFeatureToggle) { ... }
 * ```
 *
 * @see RemoteVariable
 */
open class BooleanRemoteVariable(
    name: String,
    defaultValue: Boolean,
    valueHolder: RemoteVariableValueHolder,
) : RemoteVariable<Boolean>(
    name = name,
    defaultValue = defaultValue,
    valueHolder = valueHolder,
    deserializationStrategy = { it == "true" },
)
