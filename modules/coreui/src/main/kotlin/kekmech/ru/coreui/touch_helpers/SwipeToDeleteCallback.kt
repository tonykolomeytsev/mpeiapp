package kekmech.ru.coreui.touch_helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.getResColor
import kekmech.ru.common_android.getThemeColor
import kekmech.ru.coreui.R

/**
 * According to https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
 */
public abstract class SwipeToDeleteCallback(
    context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    @Suppress("UnsafeCallOnNullableType")
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_24px)!!
    private val intrinsicWidth = deleteIcon.intrinsicWidth
    private val intrinsicHeight = deleteIcon.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = context.getThemeColor(R.attr.colorRed)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private val iconTintColor = context.getResColor(R.color.colorWhite)

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (!isTypeForDelete(viewHolder.bindingAdapterPosition, viewHolder.itemViewType)) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.setTint(iconTintColor)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        c.drawRect(left, top, right, bottom, clearPaint)
    }

    public abstract fun isTypeForDelete(adapterPosition: Int, itemViewType: Int): Boolean
}

public fun RecyclerView.attachSwipeToDeleteCallback(
    isItemForDelete: (Any) -> Boolean,
    callback: (Any) -> Unit
) {
    val baseAdapter = (adapter as? BaseAdapter)
        ?: error("This function can only be used with RecyclerView with BaseAdapter. " +
                "Your adapter is ${adapter?.javaClass?.simpleName}.")
    val swipeHandler = object : SwipeToDeleteCallback(context) {

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = baseAdapter.allData.getOrNull(viewHolder.bindingAdapterPosition)
            item?.let(callback)
        }

        override fun isTypeForDelete(adapterPosition: Int, itemViewType: Int): Boolean {
            val item = baseAdapter.allData.getOrNull(adapterPosition)
            return item?.let(isItemForDelete) ?: false
        }
    }
    val itemTouchHelper = ItemTouchHelper(swipeHandler)
    itemTouchHelper.attachToRecyclerView(this)
}
