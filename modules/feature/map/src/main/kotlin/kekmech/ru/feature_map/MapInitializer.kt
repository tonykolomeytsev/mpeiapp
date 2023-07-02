package kekmech.ru.feature_map

import android.content.Context
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.MapsInitializer
import kekmech.ru.library_app_lifecycle.MainActivityLifecycleObserver
import timber.log.Timber

internal class MapInitializer : MainActivityLifecycleObserver {

    override fun onCreate(context: Context) {
        try {
            MapsInitializer.initialize(context)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Timber.e(e)
        }
    }
}
