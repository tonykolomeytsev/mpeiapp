package kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Bullet
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Header
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Paragraph

/**
 * Building blocks of the [WhatsNewScreen] text.
 * Use [androidx.compose.ui.text.buildAnnotatedString] for bold, italic and other inline styles.
 */
@Immutable
internal sealed interface WhatsNewBlock {

    data class Header(val text: String) : WhatsNewBlock

    data class Paragraph(val text: AnnotatedString) : WhatsNewBlock {
        constructor(text: String) : this(AnnotatedString(text))
    }

    data class Bullet(val text: AnnotatedString) : WhatsNewBlock {
        constructor(text: String) : this(AnnotatedString(text))
    }

    /** Image centered horizontally, with the 32.dp padding around it */
    data class Illustration(val imageVector: ImageVector) : WhatsNewBlock
}

internal val WhatsNewContent: List<WhatsNewBlock> = listOf(
    Paragraph(
        "Последнюю неделю я в основном тратил на невидимые глазу улучшения " +
                "под капотом и избавление от технических долгов, которые накопил за несколько лет. " +
                "Это поможет мне быстрее выпускать новый функционал в будущем."
    ),

    Header("Что изменилось для вас"),
    Bullet(
        "Стали доступны расписания аудиторий. Можете делать с ними всё то же самое, " +
                "что и с остальными расписаниями."
    ),
    Bullet(
        "Дни в расписаниях теперь можно скроллить бесконечно в обе стороны. С субботы " +
                "на одной неделе можно перепрыгнуть на понедельник следующей."
    ),
    Bullet("Минорные визуальные изменения по всему приложению."),

    Header("Что изменилось под капотом"),
    Bullet(
        "Приложение полностью переехало на Jetpack Compose и стало еще ближе " +
                "к Kotlin Multi-Platform."
    ),
    Bullet("Приложение похудело с 4.3 до 3.9 мегабайт."),
    Bullet(
        "Я начал автоматизировать тестирование приложения с помощью UI-тестов. " +
                "Это должно положительно сказаться на скорости выпуска будущих обновлений. "
    ),
)
