package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.feature_schedule_ui.compose.icon.ArtistPalette
import kekmech.ru.feature_schedule_ui.compose.icon.Bookmark
import kekmech.ru.feature_schedule_ui.compose.icon.Books
import kekmech.ru.feature_schedule_ui.compose.icon.Burrito
import kekmech.ru.feature_schedule_ui.compose.icon.ConfettiBall
import kekmech.ru.feature_schedule_ui.compose.icon.Eggplant
import kekmech.ru.feature_schedule_ui.compose.icon.Guitar
import kekmech.ru.feature_schedule_ui.compose.icon.Laptop
import kekmech.ru.feature_schedule_ui.compose.icon.PartyPopper
import kekmech.ru.feature_schedule_ui.compose.icon.Pencil
import kekmech.ru.feature_schedule_ui.compose.icon.Pineapple
import kekmech.ru.feature_schedule_ui.compose.icon.SoccerBall
import kekmech.ru.feature_schedule_ui.compose.icon.StraightRuler
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import kotlin.random.Random
import kekmech.ru.res_strings.R.string as Strings

/**
 * # SelfStudyItem
 *
 * Placeholder of the day without classes: a row of three small illustrations,
 * title and description, centered vertically in the available space.
 *
 * The illustrations are three distinct ones picked randomly from [SelfStudyIllustrations]
 * by the [seed], so the same day always gets the same combination (a little BINGO).
 *
 * Replaces the `SelfStudyAdapterItem` of the RecyclerView implementation.
 *
 * @param seed seed of the illustrations choice, e.g. [LocalDate.toEpochDay] of the day
 * @param modifier [Modifier] to be applied to the item, it defines the size of the item
 */
@Composable
public fun SelfStudyItem(seed: Long, modifier: Modifier = Modifier) {
    val illustrations = remember(seed) {
        SelfStudyIllustrations
            .shuffled(Random(seed))
            .take(SELF_STUDY_ILLUSTRATIONS_COUNT)
            .map { it.invoke() }
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            illustrations.forEach { illustration ->
                Image(
                    imageVector = illustration,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Strings.schedule_item_self_study_header),
            style = MpeixTheme.typography.headlineSmall,
            color = MpeixTheme.palette.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Strings.schedule_item_self_study_description),
            style = MpeixTheme.typography.bodyLarge,
            fontSize = 16.sp,
            color = MpeixTheme.palette.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

private const val SELF_STUDY_ILLUSTRATIONS_COUNT = 3

/**
 * Twemoji illustrations of the [SelfStudyItem]. Lambdas, so that only the picked vectors are built.
 * Append new ones to the end: reordering changes the illustrations of the days already shown.
 */
private val SelfStudyIllustrations: List<() -> ImageVector> = listOf(
    { Books },
    { Laptop },
    { Pencil },
    { StraightRuler },
    { Bookmark },
    { ConfettiBall },
    { PartyPopper },
    { Pineapple },
    { Eggplant },
    { ArtistPalette },
    { Burrito },
    { Guitar },
    { SoccerBall },
)

@PreviewLightDark
@Composable
private fun SelfStudyItemPreview() {
    MpeixTheme {
        SelfStudyItem(
            seed = LocalDate.of(2026, 11, 17).toEpochDay(),
            modifier = Modifier
                .fillMaxSize()
                .background(MpeixTheme.palette.surface),
        )
    }
}
