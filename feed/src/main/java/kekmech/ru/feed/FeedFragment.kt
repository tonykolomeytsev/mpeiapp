package kekmech.ru.feed

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import kekmech.ru.core.Presenter
import kekmech.ru.core.dto.Couple
import kekmech.ru.core.dto.CoupleType
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.items.CoupleItem
import kekmech.ru.feed.items.FeedDividerItem
import kekmech.ru.feed.items.LunchItem
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed.view.recyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class FeedFragment : DaggerFragment() {

    @Inject
    lateinit var presenter: Presenter<FeedFragment>

    /**
     * fires when user scroll his feed down to the end.
     * presenter must subscribe for this event and load new items to the feed
     */
    var onScrollEndListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        GlobalScope.launch(Dispatchers.Main) {
            val adapter = BaseAdapter.Builder()
                .registerViewTypeFactory(FeedDividerItem.Factory())
                .registerViewTypeFactory(CoupleItem.Factory())
                .registerViewTypeFactory(LunchItem.Factory())
                .build()
            GlobalScope.async(Dispatchers.Default) {
                adapter.baseItems.addAll(listOf(
                    FeedDividerItem("Четверг 28 октября", isLineVisible = false),
                    CoupleItem(
                        Couple(
                            0,
                            "Линейная алгебра",
                            "Кудин С.Ф.", Date(), CoupleType.LECTURE
                        )
                    ),
                    CoupleItem(
                        Couple(
                            1,
                            "Гидропневпопривод мехатронных и робототехнических устройств",
                            "Зуев Ю.Ю.", Date(), CoupleType.PRACTICE
                        )
                    ),
                    LunchItem(),
                    CoupleItem(
                        Couple(
                            2,
                            "Английский",
                            "Крук Р.Р.", Date(), CoupleType.LAB
                        )
                    ),
                    CoupleItem(
                        Couple(
                            3,
                            "Инж. и комп. графика",
                            "Чахеев Е.Я.", Date(), CoupleType.PRACTICE
                        )
                    ),
                    FeedDividerItem("Пятница 29 октября", isLineVisible = false),
                    CoupleItem(
                        Couple(
                            0,
                            "Линейная алгебра",
                            "Кудин С.Ф.", Date(), CoupleType.LECTURE
                        )
                    ),
                    CoupleItem(
                        Couple(
                            1,
                            "Гидропневпопривод мехатронных и робототехнических устройств",
                            "Зуев Ю.Ю.", Date(), CoupleType.PRACTICE
                        )
                    ),
                    LunchItem(),
                    CoupleItem(
                        Couple(
                            2,
                            "Английский",
                            "Крук Р.Р.", Date(), CoupleType.LAB
                        )
                    ),
                    CoupleItem(
                        Couple(
                            3,
                            "Инж. и комп. графика",
                            "Чахеев Е.Я.", Date(), CoupleType.PRACTICE
                        )
                    )
                ))
                Unit
            }.await()

            view.recyclerView.layoutManager = LinearLayoutManager(activity)
            view.recyclerView.adapter = adapter
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause(this)
    }

    fun updateView(adapter: BaseAdapter) {
        recyclerView.adapter = adapter
    }

    fun showLoading() {

    }

    fun hideLoading() {

    }
}
