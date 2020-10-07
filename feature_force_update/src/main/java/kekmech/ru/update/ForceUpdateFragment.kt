package kekmech.ru.update

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_force_update.*

class ForceUpdateFragment : BottomSheetDialogFragment() {

    var onUpdateNow: () -> Unit = {}

    var onUpdateLater: () -> Unit = {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View?)?.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_force_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUpdateNow?.setOnClickListener { onUpdateNow() }
        buttonUpdateLater?.setOnClickListener { onUpdateLater() }
    }
}