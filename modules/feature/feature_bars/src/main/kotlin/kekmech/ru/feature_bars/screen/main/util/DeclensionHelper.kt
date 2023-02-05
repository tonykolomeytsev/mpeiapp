package kekmech.ru.feature_bars.screen.main.util

internal object DeclensionHelper {

    private const val DECLENSION_5_TO_10 = 2
    private const val DECLENSION_2_TO_4 = 1
    private const val DECLENSION_1 = 0

    @Suppress("MagicNumber")
    fun format(declensions: Array<String>, n: Long): String {
        if (n in 11L..19L) return "$n " + declensions[DECLENSION_5_TO_10]
        return "$n " + when (n % 10L) {
            1L -> declensions[DECLENSION_1]
            in 2L..4L -> declensions[DECLENSION_2_TO_4]
            else -> declensions[DECLENSION_5_TO_10]
        }
    }
}
