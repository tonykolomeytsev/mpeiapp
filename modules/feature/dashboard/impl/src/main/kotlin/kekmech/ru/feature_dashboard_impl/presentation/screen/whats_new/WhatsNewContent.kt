package kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Bullet
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Header
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Illustration
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.WhatsNewBlock.Paragraph
import kekmech.ru.feature_dashboard_impl.presentation.screen.whats_new.icon.PixelHeart

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
    Header("Всем привет!"),
    Paragraph(
        "Спасибо ребятам, которые открывали Pull Request'ы в репозитории mpeix, " +
                "и тем кто создавал форки типа МЭИ.Хаб. Ваша активность стала триггером к тому " +
                "чтобы я наконец сел и навалил базы. Т.к. меня теперь выпускают из смирительной " +
                "рубашки на два часа в день и дают интернет, это обновление стало возможным."
    ),

    Header("Что изменилось для вас"),
    Bullet(
        "Расписание теперь реально кэшируется и работает без интернета. Приложение будет " +
                "скачивать ваше расписание, и все те, что добавлены в \"Избранное\", по ночам, " +
                "на несколько недель вперед. Зачем? У некоторых из вас при включенном ВПН сервер " +
                "MpeiX становится недоступен или отвечает с огромной задержкой. Как исправить это " +
                "на более системном уровне я придумаю попозже. Пока так."
    ),
    Bullet(
        "БАРС в какой-то степени вернулся. Но мне еще предстоит вернуть просмотр во встроенном " +
                "браузере и другие фичи которых не хватает. Это самая хрупкая часть приложения, " +
                "так сложилось исторически."
    ),
    Bullet(
        "Карточки параллельных пар в расписании теперь стакаются в одну. Теперь у вас одна " +
                "лабораторная на две подгруппы, а не две лабораторных в одно время."
    ),
    Bullet(
        "Я починил баг из-за которого на китайских телефонах не работали заметки " +
                "и избранные расписания."
    ),
    Bullet("Немного изменился внешний вид приложения."),

    Header("Что изменилось под капотом"),
    Bullet(
        "Переписано 90% визуального интерфейса со старого фреймворка на Jetpack Compose. " +
                "На Fragment и View работает теперь только вкладка с расписанием, потому что я боюсь " +
                "её ломать (сломаю в следующем обновлении)."
    ),
    Bullet("Навигация в приложении заменена на Compose Navigation 3."),
    Bullet("Фактически это крупнейшее переписывание Presentation слоя за последние 5 лет."),

    Paragraph(
        "Я очень спешил с обновлением. Все недочеты, которые возникнут из-за этого массивного " +
                "перелопачивания кода, я буду исправлять опираясь на аналитику и на ваши отзывы. " +
                "Мои контакты теперь есть в настройках приложения."
    ),

    Header("Ближайшая перспектива"),
    Paragraph(
        "В ближайшие полгода, минимум раз в две недели будут выходить обновления, чтобы " +
                "адаптировать приложение к современным реалиям и расчистить беклог на 50+ новых идей, " +
                "которые были слишком дорогими в реализации в эпоху до ИИ."
    ),
    Paragraph(
        "Пока подписка на Claude Code стоит примерно как 10 хинкали с зелёным салатом, " +
                "я могу в ускоренном темпе добавлять всё, что складывал в долгий ящик " +
                "и считал \"неоправданной тратой усилий\" последние годы."
    ),
    Illustration(PixelHeart),
)
