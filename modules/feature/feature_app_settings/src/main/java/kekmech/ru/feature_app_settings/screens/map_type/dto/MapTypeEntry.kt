package kekmech.ru.feature_app_settings.screens.map_type.dto

import androidx.annotation.StringRes
import kekmech.ru.feature_app_settings.R

internal enum class MapTypeEntry(
    val mapTypeCode: String,
    @StringRes val descriptionRes: Int
) {

    HYBRID(
        mapTypeCode = "hybrid",
        descriptionRes = R.string.change_map_type_hybrid,
    ),

    SCHEME(
        mapTypeCode = "scheme",
        descriptionRes = R.string.change_map_type_scheme,
    )
}