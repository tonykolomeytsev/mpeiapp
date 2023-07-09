package kekmech.ru.feature_main_screen_impl.presentation.screen.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.spotlight.Spotlight
import com.bumble.appyx.navmodel.spotlight.backpresshandler.GoToPrevious
import com.bumble.appyx.navmodel.spotlight.current
import com.bumble.appyx.navmodel.spotlight.operation.activate
import com.bumble.appyx.navmodel.spotlight.transitionhandler.rememberSpotlightFader
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTab
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTabSwitcher
import kekmech.ru.feature_main_screen_api.presentation.navigation.children.ChildrenResolutionStrategy
import kekmech.ru.feature_main_screen_impl.presentation.navigation.MainScreenTabStateHolder
import kekmech.ru.feature_main_screen_impl.presentation.navigation.children.DependencyInjectionPlugin
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.ui_icons.MpeixIcons
import kekmech.ru.ui_kit_navigationbar.NavigationBar
import kekmech.ru.ui_kit_navigationbar.NavigationBarItem
import kotlinx.parcelize.Parcelize
import org.koin.compose.rememberKoinInject

@Parcelize
internal class MainScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        MainScreenNode(
            buildContext = buildContext,
        ) { modifier -> MainScreen(modifier) }
}

internal class MainScreenNode(
    buildContext: BuildContext,
    private val spotlight: Spotlight<MainScreenTab> = Spotlight(
        items = MainScreenTab.values().asList(),
        initialActiveIndex = 0,
        savedStateMap = buildContext.savedStateMap,
        key = "MainScreenNode",
        backPressHandler = GoToPrevious(),
    ),
    private val composable: @Composable MainScreenNode.(Modifier) -> Unit,
) :
    ParentNode<MainScreenTab>(
        buildContext = buildContext,
        navModel = spotlight,
        plugins = listOf(DependencyInjectionPlugin()),
    ) {

    // will be injected in DependencyInjectionPlugin
    lateinit var resolutionStrategy: ChildrenResolutionStrategy

    override fun resolve(navTarget: MainScreenTab, buildContext: BuildContext): Node =
        resolutionStrategy.resolve(navTarget, buildContext)

    @Composable
    override fun View(modifier: Modifier) {
        CompositionLocalProvider(
            LocalSpotlight provides spotlight,
        ) {
            composable(modifier)
        }
    }
}

private val LocalSpotlight = compositionLocalOf<Spotlight<MainScreenTab>> {
    error("LocalSpotlight not provided")
}

@Composable
private fun MainScreenNode.MainScreen(
    modifier: Modifier = Modifier,
) {
    val tabStateHolder = rememberKoinInject<MainScreenTabStateHolder>()
    val spotlight = LocalSpotlight.current

    LaunchedEffect(Unit) {
        // subscribe nav model to tabStateHolder changes
        tabStateHolder.tabState.collect { tab ->
            spotlight.activate(tab.ordinal)
        }
    }
    val tab = spotlight.current().collectAsState(initial = null)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainScreenNavigationBar(tab)
        }
    ) { innerPadding ->
        Children(
            navModel = spotlight,
            modifier = Modifier.padding(innerPadding),
            transitionHandler = rememberSpotlightFader(),
        )
    }
}

@Composable
private fun MainScreenNavigationBar(
    tab: State<MainScreenTab?>,
) {
    val tabSwitcher = rememberKoinInject<MainScreenTabSwitcher>()

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
    ) {
        NavigationBarItem(
            selected = tab.value == MainScreenTab.Dashboard || tab.value == null,
            onClick = { tabSwitcher.switch(MainScreenTab.Dashboard) },
            icon = {
                Icon(
                    painterSelected = MpeixIcons.WhatshotBlack24,
                    painterUnselected = MpeixIcons.WhatshotOutline24,
                )
            },
            label = "Главная",
        )
        NavigationBarItem(
            selected = tab.value == MainScreenTab.Schedule,
            onClick = { tabSwitcher.switch(MainScreenTab.Schedule) },
            icon = {
                Icon(
                    painterSelected = MpeixIcons.EventBlack24,
                    painterUnselected = MpeixIcons.EventOutline24,
                )
            },
            label = "Расписание",
        )
        NavigationBarItem(
            selected = tab.value == MainScreenTab.Map,
            onClick = { tabSwitcher.switch(MainScreenTab.Map) },
            icon = {
                Icon(
                    painterSelected = MpeixIcons.ExploreBlack24,
                    painterUnselected = MpeixIcons.ExploreOutline24,
                )
            },
            label = "Карта",
        )
        NavigationBarItem(
            selected = tab.value == MainScreenTab.Profile,
            onClick = { tabSwitcher.switch(MainScreenTab.Profile) },
            icon = {
                Icon(
                    painterSelected = MpeixIcons.AccountBlack24,
                    painterUnselected = MpeixIcons.AccountOutline24,
                )
            },
            label = "Профиль",
        )
    }
}
