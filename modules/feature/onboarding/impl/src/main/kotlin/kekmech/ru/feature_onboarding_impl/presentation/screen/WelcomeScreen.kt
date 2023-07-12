package kekmech.ru.feature_onboarding_impl.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.feature_schedule_api.presentation.navigation.ScheduleSearchScreenApi
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.lib_navigation_compose.LocalBackStackNavigator
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.parcelize.Parcelize
import org.koin.compose.rememberKoinInject

@Parcelize
internal class WelcomeScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { modifier -> WelcomeScreen(modifier) }
}

@Composable
private fun WelcomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Welcome screen",
                style = MpeixTheme.typography.header2,
            )
            Spacer(modifier = Modifier.height(16.dp))

            val navigator = LocalBackStackNavigator.current
            val scheduleSearchScreenApi: ScheduleSearchScreenApi = rememberKoinInject()

            TextButton(onClick = {
                navigator.navigate(scheduleSearchScreenApi.navTarget())
            }) {
                Text(text = "Go to ScheduleSearchScreen")
            }
        }
    }
}
