package kekmech.ru.common_android

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat

public fun Context.getResDrawable(@DrawableRes res: Int): Drawable =
    checkNotNull(ContextCompat.getDrawable(this, res))

@ColorInt
public fun Context.getResColor(@ColorRes res: Int): Int =
    checkNotNull(ContextCompat.getColor(this, res))

@Px
public fun Context.getIntDimen(@DimenRes res: Int): Int =
    resources.getDimensionPixelSize(res)

@ColorInt
public fun Context.getThemeColor(@AttrRes attrId: Int): Int {
    val value = TypedValue()
    theme.resolveAttribute(attrId, value, true)
    return value.data
}

public fun Context.getStringArray(@ArrayRes stringRes: Int): Array<String> =
    resources.getStringArray(stringRes)

@SuppressLint("UseKtx")
public fun Context.openLinkExternal(link: String) {
    openLinkExternal(Uri.parse(link))
}

public fun Context.openLinkExternal(link: Uri) {
    startActivity(Intent(Intent.ACTION_VIEW, link))
}
