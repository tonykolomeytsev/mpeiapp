package kekmech.ru.update

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.*
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo
import kekmech.ru.update.databinding.FragmentBlockingUpdateBinding

private const val ARG_UPDATE_INFO = "Arg.UpdateInfo"
private const val LINK_VK = "https://vk.com/kekmech"

class BlockingUpdateFragment : Fragment(R.layout.fragment_blocking_update) {

    private val viewBinding by viewBinding(FragmentBlockingUpdateBinding::bind)
    private val updateInfo: ForceUpdateInfo by fastLazy { getArgument(ARG_UPDATE_INFO) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            buttonExit.setOnClickListener { close() }
            buttonUpdateNow.setOnClickListener {
                requireContext().openLinkExternal(updateInfo.updateUrl)
            }
            header.setText(R.string.blocking_update_title)
            fullText.setText(R.string.blocking_update_description)
            link.text = LINK_VK
            link.setOnClickListener {
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