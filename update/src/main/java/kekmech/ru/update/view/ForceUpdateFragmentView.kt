package kekmech.ru.update.view

import androidx.lifecycle.LifecycleOwner

interface ForceUpdateFragmentView : LifecycleOwner {
    var onUpdateNow: () -> Unit
    var onUpdateLater: () -> Unit
}