package kekmech.ru.timetable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.R
import kekmech.ru.timetable.TimetableFragmentPresenter
import kekmech.ru.timetable.model.TimetableFragmentModel
import kekmech.ru.timetable.view.items.MinCoupleItem
import kekmech.ru.timetable.view.items.MinLunchItem
import kekmech.ru.timetable.view.items.MinWeekendItem
import kotlinx.android.synthetic.main.fragment_day.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class DayFragment : DaggerFragment() {

    abstract val dayOfWeek: Int

    @Inject
    lateinit var model: TimetableFragmentModel

    val couples: () -> List<BaseItem<*>>
        get() = { model.getDaySchedule(dayOfWeek + 1, model.today.weekOfSemester) }

    var weekOffset = 0 // Для переключения между четной и нечетной неделей

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
        GlobalScope.launch(Dispatchers.IO) {
            val awaitedCouples = couples()
            withContext(Dispatchers.Main) {
                adapter.baseItems.clear()
                if (awaitedCouples.isNotEmpty())
                    adapter.baseItems.addAll(awaitedCouples)
                else
                    adapter.baseItems.add(MinWeekendItem())
                adapter.notifyDataSetChanged()
            }
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        private val recycledViewPool by lazy { RecyclerView.RecycledViewPool() }
    }
}