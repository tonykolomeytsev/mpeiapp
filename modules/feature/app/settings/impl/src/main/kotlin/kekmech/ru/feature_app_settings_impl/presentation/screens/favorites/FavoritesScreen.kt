package kekmech.ru.feature_app_settings_impl.presentation.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.feature_app_settings_impl.presentation.utils.adaptiveContentWidth
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonPalette
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_icons.R.drawable as IconsDrawable
import kekmech.ru.res_strings.R.string as Strings

private val FavoriteCardShape = RoundedCornerShape(8.dp)
private const val SwipeDismissThreshold = 0.5f

// colorRed of the old XML theme, the same in light and dark themes
private val SwipeDeleteBackground = Color(0xFFDB4437)

@Composable
internal fun FavoritesScreen(
    favorites: List<FavoriteSchedule>?,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onFavoriteClick: (FavoriteSchedule) -> Unit,
    onFavoriteDelete: (FavoriteSchedule) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton(onBackClick)
                },
                title = stringResource(Strings.favorites_screen_title),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .adaptiveContentWidth(),
            contentPadding = innerPadding,
        ) {
            item {
                HelpBanner()
            }
            // Favorites are null while loading
            if (favorites != null) {
                items(favorites, key = { it.name }) { favorite ->
                    SwipeToDeleteItem(
                        onDelete = { onFavoriteDelete(favorite) },
                        modifier = Modifier
                            .animateItem()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    ) {
                        FavoriteScheduleCard(
                            favorite = favorite,
                            onClick = { onFavoriteClick(favorite) },
                        )
                    }
                }
                item {
                    Button(
                        text = stringResource(Strings.favorites_empty_state_add),
                        onClick = onAddClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        palette = ButtonPalette.Secondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun HelpBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
            .background(
                color = MpeixTheme.palette.surfaceContainer,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_lamp_idea),
            contentDescription = null,
            modifier = Modifier.size(36.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(Strings.favorites_help_title),
                style = MpeixTheme.typography.titleMedium,
                color = MpeixTheme.palette.onSurface,
            )
            Text(
                text = stringResource(Strings.favorites_help_text),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FavoriteScheduleCard(
    favorite: FavoriteSchedule,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FavoriteCardShape)
            // Same as colorGray10 of the old XML theme in both light and dark themes
            .background(MpeixTheme.palette.secondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = favorite.name,
            style = MpeixTheme.typography.bodyLarge,
            color = MpeixTheme.palette.onSurface,
        )
        if (favorite.description.isNotEmpty()) {
            Text(
                text = favorite.description,
                style = MpeixTheme.typography.labelMedium.copy(fontSize = 13.sp),
                color = MpeixTheme.palette.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

/**
 * Deletes [content] by swipe from end to start, like the old RecyclerView `SwipeToDeleteCallback`:
 * red rectangle appears only in the area left by [content], with delete icon at the end.
 */
@Composable
private fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val currentOnDelete by rememberUpdatedState(onDelete)
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) currentOnDelete()
            // Item is removed from the list by the store, so it never has to settle back
            value == SwipeToDismissBoxValue.EndToStart
        },
        // ItemTouchHelper dismisses an item swiped by half of its width
        positionalThreshold = { totalDistance -> totalDistance * SwipeDismissThreshold },
    )
    SwipeToDismissBox(
        state = state,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val iconPainter = painterResource(IconsDrawable.ic_delete_black_24)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        // Offset is negative while swiping from end to start
                        val offset = state.requireOffset().coerceAtMost(0f)
                        drawRect(
                            color = SwipeDeleteBackground,
                            topLeft = Offset(size.width + offset, 0f),
                            size = Size(-offset, size.height),
                        )
                        // Icon is drawn under the content, so it shows up only in the revealed area
                        val iconSize = iconPainter.intrinsicSize
                        val iconMargin = (size.height - iconSize.height) / 2f
                        translate(
                            left = size.width - iconMargin - iconSize.width,
                            top = iconMargin,
                        ) {
                            with(iconPainter) {
                                draw(iconSize, colorFilter = ColorFilter.tint(Color.White))
                            }
                        }
                    },
            )
        },
    ) {
        content()
    }
}

@Composable
@PreviewLightDark
private fun FavoritesScreenPreview() {
    MpeixPreview {
        FavoritesScreen(
            favorites = listOf(
                FavoriteSchedule(
                    name = "С-12м-20",
                    type = ScheduleType.GROUP,
                    description = "Моя группа",
                    order = 0,
                ),
                FavoriteSchedule(
                    name = "Иванов Иван Иванович",
                    type = ScheduleType.PERSON,
                    description = "",
                    order = 1,
                ),
            ),
            onBackClick = { /* no-op */ },
            onAddClick = { /* no-op */ },
            onFavoriteClick = { /* no-op */ },
            onFavoriteDelete = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun FavoritesScreenTabletPreview() {
    FavoritesScreenPreview()
}
