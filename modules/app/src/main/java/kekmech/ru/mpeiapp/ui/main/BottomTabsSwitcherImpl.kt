package kekmech.ru.mpeiapp.ui.main

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kekmech.ru.common_kotlin.Option
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher

object BottomTabsSwitcherImpl : BottomTabsSwitcher {

    private val subject = BehaviorSubject.create<Option<BottomTab>>()

    override fun changeTab(tab: BottomTab) {
        subject.onNext(Option(tab))
    }

    override fun clearTab() {
        subject.onNext(Option(null))
    }

    override fun observe(): Observable<Option<BottomTab>> = subject
}