package kekmech.ru.coreui.items

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.ReusableViewHolder
import kekmech.ru.common_android.viewbinding.lazyBinding
import kekmech.ru.common_android.viewbinding.unit
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.coreui.R
import kekmech.ru.coreui.databinding.ItemClassesBinding
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.ClassesType
import java.time.format.DateTimeFormatter

interface ClassesViewHolder : ClickableItemViewHolder {
    fun setDisciplineName(name: String)
    fun setPersonName(name: String?)
    fun setPlace(name: String)
    fun setNumberName(name: String)
    fun setTypeName(name: String)
    fun setTagColor(@ColorInt color: Int)
    fun setStartTime(time: String)
    fun setEndTime(time: String)
}

open class ClassesViewHolderImpl(
    override val containerView: View
) :
    ClassesViewHolder,
    ClickableItemViewHolder by ClickableItemViewHolderImpl(containerView),
    RecyclerView.ViewHolder(containerView),
    ReusableViewHolder {

    private val textViewClassesName by lazyBinding<TextView>(R.id.textViewClassesName)
    private val textViewClassesPerson by lazyBinding<TextView>(R.id.textViewClassesPerson)
    private val textViewClassesPlace by lazyBinding<TextView>(R.id.textViewClassesPlace)
    private val textViewClassesNumber by lazyBinding<TextView>(R.id.textViewClassesNumber)
    private val colorTag by lazyBinding<View>(R.id.colorTag)
    private val textViewClassesType by lazyBinding<TextView>(R.id.textViewClassesType)
    private val textViewTimeStart by lazyBinding<TextView>(R.id.textViewTimeStart)
    private val textViewTimeEnd by lazyBinding<TextView>(R.id.textViewTimeEnd)

    override fun setDisciplineName(name: String) {
        textViewClassesName.text = name
    }

    override fun setPersonName(name: String?) {
        name?.let {
            textViewClassesPerson.isVisible = true
            textViewClassesPerson.text = it
        } ?: run {
            textViewClassesPerson.isVisible = false
        }
    }

    override fun setPlace(name: String) {
        textViewClassesPlace.text = name
    }

    override fun setNumberName(name: String) {
        textViewClassesNumber.text = name
    }

    override fun setTagColor(color: Int) {
        colorTag.backgroundTintList = ColorStateList.valueOf(color)
    }

    override fun setTypeName(name: String) {
        textViewClassesType.text = name
    }

    override fun setStartTime(time: String) {
        textViewTimeStart.text = time
    }

    override fun setEndTime(time: String) {
        textViewTimeEnd.text = time
    }
}

class ClassesItemBinder(
    context: Context,
    private val onClickListener: ((Classes) -> Unit)? = null
) : BaseItemBinder<ClassesViewHolder, Classes>() {

    private val classesNumbers by fastLazy { context.getStringArray(R.array.classes_numbers) }
    private val classesTypes by fastLazy {
        val types = listOf(
            ClassesType.UNDEFINED to 0,
            ClassesType.LECTURE to 1,
            ClassesType.PRACTICE to 2,
            ClassesType.LAB to 3,
            ClassesType.COURSE to 4
        )
        val string = context.getStringArray(R.array.classes_types)
        types.map { (k, v) -> k to string[v] }.toMap()
    }
    private val colorTags by fastLazy {
        mapOf(
            ClassesType.UNDEFINED to context.getThemeColor(R.attr.colorGray30),
            ClassesType.LECTURE to context.getThemeColor(R.attr.colorGreen),
            ClassesType.PRACTICE to context.getThemeColor(R.attr.colorYellowLight),
            ClassesType.LAB to context.getThemeColor(R.attr.colorRed),
            ClassesType.COURSE to context.getThemeColor(R.attr.colorGray30)
        )
    }
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun bind(vh: ClassesViewHolder, model: Classes, position: Int) {
        vh.setDisciplineName(model.name)
        vh.setPersonName(model.person.takeIfNotEmpty())
        vh.setPlace(model.place)
        vh.setNumberName(classesNumbers.getOrElse(model.number) { classesNumbers[0] }.toUpperCase())
        vh.setTypeName(getClassesTypeName(model).toUpperCase())
        vh.setTagColor(colorTags.getValue(model.type))
        vh.setStartTime(model.time.start.format(timeFormatter))
        vh.setEndTime(model.time.end.format(timeFormatter))
        vh.setOnClickListener { onClickListener?.invoke(model) }
    }

    private fun String.takeIfNotEmpty() = takeIf {
        isNotBlank() && !it.contains("Вакансия", ignoreCase = true)
    }

    private fun getClassesTypeName(classes: Classes): String {
        return if (classes.type == ClassesType.UNDEFINED && classes.rawType.isNotBlank()) {
            classes.rawType
        } else {
            classesTypes[classes.type].orEmpty()
        }
    }
}

class ClassesAdapterItem(
    context: Context,
    onClickListener: ((Classes) -> Unit)? = null
) : AdapterItem<ClassesViewHolder, Classes>(
    isType = { it is Classes },
    layoutRes = R.layout.item_classes,
    viewHolderGenerator = ::ClassesViewHolderImpl,
    itemBinder = ClassesItemBinder(context, onClickListener)
)