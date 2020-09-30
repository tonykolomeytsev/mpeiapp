package kekmech.ru.common_emoji

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

interface EmojiProvider {
    fun provideEmoji(emojiName: String): VectorDrawableCompat?
}