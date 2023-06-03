package kekmech.ru.common_emoji

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val CommonEmojiModule = module {
    factory { EmojiProviderImpl(androidContext()) } bind EmojiProvider::class
}
