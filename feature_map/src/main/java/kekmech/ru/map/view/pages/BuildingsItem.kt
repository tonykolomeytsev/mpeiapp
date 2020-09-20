package kekmech.ru.map.view.pages

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.map.R
import com.google.firebase.firestore.GeoPoint
import kekmech.ru.coreui.deprecated.adapter.BaseAdapter
import kekmech.ru.coreui.deprecated.adapter.BaseFactory
import kekmech.ru.coreui.deprecated.adapter.BaseItem
import kekmech.ru.coreui.deprecated.adapter.BaseViewHolder
import kekmech.ru.map.model.MapFragmentModel

class BuildingsItem(private val model: MapFragmentModel) : BaseItem<BuildingsItem.ViewHolder>() {

    private val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(SingleBuildingItem.Factory())
        .build()

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.recycler.layoutManager = LinearLayoutManager(viewHolder.itemView.context)
        println("Buildings")
        model.buildings.observeForever {
            println(it.joinToString(prefix = "[", postfix = "]") {
                """{"name":"${it.name}","address":"${it.address}","location":${latLng(it.location)}}"""
            })
        }
        println("Foods")
        model.foods.observeForever {
            println(it.joinToString(prefix = "[", postfix = "]") {
                """{"name":"${it.name}","address":"${it.address}","location":${latLng(it.location)}}"""
            })
        }
        println("Hostels")
        model.hostels.observeForever {
            println(it.joinToString(prefix = "[", postfix = "]") {
                """{"name":"${it.name}","address":"${it.address}","location":${latLng(it.location)}}"""
            })
        }
//        GlobalScope.launch(Dispatchers.Main) {
//            delay(100)
//            model.buildings.observeForever {
//                adapter.baseItems.clear()
//                adapter.baseItems.addAll(it
//                    .map(::SingleBuildingItem)
//                    .onEach { item -> item.clickListener = { model.selectedPlaceListener(item) } })
//                viewHolder.recycler.adapter = adapter
//                viewHolder.progressBar.visibility = View.INVISIBLE
//            }
//        }
    }

    fun latLng(geoPoint: GeoPoint) = """{"lat":${geoPoint.latitude},"lng":${geoPoint.longitude}}"""

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val recycler by bind<RecyclerView>(R.id.recyclerViewBuildings)
        val progressBar by bind<ProgressBar>(R.id.progressBar)
    }

    class Factory : BaseFactory(R.layout.item_buildings) {
        override fun instance(view: View): BaseViewHolder {
            return ViewHolder(view)
        }
    }
}