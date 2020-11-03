package kekmech.ru.coreui

import android.content.Context
import android.util.Log

object Resources {
    @Suppress("DEPRECATION")
    fun getColor(context: Context?, colorId: Int): Int {
        if (context == null) {
            Log.e(this::class.java.simpleName, "Does not found color resource")
            return 0
        }
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            context.getColor(colorId)
        } else{
            context.resources.getColor(colorId)
        }
    }

    fun getStringArray(context: Context?, stringArrayId: Int): Array<String> {
        if (context == null) {
            Log.e(this::class.java.simpleName, "Does not found string array resource")
            return emptyArray()
        }
        return context.resources.getStringArray(stringArrayId)
    }
}