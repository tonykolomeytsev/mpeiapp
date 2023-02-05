package kekmech.ru.feature_bars.screen.main.util

import android.webkit.CookieManager

private const val BARS_DOMAIN = "https://bars.mpei.ru"
private const val AUTH_COOKIE = "auth_bars"

fun CookieManager.hasAuthCookie() =
    getCookie(BARS_DOMAIN)?.contains(AUTH_COOKIE, ignoreCase = true) ?: false

fun CookieManager.removeAuthCookie() = removeAllCookies(null)
