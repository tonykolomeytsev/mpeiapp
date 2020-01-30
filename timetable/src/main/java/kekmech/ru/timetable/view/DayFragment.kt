package kekmech.ru.timetable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.TIMETABLE_TO_NOTE
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.usecases.GetTimetableScheduleLiveDataUseCase
import kekmech.ru.core.zip
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.R
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.items.MinCoupleItem
import kekmech.ru.timetable.view.items.MinLunchItem
import kekmech.ru.timetable.view.items.MinWeekendItem
import kotlinx.android.synthetic.main.fragment_day.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

abstract class DayFragment : Fragment() {

    abstract val dayOfWeek: Int

    val model: TimetableFragmentModel by inject()

    val router: Router by inject()

    fun getCouples(): LiveData<List<BaseItem<*>>> = model.getCouplesForDay(dayOfWeek + 1)

    private val adapter = BaseAdapter.Builder()
        .registerViewTypeFactory(MinCoupleItem.Factory())
        .registerViewTypeFactory(MinLunchItem.Factory())
        .registerViewTypeFactory(MinWeekendItem.Factory())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        view.dayFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        //view.dayFragmentRecyclerView.setRecycledViewPool(Companion.recycledViewPool)
        view.dayFragmentRecyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()
        loadSchedule()
    }

    private fun loadSchedule() {
        zip(getCouples(), model.weekOffset).observe(this, Observer { (couples, offset) ->
            // переход на добавление домашки
            val newListOfItems = couples
                .filter { if (it is MinCoupleItem) it.coupleNative.week % 2 != offset % 2 else true }
                .onEach { coupleItem ->
                    if (coupleItem is MinCoupleItem) {
                        coupleItem.clickListener = { onCoupleClick(coupleItem.coupleNative) }
                    }
                }
                .toMutableList()

            if (newListOfItems.all { it is MinLunchItem }) newListOfItems.clear() // костыль
            if (newListOfItems.isEmpty()) newListOfItems.add(MinWeekendItem())
            val diffCallback = object : DiffUtil.Callback() {
                override fun getOldListSize() = adapter.items.size
                override fun getNewListSize() = newListOfItems.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    adapter.items[oldItemPosition].javaClass == newListOfItems[newItemPosition].javaClass
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    adapter.items[oldItemPosition] == newListOfItems[newItemPosition]
            }
            val diffResult = DiffUtil.calculateDiff(diffCallback, true)
            adapter.items.clear()
            adapter.items.addAll(newListOfItems)
            diffResult.dispatchUpdatesTo(adapter)
        })
    }

    private fun onCoupleClick(coupleNative: CoupleNative) {
//        model.transactCouple(coupleNative)
//        router.navigate(TIMETABLE_TO_NOTE)
    }

    companion object {
        private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
    }
}