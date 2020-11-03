package kekmech.ru.common_emoji

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

internal class EmojiProviderImpl(private val context: Context) : EmojiProvider {

    override fun provideEmoji(emojiName: String): VectorDrawableCompat? = when (emojiName) {
        ":swimming_pool:" -> getFromRes(R.drawable.ic_emoji_swimming_pool)
        ":library:" -> getFromRes(R.drawable.ic_emoji_library)
        ":masks:" -> getFromRes(R.drawable.ic_emoji_masks)
        ":block:" -> getFromRes(R.drawable.ic_emoji_block)
        ":pistol:" -> getFromRes(R.drawable.ic_emoji_pistol)
        ":sachok:" -> getFromRes(R.drawable.ic_emoji_sachok)
        ":sputnik:" -> getFromRes(R.drawable.ic_emoji_sputnik)
        ":force:" -> getFromRes(R.drawable.ic_emoji_force)
        ":stadium:" -> getFromRes(R.drawable.ic_emoji_stadium)
        ":hostels:" -> getFromRes(R.drawable.ic_emoji_hostels)
        ":massage:" -> getFromRes(R.drawable.ic_emoji_massage)

        else -> null
    }

    private fun getFromRes(@DrawableRes resId: Int) = VectorDrawableCompat.create(context.resources, resId, null)
}