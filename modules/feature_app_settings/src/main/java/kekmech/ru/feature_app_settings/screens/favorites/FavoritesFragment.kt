package kekmech.ru.feature_app_settings.screens.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.ActivityResultListener
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withResultFor
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
import org.koin.android.ext.android.inject

private const val REQUEST_NEW_FAVORITE = 312344
private const val REQUEST_EDIT_FAVORITE = 312345

internal class FavoritesFragment :
    BaseFragment<FavoritesEvent, FavoritesEffect, FavoritesState>(),
    ActivityResultListener {

    override val initEvent get() = Wish.Init
    override var layoutId: Int = R.layout.fragment_favorites

    private val dependencies by inject<AppSettingDependencies>()
    private val analytics by screenAnalytics("Favorites")
    private val adapter by fastLazy { createAdapter() }
    private val viewBinding by viewBinding(FragmentFavoritesBinding::bind)

    override fun createStore() = dependencies.favoritesFeatureFactory.create()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.addSystemTopPadding()
        viewBinding.apply {
            toolbar.init(R.string.favorites_screen_title)
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

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        AddActionAdapterItem {
            analytics.sendClick("AddFavorite")
            dependencies.scheduleFeatureLauncher.launchSearchGroup(
                continueTo = BACK_WITH_RESULT,
                targetFragment = this,
                requestCode = REQUEST_NEW_FAVORITE,
                selectGroupAfterSuccess = false
            )
        },
        FavoriteScheduleAdapterItem {
            analytics.sendClick("EditFavorite")
            val editFavoriteFragment = EditFavoriteFragment
                .newInstance(it.value.groupNumber, it.value.description)
                .withResultFor(this, REQUEST_EDIT_FAVORITE)
            addScreenForward { editFavoriteFragment }
        },
        HelpBannerAdapterItem()
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_NEW_FAVORITE && data != null) {
            data.getStringExtra("group_number")?.let { groupNumber ->
                val editFavoriteFragment = EditFavoriteFragment
                    .newInstance(groupNumber)
                    .withResultFor(this, REQUEST_EDIT_FAVORITE)
                addScreenForward { editFavoriteFragment }
            }
        } else if (requestCode == REQUEST_EDIT_FAVORITE && data != null) {
            val groupNumber = data.getStringExtra(EditFavoriteFragment.EXTRA_GROUP_NAME) ?: return
            val description = data.getStringExtra(EditFavoriteFragment.EXTRA_DESCRIPTION).orEmpty()
            feature.accept(Wish.Action.UpdateFavorite(FavoriteSchedule(groupNumber, description, 0)))
        }
    }
}