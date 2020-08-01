package kekmech.ru.common_mvi.util

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

@Suppress("ComplexInterface", "TooManyFunctions")
interface MappingActor<Event : Any> {

    fun Completable.mapEvents(successEvent: Event, failureEvent: Event): Observable<Event> =
        mapSuccessEvent(successEvent).mapErrorEvent { failureEvent }

    fun Completable.mapEvents(successEvent: Event, failureEvent: (Throwable) -> Event): Observable<Event> =
        mapSuccessEvent(successEvent).mapErrorEvent(failureEvent)

    fun <T : Any> Observable<T>.mapEvents(successEventMapper: (T) -> Event, failureEvent: Event): Observable<Event> =
        mapSuccessEvent(false, successEventMapper).mapErrorEvent { failureEvent }

    fun <T : Any> Observable<T>.mapEvents(
        successEventMapper: (T) -> Event,
        failureEventMapper: (throwable: Throwable) -> Event
    ): Observable<Event> = mapSuccessEvent(false, successEventMapper).mapErrorEvent(failureEventMapper)

    fun <T : Any> Single<T>.mapEvents(successEvent: Event, failureEvent: (Throwable) -> Event): Observable<Event> =
        mapSuccessEvent(successEvent).mapErrorEvent(failureEvent)

    fun <T : Any> Single<T>.mapEvents(successEventMapper: (T) -> Event, failureEvent: Event): Observable<Event> =
        mapSuccessEvent(successEventMapper).mapErrorEvent { failureEvent }

    fun <T : Any> Single<T>.mapEvents(
        successEventMapper: (T) -> Event,
        failureEvent: (Throwable) -> Event
    ): Observable<Event> =
        mapSuccessEvent(successEventMapper).mapErrorEvent(failureEvent)

    fun <T, R : Any> Single<T>.mapSuccessEvent(successEvent: R): Observable<R> =
        map { successEvent }.doOnSuccess { it.logSuccessEvent() }.toObservable()

    fun <T : Any> Maybe<T>.mapEvents(
        successEventMapper: (T) -> Event,
        completionEvent: Event,
        failureEvent: Event
    ): Observable<Event> = this
        .map(successEventMapper)
        .toSingle(completionEvent)
        .mapErrorEvent(failureEvent)

    fun <T : Any> Maybe<T>.mapEvents(
        successEventMapper: (T) -> Event,
        completionEvent: Event,
        failureEventMapper: (throwable: Throwable) -> Event
    ): Observable<Event> = this
        .map(successEventMapper)
        .toSingle(completionEvent)
        .mapErrorEvent(failureEventMapper)

    fun <T, R : Any> Single<T>.mapSuccessEvent(successEventMapper: (T) -> R): Observable<R> =
        map(successEventMapper).doOnSuccess { it.logSuccessEvent() }.toObservable()

    fun <T, R : Any> Observable<T>.mapSuccessEvent(
        withSkippingError: Boolean = false,
        successEventMapper: (T) -> R
    ): Observable<R> =
        map(successEventMapper).doOnNext { it.logSuccessEvent() }.let {
            return if (withSkippingError) {
                it.onErrorResumeNext { error: Throwable ->
                    Observable.empty<R>().also { Timber.e(error) }
                }
            } else {
                it
            }
        }

    fun <T : Any> Completable.mapSuccessEvent(successEvent: T): Observable<T> =
        andThen(
            Observable.just<T>(successEvent).doOnComplete { successEvent.logSuccessEvent() }
        )

    fun <T, R : Any> Maybe<T>.mapSuccessEvent(successEventMapper: (T?) -> R): Observable<R> =
        map(successEventMapper)
            .defaultIfEmpty(successEventMapper(null))
            .doOnSuccess { it.logSuccessEvent() }
            .toObservable()

    fun <T : Any> Completable.mapErrorEvent(failureEvent: T): Observable<T> = this
        .toObservable<T>()
        .mapErrorEvent { failureEvent }

    fun <T : Any> Single<T>.mapErrorEvent(failureEvent: T): Observable<T> = this
        .toObservable()
        .mapErrorEvent { failureEvent }

    fun Completable.mapErrorEvent(failureEvent: (Throwable) -> Event): Observable<Event> = this
        .toObservable<Event>()
        .mapErrorEvent(failureEvent)

    fun <T : Any> Single<T>.mapErrorEvent(failureEvent: (Throwable) -> T): Observable<T> = this
        .toObservable()
        .mapErrorEvent(failureEvent)

    fun <T : Any> Observable<T>.mapErrorEvent(failureEvent: (Throwable) -> T): Observable<T> =
        onErrorReturn {
            failureEvent.logFailedEvent()
            Timber.e(it)
            failureEvent(it)
        }

    private fun Any.logSuccessEvent() {
        Timber.d("Completed app state: $this")
    }

    private fun Any.logFailedEvent() {
        Timber.d("Failed app state: $this")
    }
}