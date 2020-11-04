package kekmech.ru.feature_app_settings.screens.favorites

import android.os.Bundle
import android.view.View
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.feature_app_settings.R
import kekmech.ru.feature_app_settings.di.AppSettingDependencies
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEffect
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesEvent.Wish
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesFeature
import kekmech.ru.feature_app_settings.screens.favorites.mvi.FavoritesState
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.koin.android.ext.android.inject

internal class FavoritesFragment : BaseFragment<FavoritesEvent, FavoritesEffect, FavoritesState, FavoritesFeature>() {

    override val initEvent get() = Wish.Init

    override var layoutId: Int = R.layout.fragment_favorites

    private val dependencies by inject<AppSettingDependencies>()

    private val analytics by inject<FavoritesAnalytics>()

    override fun createFeature(): FavoritesFeature = dependencies.favoritesFeatureFactory.create()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        toolbar.init()
        recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
        analytics.sendScreenShown()
    }

    private fun createAdapter() = BaseAdapter(

    )
}