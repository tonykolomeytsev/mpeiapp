package kekmech.ru.domain_bars

import kekmech.ru.common_kotlin.capitalizeSafe
import kekmech.ru.domain_bars.dto.*
import kekmech.ru.domain_bars.dto.DisciplineRowType.CONTROL_ACTIVITY
import kekmech.ru.domain_bars.dto.DisciplineRowType.UNDEFINED

internal object RawToMarksResponseMapper {

    fun map(rawMarksResponse: RawMarksResponse) = MarksResponse(
        payload = rawMarksResponse.payload.map { disciplineRow ->
            AssessedDiscipline(
                name = disciplineRow.disciplineName,
                person = disciplineRow.personName,
                assessmentType = disciplineRow.assessmentType.capitalizeSafe(),
                controlActivities = disciplineRow.activities
                    .filter { it.type == CONTROL_ACTIVITY }
                    .map { caRow ->
                        ControlActivity(
                            name = caRow.name.orEmpty(),
                            weight = caRow.weight?.trim().orEmpty(),
                            deadline = caRow.weekNum?.extractIntOrNull()?.toString().orEmpty(),
                            finalMark = caRow.markAndDate?.extractFloatOrNull() ?: -1f
                        )
                    },
                finalGrades = disciplineRow.activities
                    .filter { it.type != CONTROL_ACTIVITY && it.type != UNDEFINED }
                    .map { aRow ->
                        FinalGrade(
                            name = aRow.name.orEmpty(),
                            finalMark = aRow.markAndDate?.extractFloatOrNull() ?: -1f,
                            type = FinalGradeType.valueOf(aRow.type.name)
                        )
                    }
            )
        }
    )

    private fun String.extractFloatOrNull() = replace(',', '.').let {
        it.toFloatOrNull()
            ?: it.split("[\\s(/]+".toRegex())[0].toFloatOrNull()
    }

    private fun String.extractIntOrNull() = toIntOrNull()
        ?: split("[\\s(/]+".toRegex())[0].toIntOrNull()
}
