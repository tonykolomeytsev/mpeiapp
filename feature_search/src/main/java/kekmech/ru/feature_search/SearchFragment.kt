package kekmech.ru.feature_search

import android.os.Bundle
import android.view.View
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_android.close
import kekmech.ru.common_android.showKeyboard
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.feature_search.di.SearchDependencies
import kekmech.ru.feature_search.mvi.SearchEffect
import kekmech.ru.feature_search.mvi.SearchEvent
import kekmech.ru.feature_search.mvi.SearchFeature
import kekmech.ru.feature_search.mvi.SearchState
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject

internal class SearchFragment : BaseFragment<SearchEvent, SearchEffect, SearchState, SearchFeature>() {

    override val initEvent get() = SearchEvent.Wish.Init

    private val dependencies by inject<SearchDependencies>()

    override fun createFeature(): SearchFeature = dependencies.searchFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_search

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        navBackButton.setOnClickListener { close() }
        searchView.showKeyboard()
    }
}