package kekmech.ru.mpeiapp.ui.main

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher
import java.util.*

object BottomTabsSwitcherImpl : BottomTabsSwitcher {

    private val subject = BehaviorSubject.create<Optional<BottomTab>>()

    override fun changeTab(tab: BottomTab) {
        subject.onNext(Optional.of(tab))
    }

    override fun clearTab() {
        subject.onNext(Optional.empty())
    }

    override fun observe(): Observable<Optional<BottomTab>> = subject
}