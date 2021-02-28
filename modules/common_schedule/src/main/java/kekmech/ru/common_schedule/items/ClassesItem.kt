package kekmech.ru.common_schedule.items

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kekmech.ru.common_android.dpToPx
import kekmech.ru.common_android.getResColor
import kekmech.ru.common_android.getStringArray
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.common_android.viewbinding.ReusableViewHolder
import kekmech.ru.common_android.viewbinding.lazyBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_schedule.R
import kekmech.ru.common_schedule.drawable.ProgressBackgroundDrawable
import kekmech.ru.coreui.items.ClickableItemViewHolder
import kekmech.ru.coreui.items.ClickableItemViewHolderImpl
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.ClassesType
import kekmech.ru.domain_schedule.dto.ScheduleType
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
    fun setProgress(progress: Float)
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

    override fun setProgress(progress: Float) {
        val context = containerView.context
        val bg = containerView.findViewById<View>(R.id.constraintLayout)
        if (bg.background !is ProgressBackgroundDrawable) {
            val progressBackgroundDrawable = ProgressBackgroundDrawable(
                context,
                context.getThemeColor(R.attr.colorGray10),
                context.getResColor(R.color.colorMain),
                cornerRadius = ProgressBackgroundDrawable.CornerRadius
                    .of(context.resources.dpToPx(PROGRESS_BACKGROUND_CORNER_RADIUS).toFloat())
            )
            bg.background = progressBackgroundDrawable
            progressBackgroundDrawable.progress = progress
        } else {
            val progressBackgroundDrawable = bg.background as ProgressBackgroundDrawable
            progressBackgroundDrawable.progress = progress
        }
    }

    private companion object {
        private const val PROGRESS_BACKGROUND_CORNER_RADIUS = 7f
    }
}

@Suppress("MagicNumber")
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
        vh.setPersonName(when (model.scheduleType) {
            ScheduleType.GROUP -> model.person.takeIfNotEmpty()
            ScheduleType.PERSON -> model.groups.takeIfNotEmpty()
        })
        vh.setPlace(model.place)
        vh.setNumberName(classesNumbers.getOrElse(model.number) { classesNumbers[0] }.toUpperCase())
        vh.setTypeName(getClassesTypeName(model).toUpperCase())
        vh.setTagColor(colorTags.getValue(model.type))
        vh.setStartTime(model.time.start.format(timeFormatter))
        vh.setEndTime(model.time.end.format(timeFormatter))
        vh.setOnClickListener { onClickListener?.invoke(model) }
        model.progress?.let { vh.setProgress(it) }
    }

    private fun String.takeIfNotEmpty() = takeIf {
        isNotBlank() && !it.contains("Вакансия", ignoreCase = true)
    }

    private fun getClassesTypeName(classes: Classes): String {
        return if (classes.type == ClassesType.UNDEFINED && classes.rawType != null) {
            classes.rawType.orEmpty()
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