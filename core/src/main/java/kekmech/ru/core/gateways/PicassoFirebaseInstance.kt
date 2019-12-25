package kekmech.ru.core.gateways

import android.widget.ImageView

interface PicassoFirebaseInstance {
    fun load(link: String, imageView: ImageView)
}