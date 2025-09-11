# Disable R8 for JS Bridge and WebView
-keepclassmembers class kekmech.ru.feature_bars_impl.presentation.screen.main.BarsFragment$JSInterface {
   public *;
}
-keep class com.kickex.feature_trading_ui.component.chart.WebAppInterface$WebInterfaceMessage { *; }
