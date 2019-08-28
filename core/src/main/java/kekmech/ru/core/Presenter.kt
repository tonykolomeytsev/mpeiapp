package kekmech.ru.core

interface Presenter<T> {

    /**
     * subscribe to view events
     */
    fun onResume(view: T)

    /**
     * unsubscribe to view events
     */
    fun onPause(view: T)


}