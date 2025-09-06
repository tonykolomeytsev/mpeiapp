package kekmech.ru.mpeiapp.demo.screens.elmslie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoStoreFactory
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kotlinx.serialization.Serializable
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoEvent as Event
import kekmech.ru.mpeiapp.demo.screens.elmslie.elm.ElmDemoState as ElmState

@Serializable
internal class ElmDemoScreen(
    private val randomArgument: Int,
) : NavScreen {

    @Composable
    override fun Content() {
        ElmContent<ElmDemoStoreFactory, _, _, _>(
            factory = { create(randomArgument = randomArgument) },
            composable = { onAccept, state, _ -> ElmDemoScreen(onAccept, state) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ElmDemoScreen(
    onAccept: (Event) -> Unit,
    state: ElmState,
) {
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
                onClick = { onAccept(Event.Ui.Click.TextButton) },
            ) {
                Text(text = "Load new resource")
            }
        }
    }
}
