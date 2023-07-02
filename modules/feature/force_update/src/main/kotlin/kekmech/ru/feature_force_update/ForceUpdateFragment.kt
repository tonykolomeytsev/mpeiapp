package kekmech.ru.feature_force_update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kekmech.ru.common_analytics.ext.screenAnalytics
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo
import kekmech.ru.ext_android.close
import kekmech.ru.ext_android.fragment.BottomSheetDialogFragment
import kekmech.ru.ext_android.getArgument
import kekmech.ru.ext_android.viewbinding.viewBinding
import kekmech.ru.ext_android.withArguments
import kekmech.ru.ext_kotlin.fastLazy
import kekmech.ru.feature_force_update.databinding.FragmentForceUpdateBinding

private const val ARG_INFO = "Arg.Info"

internal class ForceUpdateFragment : BottomSheetDialogFragment(R.layout.fragment_force_update) {

    private val viewBinding by viewBinding(FragmentForceUpdateBinding::bind)
    private val forceUpdateInfo by fastLazy { getArgument<ForceUpdateInfo>(ARG_INFO) }
    private val analytics by screenAnalytics("ForceUpdate")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.apply {
            buttonUpdateNow.setOnClickListener {
                analytics.sendClick("UpdateNow")
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(forceUpdateInfo.updateUrl)))
            }
            buttonUpdateLater.setOnClickListener {
                analytics.sendClick("UpdateLater")
                close()
            }
            textDescription.text = forceUpdateInfo.shortDescription
        }
    }

    companion object {

        fun newInstance(forceUpdateInfo: ForceUpdateInfo) = ForceUpdateFragment()
            .withArguments(ARG_INFO to forceUpdateInfo)
    }
}
