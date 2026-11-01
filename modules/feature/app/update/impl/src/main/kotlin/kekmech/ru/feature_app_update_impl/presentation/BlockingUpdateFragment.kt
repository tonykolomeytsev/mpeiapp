package kekmech.ru.feature_app_update_impl.presentation

import androidx.compose.runtime.Composable
import com.kekmech.lib_fragment.ComposeFragment
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.notNullSerializableArg
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_update_impl.domain.models.ForceUpdateInfo
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val ARG_UPDATE_INFO = "Arg.UpdateInfo"
private const val LINK_GITHUB = "https://github.com/tonykolomeytsev/mpeiapp/issues"

class BlockingUpdateFragment : ComposeFragment() {

    private val updateInfo: ForceUpdateInfo by fastLazy { notNullSerializableArg(ARG_UPDATE_INFO) }

    @Composable
    override fun Screen() {
        MpeixTheme {
            BlockingUpdateScreen(
                link = LINK_GITHUB,
                onExitClick = { close() },
                onUpdateClick = { requireContext().openLinkExternal(updateInfo.updateUrl) },
                onLinkClick = { requireContext().openLinkExternal(LINK_GITHUB) },
            )
        }
    }

    companion object {

        fun newInstance(forceUpdateInfo: ForceUpdateInfo) =
            BlockingUpdateFragment().withArguments(ARG_UPDATE_INFO to forceUpdateInfo)
    }
}
