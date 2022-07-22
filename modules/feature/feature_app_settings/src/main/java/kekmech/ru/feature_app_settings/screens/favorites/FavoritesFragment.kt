package kekmech.ru.feature_app_settings.screens.favorites

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.close
import kekmech.ru.common_android.setResultListener
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.common_navigation.addScreenForward
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.AddActionAdapterItem
import kekmech.ru.coreui.items.FavoriteScheduleAdapterItem
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.databinding.FragmentFavoritesBinding
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.screens.edit_favorite.EditFavoriteFragment
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEffect
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesEvent.Wish
import kekmech.ru.feature_app_settings.screens.favorites.elm.FavoritesState
import kekmech.ru.feature_app_settings.screens.favorites.item.HelpBannerAdapterItem
import kekmech.ru.strings.Strings
import org.koin.android.ext.android.inject

internal class FavoritesFragment : BaseFragment<FavoritesEvent, FavoritesEffect, FavoritesState>() {

    override val initEvent get() = Wish.Init
    override var layoutId: Int = R.layout.fragment_favorites

    private val dependencies by inject<AppSettingDependencies>()
    private val analytics by screenAnalytics("Favorites")
    private val adapter by fastLazy { createAdapter() }
    private val viewBinding by viewBinding(FragmentFavoritesBinding::bind)

    override fun createStore() = dependencies.favoritesFeatureFactory.create()

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
                feature.accept(Wish.Click.DeleteSchedule((it as FavoriteScheduleItem).value))
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
                setResultListener<String>(FIND_GROUP_RESULT_KEY) { groupName ->
                    setResultListenerForUpdateFavorite(NEW_FAVORITE_RESULT_KEY)
                    addScreenForward {
                        EditFavoriteFragment.newInstance(
                            groupNumber = groupName,
                            description = "",
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
                        groupNumber = it.value.groupNumber,
                        description = it.value.description,
                        resultKey = EDIT_FAVORITE_RESULT_KEY
                    )
                }
            },
            HelpBannerAdapterItem()
        )

    private fun setResultListenerForUpdateFavorite(resultKey: String) =
        setResultListener<Pair<String, String>>(resultKey) { (groupName, description) ->
            feature.accept(
                Wish.Action.UpdateFavorite(
                    FavoriteSchedule(groupName, description, 0)
                )
            )
        }

    companion object {

        private const val FIND_GROUP_RESULT_KEY = "FIND_GROUP_RESULT_KEY"
        private const val NEW_FAVORITE_RESULT_KEY = "NEW_FAVORITE_RESULT_KEY"
        private const val EDIT_FAVORITE_RESULT_KEY = "EDIT_FAVORITE_RESULT_KEY"
    }
}