package kekmech.ru.feature_app_settings_impl.presentation.screens.map_type.dto

import androidx.annotation.StringRes
import kekmech.ru.res_strings.R.string as Strings

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
