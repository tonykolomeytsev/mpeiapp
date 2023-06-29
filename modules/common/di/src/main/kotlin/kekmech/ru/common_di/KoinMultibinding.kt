package kekmech.ru.common_di

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.OptionDslMarker
import org.koin.core.qualifier.TypeQualifier
import org.koin.dsl.bind
import kotlin.reflect.KClass

@OptionDslMarker
inline infix fun <S : Any, reified T : S> KoinDefinition<T>.bindIntoList(clazz: KClass<S>): KoinDefinition<out S> {
    factory.beanDefinition.qualifier = TypeQualifier(T::class)
    return bind(clazz)
}
