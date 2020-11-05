package kekmech.ru.feature_schedule.find_schedule

import android.os.Bundle
import android.view.View
import kekmech.ru.common_android.*
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.domain_schedule.CONTINUE_TO_BACK_STACK
import kekmech.ru.domain_schedule.CONTINUE_TO_BACK_STACK_WITH_RESULT
import kekmech.ru.domain_schedule.CONTINUE_TO_BARS_ONBOARDING
import kekmech.ru.domain_schedule.CONTINUE_TO_DASHBOARD
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.di.ScheduleDependencies
import kekmech.ru.feature_schedule.find_schedule.presentation.*
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleEvent.Wish
import kekmech.ru.feature_schedule.find_schedule.utils.GroupFormatTextWatcher
import kotlinx.android.synthetic.main.fragment_find_schedule.*
import org.koin.android.ext.android.inject

private const val CONTINUE_TO_ARG = "ContinueTo"
private const val SELECT_AFTER_ARG = "SelectAfter"

internal class FindScheduleFragment : BaseFragment<FindScheduleEvent, FindScheduleEffect, FindScheduleState, FindScheduleFeature>() {

    override val initEvent = Wish.Init

    override fun createFeature() = inject<FindScheduleFeatureFactory>().value.create(
        getArgument(CONTINUE_TO_ARG),
        getArgument(SELECT_AFTER_ARG)
    )

    override var layoutId = R.layout.fragment_find_schedule

    private val dependencies by inject<ScheduleDependencies>()
    private val onboardingFeatureLauncher by fastLazy { dependencies.onboardingFeatureLauncher }
    private val analytics: FindScheduleAnalytics by inject()

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)
        toolbar.init()
        groupText.showKeyboard()
        groupText.afterTextChanged {
            groupTextLayout.error = null
            feature.accept(Wish.Action.GroupNumberChanged(groupText.text?.toString().orEmpty()))
        }
        groupText.addTextChangedListener(GroupFormatTextWatcher(groupText))
        buttonContinue.setOnClickListener {
            analytics.sendClick("Continue")
            feature.accept(Wish.Click.Continue(groupText.text?.toString().orEmpty()))
        }
        analytics.sendScreenShown()
    }

    override fun handleEffect(effect: FindScheduleEffect) = when (effect) {
        is FindScheduleEffect.ShowError ->
            groupTextLayout.setError(getString(R.string.schedule_find_error_loading, groupText.text?.toString().orEmpty()))
        is FindScheduleEffect.ShowSomethingWentWrongError -> showBanner(R.string.something_went_wrong_error)
        is FindScheduleEffect.NavigateNextFragment -> when (effect.continueTo) {
            CONTINUE_TO_BACK_STACK -> close()
            CONTINUE_TO_BARS_ONBOARDING -> onboardingFeatureLauncher.launchBarsPage()
            CONTINUE_TO_DASHBOARD -> dependencies.mainScreenLauncher.launch()
            CONTINUE_TO_BACK_STACK_WITH_RESULT -> closeWithResult { putExtra("group_number", effect.groupName) }
            else -> Unit
        }
    }

    override fun render(state: FindScheduleState) {
        buttonContinue.isEnabled = state.isContinueButtonEnabled
        buttonContinue.setLoading(state.isLoading)
    }

    companion object {
        fun newInstance(
            continueTo: String = CONTINUE_TO_BACK_STACK,
            selectGroupAfterSuccess: Boolean = true
        ) = FindScheduleFragment().withArguments(
            CONTINUE_TO_ARG to continueTo,
            SELECT_AFTER_ARG to selectGroupAfterSuccess
        )
    }
}