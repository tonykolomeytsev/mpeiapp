package kekmech.ru.feed

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kekmech.ru.core.dto.Couple
import kekmech.ru.coreui.adapter.BaseAdapter
import kotlinx.android.synthetic.main.fragment_feed.view.*
import java.util.*
import javax.inject.Inject


class FeedFragment : DaggerFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        val adapter = BaseAdapter.Builder()
            .registerViewTypeFactory(FeedItem.Factory())
            .build()
        adapter.baseItems.addAll(listOf(
            Couple(0,"Линейная алгебра", "Кудин С.Ф.", Date()),
            Couple(1,"Высшая математика", "Качалов В.Е.", Date()),
            Couple(2,"Английский", "Крук Р.Р.", Date()),
            Couple(3,"Инж. и комп. графика", "Чахеев Е.Я.", Date())
        ).map { FeedItem(it) })

        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.adapter = adapter
        return view
    }

    companion object {
        fun newInstance():FeedFragment = FeedFragment()
    }
}
