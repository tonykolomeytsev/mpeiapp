package kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
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
        "Небольшое обновление для вас. Лучше поздно, чем никогда — информация о посещаемости " +
                "и пропусках теперь показывается в разделе профиля в БАРС."
    ),
    Paragraph(
        "Мне всё ещё предстоит выяснить, как лучше отображать подобную информацию на экране " +
                "мобильного устройства. Текущий вариант не финальный, будут эксперименты."
    ),
    Paragraph("Впереди ещё много работы. Всем хорошей недели."),
)
