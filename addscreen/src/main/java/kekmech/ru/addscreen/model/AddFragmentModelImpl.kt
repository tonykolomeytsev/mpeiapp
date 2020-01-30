package kekmech.ru.addscreen.model

import kekmech.ru.core.dto.AcademGroup
import kekmech.ru.core.usecases.*
import java.util.*

class AddFragmentModelImpl constructor(
    private val getAllSchedulesUseCase: GetAllSchedulesUseCase,
    private val changeCurrentScheduleUseCase: ChangeCurrentScheduleUseCase,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val loadNewScheduleUseCase: LoadNewScheduleUseCase
) : AddFragmentModel {

    override suspend fun getGroupsAsync(): List<AcademGroup> {
        return getAllSchedulesUseCase()
            .map { AcademGroup(it.group.toUpperCase(Locale.getDefault()), it.id) }
    }

    override suspend fun setCurrentGroup(groupNumber: String) {
        changeCurrentScheduleUseCase(groupNumber)
    }

    override fun getGroupNumber() {
        getGroupNumberUseCase() // just
    }

    override suspend fun loadNewSchedule(groupNum: String): Boolean = loadNewScheduleUseCase(groupNum)

}