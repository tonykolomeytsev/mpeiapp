package kekmech.ru.core

import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module

abstract class ModuleProvider(private val declaration: ModuleDeclaration) {
    val provider = module(
        createdAtStart = false,
        override = false,
        moduleDeclaration = declaration
    )
}