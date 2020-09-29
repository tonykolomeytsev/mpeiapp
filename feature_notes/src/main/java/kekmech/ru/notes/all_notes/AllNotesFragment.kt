package kekmech.ru.notes.all_notes

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_analytics.addScrollAnalytics
import kekmech.ru.common_android.addSystemVerticalPadding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.attachScrollListenerForAppBarLayoutShadow
import kekmech.ru.coreui.items.NoteAdapterItem
import kekmech.ru.coreui.items.SectionHeaderAdapterItem
import kekmech.ru.coreui.items.SpaceAdapterItem
import kekmech.ru.coreui.touch_helpers.attachSwipeToDeleteCallback
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.notes.R
import kekmech.ru.notes.all_notes.mvi.AllNotesEffect
import kekmech.ru.notes.all_notes.mvi.AllNotesEvent
import kekmech.ru.notes.all_notes.mvi.AllNotesEvent.Wish
import kekmech.ru.notes.all_notes.mvi.AllNotesFeature
import kekmech.ru.notes.all_notes.mvi.AllNotesState
import kekmech.ru.notes.di.NotesDependencies
import kotlinx.android.synthetic.main.fragment_all_notes.*
import org.koin.android.ext.android.inject

class AllNotesFragment : BaseFragment<AllNotesEvent, AllNotesEffect, AllNotesState, AllNotesFeature>() {

    override val initEvent = Wish.Init

    private val dependencies: NotesDependencies by inject()

    override fun createFeature() = dependencies.allNotesFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_all_notes

    private val analytics: AllNotesAnalytics by inject()

    private val adapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        view.addSystemVerticalPadding()
        toolbar.init()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.attachScrollListenerForAppBarLayoutShadow(appBarLayout)
        recyclerView.addScrollAnalytics(analytics, "RecyclerView")
        recyclerView.attachSwipeToDeleteCallback(
            isTypeForDelete = { adapter.allData.getOrNull(it) is Note }
        ) { adapterPosition ->
            val noteToDelete = adapter.allData.getOrNull(adapterPosition) as? Note
            noteToDelete?.let {
                analytics.sendClick("DeleteNote")
                feature.accept(Wish.Action.DeleteNote(it))
            }
        }
        analytics.sendScreenShown()
    }

    override fun render(state: AllNotesState) {
        adapter.update(AllNotesListConverter().map(state))
    }

    override fun handleEffect(effect: AllNotesEffect) {

    }

    private fun createAdapter() = BaseAdapter(
        SpaceAdapterItem(),
        NoteAdapterItem(requireContext()),
        SectionHeaderAdapterItem()
    )
}