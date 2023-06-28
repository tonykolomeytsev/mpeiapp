package kekmech.ru.mpeiapp.demo.screens.elmslie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.common_elm.elmNode
import kekmech.ru.common_elm.rememberAcceptAction
import kekmech.ru.common_kotlin.Resource
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoStore
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoStoreFactory
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.parcelize.Parcelize
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent as Event
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoState as ElmState

@Parcelize
internal class ElmDemoScreenNavTarget(
    private val randomArgument: Int,
) : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        elmNode<ElmDemoStoreFactory, _, _, _>(
            buildContext = buildContext,
            factory = { create(randomArgument = randomArgument) },
        ) { _, store, state -> ElmDemoScreen(store = store, state = state) }
}

@Composable
private fun ElmDemoScreen(
    store: ElmDemoStore,
    state: ElmState,
) {
    val accept = store.rememberAcceptAction()

    Scaffold(
        topBar = {
            TopAppBar(title = "Elm Demo")
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Random argument = ${state.randomArgument}")
            Spacer(modifier = Modifier.height(16.dp))
            val loadedResource = when (state.resource) {
                is Resource.Loading -> "Loading..."
                is Resource.Data -> state.resource.value
                is Resource.Error -> "Error: ${state.resource.error}"
            }
            Text(text = "Loaded resource = $loadedResource")
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = { accept(Event.Ui.Click.TextButton) },
            ) {
                Text(text = "Load new resource")
            }
        }
    }
}
