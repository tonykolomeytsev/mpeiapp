package kekmech.ru.addscreen.presenter

import kekmech.ru.addscreen.IAddFragment
import kekmech.ru.addscreen.model.AddFragmentModel
import kekmech.ru.core.Presenter
import kekmech.ru.core.Router
import kekmech.ru.core.dto.AcademGroup
import kekmech.ru.coreui.adapter2.BaseAdapter2
import kekmech.ru.coreui.adapter2.BaseItem2
import kotlinx.coroutines.*
import java.lang.RuntimeException
import java.util.*

class AddFragmentPresenter constructor(
    private val model: AddFragmentModel,
    private val router: Router
) : Presenter<IAddFragment>() {
    private var view: IAddFragment? = null

    /**
     * subscribe to view events
     */
    override fun onResume(view: IAddFragment) {
        this.view = view
        view.onSearchClickListener = this::onSearch
        GlobalScope.launch(Dispatchers.Main) {
            val groups = withContext(Dispatchers.IO) { model.getGroupsAsync() }
            val groupSet = mutableListOf<AcademGroup>()
            groups.reversed().forEach { raw ->
                if (!groupSet.any { it.name == raw.name }) {
                    groupSet.add(raw)
                }
            }

            val adapter = BaseAdapter2.Builder()
                .registerItemTypes(GroupItem::class)
                .build()
            adapter.items.addAll(groupSet
                //.take(10) снимаем ограничение
                .map(::GroupItem)
                .onEach { it.clickListener = ::onGroupClick })
            delay(100)
            view.setAdapter(adapter)
        }
    }

    private fun onGroupClick(groupItem: BaseItem2<*>) {
        val group = (groupItem as GroupItem).group
        try {
            GlobalScope.launch(Dispatchers.IO) {
                model.setCurrentGroup(group.name)
                withContext(Dispatchers.Main) {
                    router.popBackStack()
                }
                model.getGroupNumber()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * unsubscribe to view events
     */
    override fun onPause(view: IAddFragment) {
        this.view = null
    }

    private fun onSearch(group: String) {
        view?.hideLoadButton()
        view?.disableEditText()
        view?.showLoading()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val result = model.loadNewSchedule(group.toUpperCase(Locale.getDefault())) // FIXME не видит ошибку
                if (!result) throw RuntimeException("Error while loading schedule")
                withContext(Dispatchers.Main) {
                    router.popBackStack()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) { view?.showError() }
            }
            withContext(Dispatchers.Main) {
                view?.showLoadButton()
                view?.enableEditText()
                view?.hideLoading()
            }
            model.getGroupNumber()
        }
    }
}