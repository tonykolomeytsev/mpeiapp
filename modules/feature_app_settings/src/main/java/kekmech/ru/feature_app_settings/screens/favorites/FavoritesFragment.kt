package kekmech.ru.feature_app_settings.screens.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.ActivityResultListener
import kekmech.ru.common_android.addSystemBottomPadding
import kekmech.ru.common_android.addSystemTopPadding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.AddActionAdapterItem
import kekmech.ru.coreui.items.FavoriteScheduleAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.domain_schedule.CONTINUE_TO_BACK_STACK_WITH_RESULT
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEffect
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent.Wish
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesFeature
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesState
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.ext.android.inject

private const val REQUEST_NEW_FAVORITE = 312344
private const val REQUEST_EDIT_FAVORITE = 312345

internal class FavoritesFragment : BaseFragment<FavoritesEvent, FavoritesEffect, FavoritesState, FavoritesFeature>(), ActivityResultListener {

    override val initEvent get() = Wish.Init

    override var layoutId: Int = R.layout.fragment_favorites

    private val dependencies by inject<AppSettingDependencies>()

    private val analytics by inject<FavoritesAnalytics>()

    override fun createFeature(): FavoritesFeature = dependencies.favoritesFeatureFactory.create()

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemTopPadding()
        toolbar.init(R.string.favorites_screen_title)
        recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
        recyclerView.addSystemBottomPadding()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.attachSwipeToDeleteCallback(isItemForDelete = { it is FavoriteSchedule }) {
            /* no-op */
        }
        analytics.sendScreenShown()
    }

    override fun render(state: FavoritesState) {
        adapter.update(FavoritesListConverter(requireContext()).map(state))
    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        AddActionAdapterItem {
            analytics.sendClick("AddFavorite")
            dependencies.scheduleFeatureLauncher.launchSearchGroup(
                continueTo = CONTINUE_TO_BACK_STACK_WITH_RESULT,
                targetFragment = this,
                requestCode = REQUEST_NEW_FAVORITE,
                selectGroupAfterSuccess = false
            )
        },
        FavoriteScheduleAdapterItem {
            analytics.sendClick("EditFavorite")
            feature.accept(Wish.Click.EditFavorite(it.value))
        }
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_NEW_FAVORITE && data != null) {
            Toast.makeText(requireContext(), data.getStringExtra("group_number"), Toast.LENGTH_SHORT).show()
        }
    }
}