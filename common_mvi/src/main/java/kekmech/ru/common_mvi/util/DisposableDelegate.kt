package kekmech.ru.common_mvi.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface DisposableDelegate : Disposable {

    fun Disposable.bind()

    fun clearDisposables()
}

class DisposableDelegateImpl : DisposableDelegate {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun isDisposed(): Boolean = disposables.isDisposed

    override fun clearDisposables() {
        disposables.clear()
    }

    override fun dispose() {
        disposables.dispose()
    }

    override fun Disposable.bind() {
        disposables.add(this)
    }
}