package kekmech.ru.feature_bars.screen.main.util

import android.webkit.CookieManager

private const val BARS_DOMAIN = "https://bars.mpei.ru"
private const val AUTH_COOKIE = "auth_bars"

internal fun CookieManager.hasAuthCookie() =
    getCookie(BARS_DOMAIN)?.contains(AUTH_COOKIE, ignoreCase = true) ?: false

internal fun CookieManager.removeAuthCookie() = removeAllCookies(null)
