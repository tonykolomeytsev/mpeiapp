package kekmech.ru.feature_schedule.find_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.chip.Chip
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.BaseFragment
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher.ContinueTo.*
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.databinding.FragmentFindScheduleBinding
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEffect
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleEvent.Wish
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleFeatureFactory
import kekmech.ru.feature_schedule.find_schedule.elm.FindScheduleState
import kekmech.ru.feature_schedule.find_schedule.utils.GroupFormatTextWatcher
import org.koin.android.ext.android.inject

private const val CONTINUE_TO_ARG = "ContinueTo"
private const val SELECT_AFTER_ARG = "SelectAfter"

internal class FindScheduleFragment :
    BaseFragment<FindScheduleEvent, FindScheduleEffect, FindScheduleState>() {

    override val initEvent = Wish.Init
    override var layoutId = R.layout.fragment_find_schedule

    private val dependencies by inject<ScheduleDependencies>()
    private val onboardingFeatureLauncher by fastLazy { dependencies.onboardingFeatureLauncher }
    private val analytics by screenAnalytics("FindSchedule")
    private val viewBinding by viewBinding(FragmentFindScheduleBinding::bind)

    override fun createStore() = inject<FindScheduleFeatureFactory>().value.create(
        getArgument(CONTINUE_TO_ARG),
        getArgument(SELECT_AFTER_ARG)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            toolbar.init()
            groupText.showKeyboard()
            groupText.afterTextChanged {
                groupTextLayout.error = null
                feature.accept(Wish.Action.GroupNumberChanged(groupText.text?.toString().orEmpty()))
            }
            groupText.addTextChangedListener(GroupFormatTextWatcher(groupText))
            buttonContinue.setOnClickListener {
                analytics.sendClick("FindContinue")
                feature.accept(Wish.Click.Continue(groupText.text?.toString().orEmpty()))
            }
        }
    }

    override fun handleEffect(effect: FindScheduleEffect) = when (effect) {
        is FindScheduleEffect.ShowError -> viewBinding.groupTextLayout.setError(getString(
            R.string.schedule_find_error_loading,
            viewBinding.groupText.text?.toString().orEmpty()
        ))
        is FindScheduleEffect.ShowSomethingWentWrongError -> showBanner(R.string.something_went_wrong_error)
        is FindScheduleEffect.NavigateNextFragment -> when (effect.continueTo) {
            BACK -> close()
            BARS -> onboardingFeatureLauncher.launchBarsPage()
            DASHBOARD -> dependencies.mainScreenLauncher.launch()
            BACK_WITH_RESULT -> closeWithResult { putExtra("group_number", effect.groupName) }
            else -> Unit
        }
    }

    override fun render(state: FindScheduleState) {
        viewBinding.buttonContinue.isEnabled = state.isContinueButtonEnabled
        viewBinding.buttonContinue.setLoading(state.isLoading)
        renderAutocompleteList(state)
    }

    private fun renderAutocompleteList(state: FindScheduleState) {
        with(viewBinding.flowLayout) {
            removeAllViews()
            val inflater = LayoutInflater.from(requireContext())
            state.searchResults
                .filter { it.name != viewBinding.groupText.text.toString() }
                .forEach { result ->
                    val chip = inflater.inflate(R.layout.item_search_result, this, false) as Chip
                    chip.text = result.name
                    chip.setOnClickListener {
                        analytics.sendClick("ClickAutocomplete")
                        viewBinding.groupText.setText(result.name)
                        viewBinding.groupText.setSelection(result.name.length)
                    }
                    addView(chip)
                }
        }
    }

    companion object {
        fun newInstance(
            continueTo: ScheduleFeatureLauncher.ContinueTo = BACK,
            selectGroupAfterSuccess: Boolean = true
        ) = FindScheduleFragment().withArguments(
            CONTINUE_TO_ARG to continueTo,
            SELECT_AFTER_ARG to selectGroupAfterSuccess
        )
    }
}