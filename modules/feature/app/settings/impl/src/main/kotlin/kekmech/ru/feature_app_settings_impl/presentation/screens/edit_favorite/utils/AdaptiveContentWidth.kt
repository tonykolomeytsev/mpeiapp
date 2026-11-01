package kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Max width of single-column content, matches compact -> medium window size class breakpoint.
 */
internal val ContentMaxWidth: Dp = 600.dp

/**
 * Fills available width on phones, but on wide screens (tablets, landscape, foldables)
 * limits the element to [maxWidth] and centers it horizontally.
 *
 * Apply it before content paddings, so the paddings stay inside the limited area.
 */
internal fun Modifier.adaptiveContentWidth(maxWidth: Dp = ContentMaxWidth): Modifier =
    this
        .fillMaxWidth()
        .wrapContentWidth(Alignment.CenterHorizontally)
        .widthIn(max = maxWidth)
        .fillMaxWidth()
