package kekmech.ru.feature_app_settings_impl.presentation.screens.favorites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.AddActionAdapterItem
import kekmech.ru.coreui.items.FavoriteScheduleAdapterItem
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.ext_android.addSystemBottomPadding
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.setResultListener
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_settings_impl.R
import kekmech.ru.feature_app_settings_impl.databinding.FragmentFavoritesBinding
import kekmech.ru.feature_app_settings_impl.di.AppSettingDependencies
import kekmech.ru.feature_app_settings_impl.presentation.screens.edit_favorite.EditFavoriteFragment
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesEffect
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesEvent
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesEvent.Ui
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.elm.FavoritesState
import kekmech.ru.feature_app_settings_impl.presentation.screens.favorites.item.HelpBannerAdapterItem
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.lib_adapter.BaseAdapter
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm.BaseFragment
import kekmech.ru.lib_navigation.addScreenForward
import org.koin.android.ext.android.inject
import kekmech.ru.res_strings.R.string as Strings

internal class FavoritesFragment :
    BaseFragment<FavoritesEvent, FavoritesEffect, FavoritesState>(R.layout.fragment_favorites) {

    private val dependencies by inject<AppSettingDependencies>()
    private val analytics by screenAnalytics("Favorites")
    private val adapter by fastLazy { createAdapter() }
    private val viewBinding by viewBinding(FragmentFavoritesBinding::bind)

    override fun createStore() = dependencies.favoritesStoreFactory.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            appBarLayout.addSystemTopPadding()

            toolbar.setTitle(Strings.favorites_screen_title)
            toolbar.setNavigationOnClickListener { close() }
            recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
            recyclerView.addSystemBottomPadding()
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            recyclerView.attachSwipeToDeleteCallback(isItemForDelete = { it is FavoriteScheduleItem }) {
                analytics.sendClick("DeleteFavorite")
                store.accept(Ui.Click.DeleteFavorite((it as FavoriteScheduleItem).value))
            }
        }
    }

    override fun render(state: FavoritesState) {
        adapter.update(FavoritesListConverter(requireContext()).map(state))
    }

    private fun createAdapter() =
        BaseAdapter(
            SpaceAdapterItem(),
            AddActionAdapterItem {
                analytics.sendClick("AddFavorite")
                setResultListener<SelectedSchedule>(FIND_GROUP_RESULT_KEY) { selectedSchedule ->
                    setResultListenerForUpdateFavorite(NEW_FAVORITE_RESULT_KEY)
                    addScreenForward {
                        EditFavoriteFragment.newInstance(
                            favoriteSchedule = FavoriteSchedule(
                                name = selectedSchedule.name,
                                type = selectedSchedule.type,
                                description = "",
                                order = 0,
                            ),
                            resultKey = NEW_FAVORITE_RESULT_KEY
                        )
                    }
                }
                dependencies.scheduleFeatureLauncher.launchSearchGroup(
                    continueTo = BACK_WITH_RESULT,
                    selectGroupAfterSuccess = false,
                    resultKey = FIND_GROUP_RESULT_KEY
                )
            },
            FavoriteScheduleAdapterItem {
                analytics.sendClick("EditFavorite")
                setResultListenerForUpdateFavorite(EDIT_FAVORITE_RESULT_KEY)
                addScreenForward {
                    EditFavoriteFragment.newInstance(
                        favoriteSchedule = it.value,
                        resultKey = EDIT_FAVORITE_RESULT_KEY,
                    )
                }
            },
            HelpBannerAdapterItem()
        )

    private fun setResultListenerForUpdateFavorite(resultKey: String) =
        setResultListener<FavoriteSchedule>(resultKey) { favoriteSchedule ->
            store.accept(Ui.Action.UpdateFavorite(favoriteSchedule))
        }

    companion object {

        private const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"
        private const val NEW_FAVORITE_RESULT_KEY = "NEW_FAVORITE_RESULT_KEY"
        private const val EDIT_FAVORITE_RESULT_KEY = "EDIT_FAVORITE_RESULT_KEY"
    }
}
