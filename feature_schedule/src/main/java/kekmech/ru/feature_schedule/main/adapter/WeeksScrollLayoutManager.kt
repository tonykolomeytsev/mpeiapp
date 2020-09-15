package kekmech.ru.feature_schedule.main.adapter

import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WeeksScrollLayoutManager(context: Context) : LinearLayoutManager(context, HORIZONTAL, false) {

    private val extraSpaceLeft: Int = 0
    private val extraSpaceRight: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.resources.displayMetrics).toInt()

    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        extraLayoutSpace[0] = extraSpaceLeft
        extraLayoutSpace[1] = extraSpaceRight
    }
}