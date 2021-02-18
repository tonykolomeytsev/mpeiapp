package kekmech.ru.common_di

import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module

open class ModuleProvider(declaration: ModuleDeclaration) {
    val provider = module(
        createdAtStart = false,
        override = false,
        moduleDeclaration = declaration
    )
}
