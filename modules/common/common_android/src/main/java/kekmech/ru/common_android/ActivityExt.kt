package kekmech.ru.common_android

import android.content.Intent
import androidx.fragment.app.FragmentManager

interface ActivityResultListener {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}

fun FragmentManager.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    fragments.filterIsInstance<ActivityResultListener>()
        .forEach { it.onActivityResult(requestCode, resultCode, data) }
}