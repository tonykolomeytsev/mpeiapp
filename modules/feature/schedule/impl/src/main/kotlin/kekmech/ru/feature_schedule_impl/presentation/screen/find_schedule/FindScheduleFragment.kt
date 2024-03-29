package kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.chip.Chip
import kekmech.ru.coreui.banner.showBanner
import kekmech.ru.ext_android.addSystemBottomPadding
import kekmech.ru.ext_android.addSystemTopPadding
import kekmech.ru.ext_android.afterTextChanged
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.getArgument
import kekmech.ru.ext_android.setResult
import kekmech.ru.ext_android.showKeyboard
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher.ContinueTo.BACK
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher.ContinueTo.BACK_WITH_RESULT
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher.ContinueTo.DASHBOARD
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.feature_schedule_impl.databinding.FragmentFindScheduleBinding
import kekmech.ru.feature_schedule_impl.di.ScheduleDependencies
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEffect
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleEvent.Ui
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleState
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleStoreFactory
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.utils.GroupFormatTextWatcher
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.lib_elm.BaseFragment
import org.koin.android.ext.android.inject
import kekmech.ru.res_strings.R.string as Strings

internal class FindScheduleFragment :
    BaseFragment<FindScheduleEvent, FindScheduleEffect, FindScheduleState>(R.layout.fragment_find_schedule) {

    private val dependencies by inject<ScheduleDependencies>()
    private val analytics by screenAnalytics("FindSchedule")
    private val viewBinding by viewBinding(FragmentFindScheduleBinding::bind)
    private val resultKey by fastLazy { getArgument<String>(ARG_RESULT_KEY) }

    override fun createStore() = inject<FindScheduleStoreFactory>().value.create(
        getArgument(ARG_CONTINUE_TO),
        getArgument(ARG_SELECT_AFTER)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            root.addSystemBottomPadding()
            appBarLayout.addSystemTopPadding()

            toolbar.setNavigationOnClickListener { close() }
            groupText.showKeyboard()
            groupText.afterTextChanged {
                groupTextLayout.error = null
                store.accept(Ui.Action.GroupNumberChanged(groupText.text?.toString().orEmpty()))
            }
            groupText.addTextChangedListener(GroupFormatTextWatcher(groupText))
            buttonContinue.setOnClickListener {
                analytics.sendClick("FindContinue")
                store.accept(Ui.Click.Continue(groupText.text?.toString().orEmpty()))
            }
        }
    }

    override fun handleEffect(effect: FindScheduleEffect) = when (effect) {
        is FindScheduleEffect.ShowError -> viewBinding.groupTextLayout.setError(getString(
            Strings.schedule_find_error_loading,
            viewBinding.groupText.text?.toString().orEmpty()
        ))
        is FindScheduleEffect.ShowSomethingWentWrongError -> showBanner(Strings.something_went_wrong_error)
        is FindScheduleEffect.NavigateNextFragment -> when (effect.continueTo) {
            BACK -> close()
            DASHBOARD -> dependencies.mainScreenLauncher.launch()
            BACK_WITH_RESULT -> {
                close()
                setResult(resultKey, result = effect.selectedSchedule)
            }
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

        private const val ARG_CONTINUE_TO = "Arg.ContinueTo"
        private const val ARG_SELECT_AFTER = "Arg.SelectAfter"
        private const val ARG_RESULT_KEY = "Arg.ResultKey"

        fun newInstance(
            continueTo: ScheduleFeatureLauncher.ContinueTo = BACK,
            selectGroupAfterSuccess: Boolean = true,
            resultKey: String,
        ) = FindScheduleFragment().withArguments(
            ARG_CONTINUE_TO to continueTo,
            ARG_SELECT_AFTER to selectGroupAfterSuccess,
            ARG_RESULT_KEY to resultKey,
        )
    }
}
