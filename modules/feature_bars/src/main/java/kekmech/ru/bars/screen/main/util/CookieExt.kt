package kekmech.ru.bars.screen.main.util

import android.webkit.CookieManager

private const val BARS_DOMAIN = "https://bars.mpei.ru"
private const val AUTH_COOKIE = "auth_bars"

fun CookieManager.hasAuthCookie() =
    getCookie(BARS_DOMAIN).contains(AUTH_COOKIE, ignoreCase = true)