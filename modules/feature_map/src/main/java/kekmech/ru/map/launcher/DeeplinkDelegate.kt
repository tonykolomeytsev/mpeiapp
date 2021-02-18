package kekmech.ru.map.launcher

import kekmech.ru.map.elm.FilterTab

internal class DeeplinkDelegate {
    private var placeUid: String? = null
    private var tab: FilterTab? = null

    fun putPlaceUid(placeUid: String) {
        this.placeUid = placeUid
    }

    fun getPlaceUid() = placeUid

    fun putTab(tabName: String) {
        this.tab = FilterTab.valueOf(tabName)
    }

    fun getTab() = tab

    fun clear() {
        placeUid = null
        tab = null
    }

    fun isNotEmpty() = placeUid != null || tab != null
}