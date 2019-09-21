package kekmech.ru.timetable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.coreui.items.SingleLineItem
import kekmech.ru.timetable.R
import kotlinx.android.synthetic.main.fragment_day.view.*

class DayFragment(
    private val couples: List<CoupleNative>
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        view.dayFragmentRecyclerView.layoutManager = LinearLayoutManager(context)
        view.dayFragmentRecyclerView.adapter = SingleLineItem.buildAdapter().apply {
            baseItems.addAll(couples.map { SingleLineItem(it.name) })
        }
        // Inflate the layout for this fragment
        return view
    }
}