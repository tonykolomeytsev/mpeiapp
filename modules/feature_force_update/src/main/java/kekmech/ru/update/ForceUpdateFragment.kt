package kekmech.ru.update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kekmech.ru.common_android.close
import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import kekmech.ru.common_android.getArgument
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_android.withArguments
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo
import kekmech.ru.update.databinding.FragmentForceUpdateBinding

private const val ARG_INFO = "Arg.Info"

internal class ForceUpdateFragment : BottomSheetDialogFragment(R.layout.fragment_force_update) {

    private val viewBinding by viewBinding(FragmentForceUpdateBinding::bind)
    private val forceUpdateInfo by fastLazy { getArgument<ForceUpdateInfo>(ARG_INFO) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            buttonUpdateNow.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(forceUpdateInfo.updateUrl)))
            }
            buttonUpdateLater.setOnClickListener { close() }
            textDescription.text = forceUpdateInfo.shortDescription
        }
    }

    companion object {

        fun newInstance(forceUpdateInfo: ForceUpdateInfo) = ForceUpdateFragment()
            .withArguments(ARG_INFO to forceUpdateInfo)
    }
}