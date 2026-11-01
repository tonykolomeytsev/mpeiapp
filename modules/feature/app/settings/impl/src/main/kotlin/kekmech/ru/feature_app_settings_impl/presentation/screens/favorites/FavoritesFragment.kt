package kekmech.ru.feature_app_settings_impl.presentation.screens.favorites

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import com.kekmech.lib_fragment.LocalRouter
import com.kekmech.lib_fragment.ResultListener
import kekmech.ru.feature_app_settings_impl.di.AppSettingDependencies
import kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite.EditFavoriteFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesEvent.Ui
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.lib_navigation.AddScreenForward
import kekmech.ru.lib_navigation.PopBackStack
import kekmech.ru.ui_theme.theme.MpeixTheme
import org.koin.android.ext.android.inject

internal class FavoritesFragment : ComposeFragment() {

    private val dependencies by inject<AppSettingDependencies>()
    private val analytics by screenAnalytics("Favorites")

    @Composable
    override fun Screen() {
        MpeixTheme {
            val router = LocalRouter.current
            ElmContent(
                key = "FavoritesScreen",
                storeFactory = { dependencies.favoritesStoreFactory.create() },
            ) { onAccept, state, _ ->
                // Group found by search screen becomes a new favorite after editing its description
                ResultListener<SelectedSchedule>(FIND_GROUP_RESULT_KEY) { selectedSchedule ->
                    router.executeCommand(AddScreenForward {
                        EditFavoriteFragment.newInstance(
                            favoriteSchedule = FavoriteSchedule(
                                name = selectedSchedule.name,
                                type = selectedSchedule.type,
                                description = "",
                                order = 0,
                            ),
                            resultKey = EDIT_FAVORITE_RESULT_KEY,
                        )
                    })
                }
                ResultListener<FavoriteSchedule>(EDIT_FAVORITE_RESULT_KEY) { favoriteSchedule ->
                    onAccept(Ui.Action.UpdateFavorite(favoriteSchedule))
                }
                FavoritesScreen(
                    favorites = state.favorites,
                    onBackClick = { router.executeCommand(PopBackStack()) },
                    onAddClick = {
                        analytics.sendClick("AddFavorite")
                        router.executeCommand(AddScreenForward {
                            dependencies.scheduleFeatureApi.getSearchGroupScreen(
                                continueTo = BACK_WITH_RESULT,
                                selectGroupAfterSuccess = false,
                                resultKey = FIND_GROUP_RESULT_KEY,
                            )
                        })
                    },
                    onFavoriteClick = { favoriteSchedule ->
                        analytics.sendClick("EditFavorite")
                        router.executeCommand(AddScreenForward {
                            EditFavoriteFragment.newInstance(
                                favoriteSchedule = favoriteSchedule,
                                resultKey = EDIT_FAVORITE_RESULT_KEY,
                            )
                        })
                    },
                    onFavoriteDelete = { favoriteSchedule ->
                        analytics.sendClick("DeleteFavorite")
                        onAccept(Ui.Click.DeleteFavorite(favoriteSchedule))
                    },
                )
            }
        }
    }

    private companion object {

        const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"

        // New and edited favorites are handled the same way, so they share the result key
        const val EDIT_FAVORITE_RESULT_KEY = "EDIT_FAVORITE_RESULT_KEY"
    }
}
