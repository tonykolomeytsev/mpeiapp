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

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = content {
        // Зависимости, нужные внутри экранов для взаимодействия с внешним миром
        val router = (requireContext().applicationContext as RouterHolder).router
        val resultController = FragmentResultController(this@ComposeFragment)
        CompositionLocalProvider(
            LocalRouter provides router,
            LocalResultController provides resultController,
        ) {
            Screen()
        }
    }

    @Composable
    public abstract fun Screen()
}
