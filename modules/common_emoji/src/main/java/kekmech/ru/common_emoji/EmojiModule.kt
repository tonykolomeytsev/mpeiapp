package kekmech.ru.common_emoji

import kekmech.ru.common_di.ModuleProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind

object EmojiModule : ModuleProvider({
    factory { EmojiProviderImpl(androidContext()) } bind EmojiProvider::class
})