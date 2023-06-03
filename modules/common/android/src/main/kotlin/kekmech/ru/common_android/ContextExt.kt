package kekmech.ru.common_android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat

fun Context.getResDrawable(@DrawableRes res: Int) = checkNotNull(ContextCompat.getDrawable(this, res))

@ColorInt
fun Context.getResColor(@ColorRes res: Int) = checkNotNull(ContextCompat.getColor(this, res))

@Px
fun Context.getIntDimen(@DimenRes res: Int) = resources.getDimensionPixelSize(res)

@ColorInt
fun Context.getThemeColor(@AttrRes attrId: Int): Int {
    val value = TypedValue()
    theme.resolveAttribute(attrId, value, true)
    return value.data
}

fun Context.getStringArray(@ArrayRes stringRes: Int): Array<String> =
    resources.getStringArray(stringRes)

fun Context.openLinkExternal(link: String) =
    openLinkExternal(Uri.parse(link))

fun Context.openLinkExternal(link: Uri) {
    startActivity(Intent(Intent.ACTION_VIEW, link))
}
