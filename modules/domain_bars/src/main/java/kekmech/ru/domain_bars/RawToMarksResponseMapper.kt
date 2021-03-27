package kekmech.ru.domain_bars

import kekmech.ru.domain_bars.dto.*
import kekmech.ru.domain_bars.dto.DisciplineRowType.CONTROL_ACTIVITY
import kekmech.ru.domain_bars.dto.DisciplineRowType.UNDEFINED

object RawToMarksResponseMapper {

    fun map(rawMarksResponse: RawMarksResponse) = MarksResponse(
        payload = rawMarksResponse.payload.map { disciplineRow ->
            AssessedDiscipline(
                name = disciplineRow.disciplineName,
                person = disciplineRow.personName,
                assessmentType = disciplineRow.assessmentType,
                controlActivities = disciplineRow.activities
                    .filter { it.type == CONTROL_ACTIVITY }
                    .map { caRow ->
                        ControlActivity(
                            name = caRow.name.orEmpty(),
                            weight = caRow.weight?.toFloatOrNull() ?: -1f,
                            deadline = caRow.weekNum.orEmpty(),
                            finalMark = caRow.markAndDate?.extractFloatOfNull() ?: -1f
                        )
                    },
                finalGrades = disciplineRow.activities
                    .filter { it.type != CONTROL_ACTIVITY && it.type != UNDEFINED }
                    .map { aRow ->
                        FinalGrade(
                            name = aRow.name.orEmpty(),
                            finalMark = aRow.markAndDate?.extractFloatOfNull() ?: -1f,
                            type = FinalGradeType.valueOf(aRow.type.name)
                        )
                    }
            )
        }
    )

    private fun String.extractFloatOfNull() = toFloatOrNull()
        ?: split("[\\s\\(]+".toRegex())[0].toFloatOrNull()
}