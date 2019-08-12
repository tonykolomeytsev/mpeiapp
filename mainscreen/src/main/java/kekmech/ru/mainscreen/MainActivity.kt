package kekmech.ru.mainscreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dagger.Subcomponent
import dagger.android.AndroidInjection
import kekmech.ru.core.dto.Couple
import kekmech.ru.coreui.adapter.BaseAdapter
import kekmech.ru.feed.FeedItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : Activity() {

    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        

        val adapter = BaseAdapter.Builder()
            .registerViewTypeFactory(FeedItem.Factory())
            .build()
        adapter.baseItems.addAll((1..10).map { Couple(it,"Пара $it", "Рандом", Date()) }.map { FeedItem(it) })

        feedView.layoutManager = LinearLayoutManager(this@MainActivity)
        feedView.adapter = adapter
    }
}
