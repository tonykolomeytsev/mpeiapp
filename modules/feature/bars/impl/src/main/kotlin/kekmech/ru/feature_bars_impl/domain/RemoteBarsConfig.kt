package kekmech.ru.feature_bars_impl.domain

import androidx.annotation.Keep

@Keep
data class RemoteBarsConfig(
    val loginUrl: String,
    val studentListUrl: String,
    val marksListUrl: String,
    val logoutUrl: String,
)
