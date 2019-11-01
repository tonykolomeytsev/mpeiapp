package kekmech.ru.core.dto

data class AcademicDiscipline(
    var name: String = "",
    var controlWeeks: MutableList<ControlWeek> = mutableListOf(),
    var controlEvents: MutableList<ControlEvent> = mutableListOf(),
    var currentMark: Float = -1f,
    var examMark: Float = -1f,
    var finalComputedMark: Float = -1f,
    var finalFinalMark: Float = -1f
)