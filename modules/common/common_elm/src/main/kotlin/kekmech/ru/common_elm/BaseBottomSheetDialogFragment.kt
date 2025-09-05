package kekmech.ru.common_elm

import androidx.annotation.LayoutRes
import kekmech.ru.common_android.fragment.BottomSheetDialogFragment
import money.vivid.elmslie.android.renderer.ElmRendererDelegate

public abstract class BaseBottomSheetDialogFragment<Effect : Any, State : Any>(@LayoutRes layoutId: Int) :
    BottomSheetDialogFragment(layoutId),
    ElmRendererDelegate<Effect, State>
