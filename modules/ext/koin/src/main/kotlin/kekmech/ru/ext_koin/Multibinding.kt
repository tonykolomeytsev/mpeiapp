package kekmech.ru.ext_koin

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.OptionDslMarker
import org.koin.core.qualifier.TypeQualifier
import org.koin.dsl.bind
import kotlin.reflect.KClass

/**
 * Add a compatible type to bind to multiple descendants of that type.
 * This feature is similar to how multibinding works in dagger.
 *
 * Usage example:
 * ```kotlin
 * // in :feature1
 * val Feature1Module = module {
 *     factoryOf(::Feature1Initializer) bindIntoList AppInitializer::class
 * }
 *
 * // in :feature2
 * val Feature2Module = module {
 *     factoryOf(::Feature2Initializer) bindIntoList AppInitializer::class
 * }
 * ```
 *
 * Next we can collect all instances of AppInitializer:
 * ```kotlin
 * // in MainActivity.kt
 * private val appInitializers: List<AppInitializer> by lazy { getKoin().getAll() }
 *
 * // or in any module, in dependency definition
 * val AppModule = module {
 *     factory { ... getAll<AppInitializer>() ... }
 * }
 * ```
 */
@OptionDslMarker
public inline infix fun <S : Any, reified T : S> KoinDefinition<T>.bindIntoList(
    clazz: KClass<S>,
): KoinDefinition<out S> {
    factory.beanDefinition.qualifier = TypeQualifier(T::class)
    return bind(clazz)
}
