package kekmech.ru.common_elm

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

public interface DisposableDelegate : Disposable {

    public fun Disposable.bind()

    public fun clearDisposables()
}

public class DisposableDelegateImpl : DisposableDelegate {

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
