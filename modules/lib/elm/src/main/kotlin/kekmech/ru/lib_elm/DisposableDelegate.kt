package kekmech.ru.lib_elm

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

interface DisposableDelegate : Disposable {

    fun Disposable.bind()

    fun bindDisposable(disposable: Disposable)

    fun clearDisposables()
}

class DisposableDelegateImpl : DisposableDelegate {

    protected val disposables: CompositeDisposable = CompositeDisposable()

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

    override fun bindDisposable(disposable: Disposable) {
        disposable.bind()
    }
}
