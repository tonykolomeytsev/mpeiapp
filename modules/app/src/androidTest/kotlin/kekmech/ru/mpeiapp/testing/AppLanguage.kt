package kekmech.ru.mpeiapp.testing

import androidx.annotation.StringRes
import java.util.Locale
import kekmech.ru.res_strings.R.string as Strings

/**
 * Languages of the app, see `LanguageEntry` of the app settings
 *
 * @param code how the app stores the language in the settings
 * @param locale locale of the texts in this language
 * @param shownName how the welcome screen names the language, the same in any language
 * @param optionTitle title of the language in the language selection
 */
enum class AppLanguage(
    val code: String,
    val locale: Locale,
    val shownName: String,
    @StringRes val optionTitle: Int,
) {
    RUSSIAN(
        code = "ru_RU",
        locale = Locale.forLanguageTag("ru-RU"),
        shownName = "РУССКИЙ",
        optionTitle = Strings.change_language_description_russian,
    ),
    ENGLISH(
        code = "en_US",
        locale = Locale.forLanguageTag("en-US"),
        shownName = "ENGLISH",
        optionTitle = Strings.change_language_description_english,
    ),
}
