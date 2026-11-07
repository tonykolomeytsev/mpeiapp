package kekmech.ru.feature_bars_impl.presentation.screen.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import org.koin.compose.koinInject

@Composable
internal fun BarsEntry() {
    val factory = koinInject<BarsStoreFactory>()
    val activity = LocalActivity.current as ComponentActivity
    ElmContent(
        key = "BarsScreen",
        storeFactory = { factory.create() },
        viewModelStoreOwner = activity,
        savedStateRegistryOwner = activity,
        content = ::BarsComposeScreen,
    )
}
