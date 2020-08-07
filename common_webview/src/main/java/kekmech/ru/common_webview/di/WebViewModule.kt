package kekmech.ru.common_webview.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_webview.presentation.WebViewFeatureFactory
import org.koin.dsl.bind

object WebViewModule : ModuleProvider({
    single { WebViewFeatureFactory } bind WebViewFeatureFactory::class
})