package kekmech.ru.core.dto

data class ControlEvent(
    val name: String = "",
    val weight: Float = 0f,
    val week: Int = 0,
    val mark: Float = -1f
)