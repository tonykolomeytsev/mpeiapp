package kekmech.ru.core

import androidx.lifecycle.LifecycleOwner

abstract class Presenter<T : LifecycleOwner> {

    open fun onCreate(view: T) = Unit

    open fun onResume(view: T) = Unit

    open fun onPause(view: T) = Unit
}