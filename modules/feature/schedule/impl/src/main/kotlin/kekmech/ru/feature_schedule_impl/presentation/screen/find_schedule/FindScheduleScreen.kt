package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kekmech.lib_fragment.LocalResultController
import com.kekmech.lib_fragment.LocalRouter
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.DASHBOARD
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.compose.SearchSuggestions
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Ui
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.utils.GroupNumberFormatter
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.utils.adaptiveContentWidth
import kekmech.ru.lib_elm_compose.EffectHandler
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_kit_buttons.button.Button
import kekmech.ru.ui_kit_buttons.button.ButtonState
import kekmech.ru.ui_kit_textfield.textfield.TextField
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kekmech.ru.coreui.R.array as CoreUiArrays
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEffect as Effect
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent as Event
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleState as State
import kekmech.ru.res_strings.R.string as Strings

private sealed interface InputError {
    data class Loading(val query: String) : InputError
    data object Invalid : InputError
}

@Composable
internal fun FindScheduleScreen(
    onAccept: (Event) -> Unit,
    state: State,
    effects: Flow<Effect>,
) {
    val searchQuery = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var inputError by remember { mutableStateOf<InputError?>(null) }
    val facultyLabels = stringArrayResource(CoreUiArrays.faculties_labels)
    val formatter = remember(facultyLabels) { GroupNumberFormatter(facultyLabels.toList()) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val router = LocalRouter.current
    val resultController = LocalResultController.current

    val onQueryChange = { value: TextFieldValue ->
        val formattedValue = formatter.format(value)
        if (formattedValue.text != searchQuery.value.text) {
            inputError = null
            onAccept(Ui.Action.GroupNumberChanged(formattedValue.text))
        }
        searchQuery.value = formattedValue
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton { router.executeCommand(PopBackStack()) }
                },
            )
        },
        bottomBar = {
            Button(
                text = stringResource(Strings.schedule_find_button_continue),
                onClick = {
                    onAccept(Ui.Click.Continue(searchQuery.value.text))
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .adaptiveContentWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                state = if (state.isLoading) {
                    ButtonState.Loading
                } else {
                    ButtonState.Default
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .adaptiveContentWidth()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Strings.schedule_find_header),
                style = MpeixTheme.typography.headlineBig,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Strings.schedule_find_description),
                style = MpeixTheme.typography.titleSmall,
                color = MpeixTheme.palette.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))
            TextField(
                value = searchQuery.value,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = stringResource(Strings.schedule_find_hint),
                supportingText = when (val error = inputError) {
                    is InputError.Loading -> stringResource(Strings.schedule_find_error_loading, error.query)
                    is InputError.Invalid -> stringResource(Strings.schedule_find_error_invalid_input)
                    null -> null
                },
                isError = inputError != null,
                singleLine = true,
                trailingIcon = { ClearIconButton() },
            )
            Spacer(Modifier.height(16.dp))
            SearchSuggestions(
                suggestions = state.searchResults.filter { it.name != searchQuery.value.text },
                onSuggestionClick = {
                    onQueryChange(
                        TextFieldValue(
                            text = it.name,
                            selection = TextRange(it.name.length),
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    EffectHandler(effects) { effect ->
        when (effect) {
            is Effect.ShowLoadingError -> inputError = InputError.Loading(searchQuery.value.text)
            is Effect.ShowInvalidInputError -> inputError = InputError.Invalid
            is Effect.CloseScreen -> router.executeCommand(PopBackStack())
            is Effect.CloseScreenWithResult -> {
                resultController.setResult(state.resultKey, effect.selectedSchedule)
                router.executeCommand(PopBackStack())
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun FindScheduleScreenPreview() {
    MpeixPreview {
        FindScheduleScreen(
            onAccept = { /* no-op */ },
            state = State(
                resultKey = "",
                continueTo = DASHBOARD,
                isLoading = false,
                selectScheduleAfterSuccess = false,
            ),
            effects = emptyFlow(),
        )
    }
}

@Composable
@PreviewLightDark
private fun FindScheduleScreenSuggestionsPreview() {
    MpeixPreview {
        FindScheduleScreen(
            onAccept = { /* no-op */ },
            state = State(
                resultKey = "",
                continueTo = DASHBOARD,
                isLoading = false,
                selectScheduleAfterSuccess = false,
                searchResults = listOf("С-12-16", "ТФ-12-17", "А-12-18", "Сэ-12-19", "С-12-20")
                    .map { name ->
                        SearchResult(
                            id = name,
                            name = name,
                            description = "",
                            type = ScheduleType.GROUP,
                        )
                    },
            ),
            effects = emptyFlow(),
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun FindScheduleScreenTabletPreview() {
    FindScheduleScreenSuggestionsPreview()
}
