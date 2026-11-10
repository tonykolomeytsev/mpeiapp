package kekmech.ru.lib_elm_compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import kotlinx.coroutines.flow.Flow
import money.vivid.elmslie.core.store.Store

@Composable
public inline fun <Event : Any, Effect : Any, State : Any> ElmContent(
    key: String,
    noinline storeFactory: SavedStateHandle.() -> Store<Event, Effect, State>,
    viewModelStoreOwner: ViewModelStoreOwner = LocalViewModelStoreOwner.current ?: error("No ViewModelStoreOwner"),
    savedStateRegistryOwner: SavedStateRegistryOwner = LocalSavedStateRegistryOwner.current,
    content: @Composable (onAccept: (Event) -> Unit, state: State, effects: Flow<Effect>) -> Unit,
) {
    val store = rememberElmStore(
        key = key,
        viewModelStoreOwner = viewModelStoreOwner,
        savedStateRegistryOwner = savedStateRegistryOwner,
        storeFactory = storeFactory,
    )
    val state by store.states.collectAsState()
    val onAccept = remember {
        { event: Event -> store.accept(event) }
    }
    val effects = store.effects
    content(onAccept, state, effects)
}