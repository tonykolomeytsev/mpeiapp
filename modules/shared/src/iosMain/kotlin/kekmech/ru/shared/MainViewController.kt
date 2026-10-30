package kekmech.ru.shared

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/** Called from Swift (iosApp/iosApp/ContentView.swift): `MainViewControllerKt.MainViewController()` */
@Suppress("FunctionName")
public fun MainViewController(): UIViewController = ComposeUIViewController { App() }
