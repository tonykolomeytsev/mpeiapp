package kekmech.ru.feature_app_settings_impl.presentation.screens.lang.dto

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.res_strings.R.string as Strings

internal enum class LanguageEntry(
    val languageCode: String,
    @StringRes val descriptionRes: Int,
    @DrawableRes val iconRes: Int,
) {

    RUSSIAN(
        languageCode = "ru_RU",
        descriptionRes = Strings.change_language_description_russian,
        iconRes = R.drawable.ic_russian
    ),
    ENGLISH(
        languageCode = "en_US",
        descriptionRes = Strings.change_language_description_english,
        iconRes = R.drawable.ic_english
    )
}
