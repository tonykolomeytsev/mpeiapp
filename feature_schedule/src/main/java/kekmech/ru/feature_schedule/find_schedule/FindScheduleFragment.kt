package kekmech.ru.feature_schedule.find_schedule

import android.os.Bundle
import android.view.View
import kekmech.ru.common_android.afterTextChanged
import kekmech.ru.common_android.clearErrorAfterChange
import kekmech.ru.common_android.showKeyboard
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.find_schedule.presentation.*
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleEvent.Wish
import kotlinx.android.synthetic.main.fragment_find_schedule.*
import org.koin.android.ext.android.inject

class FindScheduleFragment : BaseFragment<FindScheduleEvent, FindScheduleEffect, FindScheduleState, FindScheduleFeature>() {

    override val initEvent = Wish.Init

    override fun createFeature() = inject<FindScheduleFeatureFactory>().value.create()

    override var layoutId = R.layout.fragment_find_schedule

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)
        toolbar.init()
        groupText.showKeyboard()
        groupText.afterTextChanged {
            groupTextLayout.error = null
            feature.accept(Wish.Action.GroupNumberChanged(groupText.text?.toString().orEmpty()))
        }
        buttonContinue.setOnClickListener {
            feature.accept(Wish.Click.Continue(groupText.text?.toString().orEmpty()))
        }
    }

    override fun handleEffect(effect: FindScheduleEffect) = Unit

    override fun render(state: FindScheduleState) {
        buttonContinue.isEnabled = state.isContinueButtonEnabled
        buttonContinue.setLoading(state.isLoading)
    }

    companion object {
        fun newInstance() = FindScheduleFragment()
    }
}