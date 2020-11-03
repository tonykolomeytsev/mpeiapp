package kekmech.ru.common_android.views

import androidx.appcompat.widget.SearchView

fun SearchView.onQueryTextChange(action: (query: String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String): Boolean {
            action(newText)
            return false
        }

        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }
    })
}