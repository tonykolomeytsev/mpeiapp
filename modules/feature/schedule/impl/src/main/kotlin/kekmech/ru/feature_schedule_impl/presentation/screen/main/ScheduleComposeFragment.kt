package kekmech.ru.feature_schedule_impl.presentation.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.ext_android.EmptyResult
import kekmech.ru.ext_android.notNullSerializable
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.feature_app_settings_api.data.AppSettingsRepository
import kekmech.ru.feature_schedule_impl.di.ScheduleDependencies
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleComposeScreen
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.compose.koinInject

internal class ScheduleComposeFragment : ComposeFragment() {

    @Composable
    override fun Screen() {
        val appSettings = koinInject<AppSettingsRepository>()
        val darkTheme =
            remember { derivedStateOf { appSettings.getAppSettings().isDarkThemeEnabled } }
        MpeixTheme(darkTheme = darkTheme.value) {
            val factory = koinInject<ScheduleStoreFactory>()
            val dependencies = koinInject<ScheduleDependencies>()
            val activity = LocalActivity.current as ComponentActivity
            ElmContent(
                key = "ScheduleScreen",
                storeFactory = { factory.create() },
                viewModelStoreOwner = activity,
                savedStateRegistryOwner = activity,
                content = { onAccept, state, effects ->
                    ScheduleComposeScreen(
                        onAccept = onAccept,
                        state = state,
                        effects = effects,
                        onNavigateToNoteList = { effect ->
                            (parentFragment ?: this@ScheduleComposeFragment).setResultListener<EmptyResult>(
                                key = NOTES_LIST_RESULT_KEY,
                                resultMapper = Bundle::notNullSerializable,
                            ) {
                                onAccept(ScheduleEvent.Ui.Action.NotesUpdated)
                            }
                            dependencies.notesFeatureLauncher.launchNoteList(
                                selectedClasses = effect.classes,
                                selectedDate = effect.date,
                                resultKey = NOTES_LIST_RESULT_KEY,
                            )
                        },
                    )
                },
            )
        }
    }

    companion object {
        private const val NOTES_LIST_RESULT_KEY = "NOTES_LIST_RESULT_KEY"
    }
}
