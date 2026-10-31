package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun SearchSuggestions(
    suggestions: List<SearchResult>,
    onSuggestionClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Disable the 48dp minimum touch target, otherwise rows are spaced too far apart
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        SearchSuggestionsFlowRow(
            suggestions = suggestions,
            onSuggestionClick = onSuggestionClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun SearchSuggestionsFlowRow(
    suggestions: List<SearchResult>,
    onSuggestionClick: (SearchResult) -> Unit,
    modifier: Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        suggestions.forEach { suggestion ->
            SuggestionChip(
                onClick = { onSuggestionClick(suggestion) },
                label = {
                    Text(
                        text = suggestion.name,
                        style = MpeixTheme.typography.titleSmall,
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MpeixTheme.palette.surfaceContainerHighest,
                    labelColor = MpeixTheme.palette.onSurface,
                ),
                shape = CircleShape,
                border = null,
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun SearchSuggestionsPreview() {
    MpeixTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            SearchSuggestions(
                suggestions = listOf("С-12-16", "С-12-17", "С-12-18", "С-12-19", "С-12-20", "С-12-21")
                    .map { name ->
                        SearchResult(
                            id = name,
                            name = name,
                            description = "",
                            type = ScheduleType.GROUP,
                        )
                    },
                onSuggestionClick = { /* no-op */ },
            )
        }
    }
}
