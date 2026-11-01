package kekmech.ru.feature_bars_impl.presentation.screen.details

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeBottomSheetDialogFragment
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_bars_impl.domain.AssessedDiscipline
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val ARG_DISCIPLINE_INFO = "Arg.Discipline"

internal class BarsDetailsFragment : ComposeBottomSheetDialogFragment() {

    @Suppress("unused")
    private val analytics by screenAnalytics("BarsDetails")
    private val discipline by fastLazy { notNullSerializableArg<AssessedDiscipline>(ARG_DISCIPLINE_INFO) }

    @Composable
    override fun Screen() {
        MpeixTheme {
            BarsDetailsScreen(discipline = discipline)
        }
    }

    companion object {

        fun newInstance(assessedDiscipline: AssessedDiscipline) = BarsDetailsFragment()
            .withArguments(ARG_DISCIPLINE_INFO to assessedDiscipline)
    }
}
