package kekmech.ru.feature_app_settings.screens.map_type.dto

import androidx.annotation.StringRes
import kekmech.ru.feature_app_settings.R
import kekmech.ru.strings.Strings

internal enum class MapTypeEntry(
    val mapTypeCode: String,
    @StringRes val descriptionRes: Int
) {

    HYBRID(
        mapTypeCode = "hybrid",
        descriptionRes = Strings.change_map_type_hybrid,
    ),

    SCHEME(
        mapTypeCode = "scheme",
        descriptionRes = Strings.change_map_type_scheme,
    )
}
