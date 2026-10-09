package kekmech.ru.feature_map_impl

import android.content.Context
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import org.osmdroid.config.Configuration

internal class MapInitializer : MainActivityLifecycleObserver {

    override fun onCreate(context: Context) {
        Configuration.getInstance().userAgentValue = context.packageName
    }
}
