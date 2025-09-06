package kekmech.ru.lib_emoji

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

public interface EmojiProvider {
    public fun provideEmoji(emojiName: String): VectorDrawableCompat?
}
