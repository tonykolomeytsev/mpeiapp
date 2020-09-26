package kekmech.ru.common_android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.annotation.*
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

@ColorInt
fun Context.getThemeAccentColor() = getThemeColor(R.attr.colorAccent)

@ColorInt
fun Context.getThemeSurfaceColor() = getThemeColor(R.attr.colorSurface)

fun Context.getStringArray(@ArrayRes stringRes: Int): Array<String> =
    resources.getStringArray(stringRes)

fun Context.openLinkExternal(link: String) =
    openLinkExternal(Uri.parse(link))

fun Context.openLinkExternal(link: Uri) {
    startActivity(Intent(Intent.ACTION_VIEW, link))
}

fun Context.getRawText(@RawRes rawResId: Int) =
    resources.openRawResource(rawResId).readBytes().decodeToString()