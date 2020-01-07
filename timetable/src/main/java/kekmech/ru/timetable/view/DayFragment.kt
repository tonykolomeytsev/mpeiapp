package kekmech.ru.timetable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.core.Router
import kekmech.ru.core.Screens.TIMETABLE_TO_NOTE
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Time
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

    val couples: () -> List<BaseItem<*>>
        get() = { model.getDaySchedule(
            dayOfWeek = dayOfWeek + 1,
            weekNum = Time.today().weekOfYear + (model.weekOffset.value ?: 0) ) } // будет либо 1, либо 2

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
        view.dayFragmentRecyclerView.setRecycledViewPool(Companion.recycledViewPool)
        view.dayFragmentRecyclerView.adapter = adapter
        model.weekOffset.observe(this, Observer { loadSchedule() })
        // Inflate the layout for this fragment
        return view
    }

    private fun loadSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            val awaitedCouples = couples()

            // переход на добавление домашки
            awaitedCouples.forEach { coupleItem ->
                if (coupleItem is MinCoupleItem) {
                    coupleItem.clickListener = { onCoupleClick(coupleItem.coupleNative) }
                }
            }

            withContext(Dispatchers.Main) {
                adapter.baseItems.clear()
                if (awaitedCouples.isNotEmpty())
                    adapter.baseItems.addAll(awaitedCouples)
                else
                    adapter.baseItems.add(MinWeekendItem())
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onCoupleClick(coupleNative: CoupleNative) {
//        model.transactCouple(coupleNative)
//        router.navigate(TIMETABLE_TO_NOTE)
    }

    companion object {
        private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
    }
}