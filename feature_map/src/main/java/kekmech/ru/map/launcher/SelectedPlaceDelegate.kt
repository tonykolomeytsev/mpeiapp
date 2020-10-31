package kekmech.ru.map.launcher

internal class SelectedPlaceDelegate {
    private var placeUid: String? = null

    fun put(placeUid: String) {
        this.placeUid = placeUid
    }

    fun get() = placeUid

    fun clear() {
        placeUid = null
    }
}