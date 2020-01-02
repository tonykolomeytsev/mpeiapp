package kekmech.ru.settings.view

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner

interface SettingsFragmentView : LifecycleOwner {

    val recreatingActivity: AppCompatActivity
}