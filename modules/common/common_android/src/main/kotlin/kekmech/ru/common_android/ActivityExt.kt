package kekmech.ru.common_android

import android.content.Intent
import androidx.fragment.app.FragmentManager

public interface ActivityResultListener {

    public fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

public fun FragmentManager.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    fragments.filterIsInstance<ActivityResultListener>()
        .forEach { it.onActivityResult(requestCode, resultCode, data) }
}
