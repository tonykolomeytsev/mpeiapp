package kekmech.ru.domain_map

import android.content.Context
import com.google.gson.Gson
import kekmech.ru.common_persistent_cache.GsonPersistentCache
import kekmech.ru.domain_map.dto.GetMapMarkersResponse

class MapPersistentCache(
    context: Context,
    gson: Gson
) : GsonPersistentCache<Unit, GetMapMarkersResponse>(
    context = context,
    gson = gson,
    cacheName = "map_markers",
    valueClass = GetMapMarkersResponse::class.java
) {

    override fun generateKeyForPut(key: Unit, value: GetMapMarkersResponse?) = "value"

    override fun generateKeyForGet(key: Unit) = "value"
}