package kekmech.ru.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

fun <A> observe(
    liveData1: LiveData<A>,
    lifecycleOwner: LifecycleOwner,
    observer: Observer<A?>
) =
    liveData1.observe(lifecycleOwner, observer)


fun <A, B> observe(
    liveData1: LiveData<A>,
    liveData2: LiveData<B>,
    lifecycleOwner: LifecycleOwner,
    observer: Observer<Pair<A?, B?>>
) =
    liveData1.observe(lifecycleOwner, Observer { a ->
        liveData2.observe(lifecycleOwner, Observer { b ->
            observer.onChanged(Pair(a, b))
        })
    })


fun <A, B, C> observe(
    liveData1: LiveData<A>,
    liveData2: LiveData<B>,
    liveData3: LiveData<C>,
    lifecycleOwner: LifecycleOwner,
    observer: Observer<Triple<A?, B?, C?>>
) =
    liveData1.observe(lifecycleOwner, Observer { a ->
        liveData2.observe(lifecycleOwner, Observer { b ->
            liveData3.observe(lifecycleOwner, Observer { c ->
                observer.onChanged(Triple(a, b, c))
            })
        })
    })

fun <A, B> zip(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

fun <A, B, C> zip(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>): LiveData<Triple<A, B, C>> {
    return MediatorLiveData<Triple<A, B, C>>().apply {
        var lastA: A? = null
        var lastB: B? = null
        var lastC: C? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            val localLastC = lastC
            if (localLastA != null && localLastB != null && localLastC != null)
                this.value = Triple(localLastA, localLastB, localLastC)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
        addSource(c) {
            lastC = it
            update()
        }
    }
}