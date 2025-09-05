package kekmech.ru.common_elm

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import money.vivid.elmslie.android.renderer.ElmRendererDelegate

public abstract class BaseFragment<Effect : Any, State : Any>(@LayoutRes layoutId: Int) :
    Fragment(layoutId),
    ElmRendererDelegate<Effect, State>
