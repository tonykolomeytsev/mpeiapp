package kekmech.ru.feature_search_impl.item

import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_ui.compose.ClassesItem
import kekmech.ru.feature_schedule_ui.compose.groupParallelClasses
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.feature_search_impl.R
import kekmech.ru.lib_adapter.AdapterItem
import kekmech.ru.lib_adapter.BaseItemBinder
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Parallel classes (see [groupParallelClasses]) shown as a single card.
 *
 * @param classes classes with the same number and time, never empty
 */
internal data class ParallelClassesItem(val classes: List<Classes>)

/**
 * RecyclerView item which renders the [ParallelClassesItem] with the Compose [ClassesItem],
 * so the classes look the same as on the schedule and dashboard screens.
 */
internal class ClassesComposeAdapterItem : AdapterItem<ClassesComposeViewHolder, ParallelClassesItem>(
    isType = { it is ParallelClassesItem },
    layoutRes = R.layout.item_classes_compose,
    viewHolderGenerator = ::ClassesComposeViewHolder,
    itemBinder = ClassesComposeItemBinder(),
)

internal class ClassesComposeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val composeView = view as ComposeView

    fun bind(item: ParallelClassesItem) {
        val model = item.classes.toUiModel(item.classes.first().scheduleType)
        composeView.setContent {
            MpeixTheme {
                ClassesItem(
                    model = model,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                )
            }
        }
    }
}

private class ClassesComposeItemBinder : BaseItemBinder<ClassesComposeViewHolder, ParallelClassesItem>() {

    override fun bind(vh: ClassesComposeViewHolder, model: ParallelClassesItem, position: Int) {
        vh.bind(model)
    }
}
