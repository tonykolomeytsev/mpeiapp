package kekmech.ru.feature_app_update_impl.presentation

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeBottomSheetDialogFragment
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_update_impl.domain.models.ForceUpdateInfo
import kekmech.ru.lib_analytics_android.ext.screenAnalytics
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val ARG_INFO = "Arg.Info"

internal class ForceUpdateFragment : ComposeBottomSheetDialogFragment() {

    private val forceUpdateInfo by fastLazy { notNullSerializableArg<ForceUpdateInfo>(ARG_INFO) }
    private val analytics by screenAnalytics("ForceUpdate")

    @Composable
    override fun Screen() {
        MpeixTheme {
            ForceUpdateScreen(
                description = forceUpdateInfo.shortDescription,
                onUpdateClick = {
                    analytics.sendClick("UpdateNow")
                    requireContext().openLinkExternal(forceUpdateInfo.updateUrl)
                },
                onLaterClick = {
                    analytics.sendClick("UpdateLater")
                    close()
                },
            )
        }
    }

    companion object {

        fun newInstance(forceUpdateInfo: ForceUpdateInfo) = ForceUpdateFragment()
            .withArguments(ARG_INFO to forceUpdateInfo)
    }
}
