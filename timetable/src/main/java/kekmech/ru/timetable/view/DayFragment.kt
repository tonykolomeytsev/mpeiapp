package kekmech.ru.timetable.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.TIMETABLE_TO_NOTE
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
import kekmech.ru.core.zip
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.R
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.items.MinCoupleItem
import kekmech.ru.timetable.view.items.MinLunchItem
import kekmech.ru.timetable.view.items.MinWeekendItem
import kotlinx.android.synthetic.main.fragment_day.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.koin.android.ext.android.inject

abstract class DayFragment : Fragment() {

    abstract val dayOfWeek: Int

    val model: TimetableFragmentModel by inject()

    val router: Router by inject()

    var lastOffset = 0

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
        loadSchedule()
        // Inflate the layout for this fragment
        return view
    }

    private fun loadSchedule() {
        zip(model.schedule, model.weekOffset).observe(this, Observer { (schedule, offset) ->
            val today = Time.today()
            val necessaryWeekNum = if (today.weekOfYear % 2 == schedule.calendarWeek % 2) (if (offset == 0) 1 else 2) else (if (offset == 0) 2 else 1)

            val couples: MutableList<BaseItem<*>> = schedule
                .coupleList
                .filter { (it.day == dayOfWeek + 1) and (it.week == necessaryWeekNum) }
                .map { MinCoupleItem(it) }
                .toMutableList()
            // insert lunch
            val thirdCoupleIndex = couples
                .indexOfFirst { (it as MinCoupleItem).coupleNative.num == 3 }
            if (thirdCoupleIndex != -1 && (couples.first() as MinCoupleItem).coupleNative.num != 3)
                couples.add(thirdCoupleIndex, MinLunchItem())

            val newListOfItems = couples
                .onEach { coupleItem ->
                    if (coupleItem is MinCoupleItem) {
                        coupleItem.clickListener = { onCoupleClick(coupleItem.coupleNative, offset) }
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

            loadNotes(newListOfItems, offset)
        })
    }

    private fun loadNotes(
        newListOfItems: MutableList<BaseItem<*>>,
        offset: Int
    ) {
        GlobalScope.launch(Default) {
            newListOfItems
                .mapNotNull { if (it is MinCoupleItem) it.coupleNative else null }
                .onEach { couple ->
                    val note = model.getNote(couple, offset)
                    if (note != null)
                        withContext(Main) { couple.noteLiveData.value = note }
                }
        }
    }

    private fun onCoupleClick(coupleNative: CoupleNative, offset: Int) {
        model.transactCouple(coupleNative, offset)
        router.navigate(TIMETABLE_TO_NOTE)
    }
}