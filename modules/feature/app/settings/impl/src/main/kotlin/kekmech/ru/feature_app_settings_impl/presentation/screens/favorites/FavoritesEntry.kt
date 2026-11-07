package kekmech.ru.feature_app_settings_impl.presentation.screens.favorites

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.feature_app_settings_impl.di.AppSettingDependencies
import kekmech.ru.feature_app_settings_impl.navigation.EditFavoriteScreenKey
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesEvent.Ui
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.lib_analytics_android.ext.rememberScreenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import org.koin.compose.koinInject

private const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"

// New and edited favorites are handled the same way, so they share the result key
private const val EDIT_FAVORITE_RESULT_KEY = "EDIT_FAVORITE_RESULT_KEY"

@Composable
internal fun FavoritesEntry() {
    val dependencies = koinInject<AppSettingDependencies>()
    val analytics = rememberScreenAnalytics("Favorites")
    val router = LocalRouter.current
    ElmContent(
        key = "FavoritesScreen",
        storeFactory = { dependencies.favoritesStoreFactory.create() },
    ) { onAccept, state, _ ->
        // Group found by search screen becomes a new favorite after editing its description
        ResultListener<SelectedSchedule>(FIND_GROUP_RESULT_KEY) { selectedSchedule ->
            router.executeCommand(AddScreenForward(
                EditFavoriteScreenKey(
                    favoriteSchedule = FavoriteSchedule(
                        name = selectedSchedule.name,
                        type = selectedSchedule.type,
                        description = "",
                        order = 0,
                    ),
                    resultKey = EDIT_FAVORITE_RESULT_KEY,
                )
            ))
        }
        ResultListener<FavoriteSchedule>(EDIT_FAVORITE_RESULT_KEY) { favoriteSchedule ->
            onAccept(Ui.Action.UpdateFavorite(favoriteSchedule))
        }
        FavoritesScreen(
            favorites = state.favorites,
            onBackClick = { router.executeCommand(PopBackStack()) },
            onAddClick = {
                analytics.sendClick("AddFavorite")
                dependencies.scheduleFeatureApi.launchSearchGroup(
                    continueTo = BACK_WITH_RESULT,
                    selectGroupAfterSuccess = false,
                    resultKey = FIND_GROUP_RESULT_KEY,
                )
            },
            onFavoriteClick = { favoriteSchedule ->
                analytics.sendClick("EditFavorite")
                router.executeCommand(AddScreenForward(
                    EditFavoriteScreenKey(
                        favoriteSchedule = favoriteSchedule,
                        resultKey = EDIT_FAVORITE_RESULT_KEY,
                    )
                ))
            },
            onFavoriteDelete = { favoriteSchedule ->
                analytics.sendClick("DeleteFavorite")
                onAccept(Ui.Click.DeleteFavorite(favoriteSchedule))
            },
        )
    }
}

