package com.kekmech.lib_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.Fragment
import androidx.fragment.compose.content
import kekmech.ru.lib_navigation.di.RouterHolder

/**
 * Compose Fragment which does not need XML layout.
 */
public abstract class ComposeFragment : Fragment() {

    // Живет вместе с фрагментом, а не с view, чтобы не пересоздаваться при каждой композиции
    private val resultController by lazy(LazyThreadSafetyMode.NONE) {
        FragmentResultController(this)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = content {
        ProvideScreenDependencies(resultController) {
            Screen()
        }
    }

    @Composable
    public abstract fun Screen()
}

/**
 * Зависимости, нужные внутри экранов для взаимодействия с внешним миром
 */
@Composable
internal fun Fragment.ProvideScreenDependencies(
    resultController: ResultController,
    content: @Composable () -> Unit,
) {
    val router = (requireContext().applicationContext as RouterHolder).router
    CompositionLocalProvider(
        LocalRouter provides router,
        LocalResultController provides resultController,
        content = content,
    )
}
