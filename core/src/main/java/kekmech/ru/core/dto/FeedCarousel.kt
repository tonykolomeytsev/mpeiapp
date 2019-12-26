package kekmech.ru.core.dto

data class FeedCarousel(
    var items: List<Item> = emptyList()
) {
    data class Item(
        var name: String = "",
        var link: String = "",
        var type: String = "",
        var image_url: String = "",
        var index: Int = 999
    )
}