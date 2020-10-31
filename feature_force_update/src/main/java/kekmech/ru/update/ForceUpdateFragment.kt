package kekmech.ru.update

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kekmech.ru.common_android.close
import kekmech.ru.common_android.withArguments
import kekmech.ru.domain_force_update.dto.ForceUpdateInfo
import kotlinx.android.synthetic.main.fragment_force_update.*

private const val ARG_INFO = "Arg.Info"

internal class ForceUpdateFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_force_update, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            dialogInterface as BottomSheetDialog
            dialogInterface.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.setBackgroundColor(Color.TRANSPARENT)
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUpdateNow?.setOnClickListener { close() }
        buttonUpdateLater?.setOnClickListener { close() }
    }

    companion object {

        fun newInstance(forceUpdateInfo: ForceUpdateInfo) = ForceUpdateFragment()
            .withArguments(ARG_INFO to forceUpdateInfo)
    }
}