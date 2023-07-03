package kekmech.ru.feature_app_update_impl.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.ext_android.addSystemVerticalPadding
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.getArgument
import kekmech.ru.ext_android.openLinkExternal
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_app_update_impl.R
import kekmech.ru.feature_app_update_impl.databinding.FragmentBlockingUpdateBinding
import kekmech.ru.feature_app_update_impl.domain.models.ForceUpdateInfo
import kekmech.ru.library_analytics_android.ext.screenAnalytics

private const val ARG_UPDATE_INFO = "Arg.UpdateInfo"
private const val LINK_VK = "https://vk.com/kekmech"

class BlockingUpdateFragment : Fragment(R.layout.fragment_blocking_update) {

    private val viewBinding by viewBinding(FragmentBlockingUpdateBinding::bind)
    private val updateInfo: ForceUpdateInfo by fastLazy { getArgument(ARG_UPDATE_INFO) }
    private val analytics by screenAnalytics("BlockingUpdate")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            buttonExit.setOnClickListener {
                analytics.sendClick("DismissBlockingUpdate")
                close()
            }
            buttonUpdateNow.setOnClickListener {
                analytics.sendClick("UpdateBlockingUpdate")
                requireContext().openLinkExternal(updateInfo.updateUrl)
            }
            header.setText(kekmech.ru.strings.Strings.blocking_update_title)
            fullText.setText(kekmech.ru.strings.Strings.blocking_update_description)
            link.text = LINK_VK
            link.setOnClickListener {
                analytics.sendClick("GoToVkBlockingUpdate")
                requireContext().openLinkExternal(LINK_VK)
            }
        }
        view.addSystemVerticalPadding()
    }

    companion object {

        fun newInstance(forceUpdateInfo: ForceUpdateInfo) =
            BlockingUpdateFragment().withArguments(ARG_UPDATE_INFO to forceUpdateInfo)
    }
}
