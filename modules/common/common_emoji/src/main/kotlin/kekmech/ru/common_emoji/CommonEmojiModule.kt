package kekmech.ru.common_emoji

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

public val CommonEmojiModule: Module = module {
    factory { EmojiProviderImpl(androidContext()) } bind EmojiProvider::class
}
