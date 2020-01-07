package kekmech.ru.addscreen.model

import kekmech.ru.core.dto.AcademGroup
import kekmech.ru.core.usecases.ChangeCurrentScheduleIdUseCase
import kekmech.ru.core.usecases.GetAllSchedulesUseCase
import kekmech.ru.core.usecases.GetGroupNumberUseCase
import kekmech.ru.core.usecases.LoadNewScheduleUseCase
import java.util.*

class AddFragmentModelImpl constructor(
    private val getAllSchedulesUseCase: GetAllSchedulesUseCase,
    private val changeCurrentScheduleIdUseCase: ChangeCurrentScheduleIdUseCase,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val loadNewScheduleUseCase: LoadNewScheduleUseCase
) : AddFragmentModel {

    override suspend fun getGroupsAsync(): List<AcademGroup> {
        return getAllSchedulesUseCase()
            .map { AcademGroup(it.group.toUpperCase(Locale.getDefault()), it.id) }
    }

    override suspend fun setCurrentGroup(id: Int) {
        changeCurrentScheduleIdUseCase(id)
    }

    override fun getGroupNumber() {
        getGroupNumberUseCase() // just
    }

    override suspend fun loadNewSchedule(groupNum: String) = loadNewScheduleUseCase(groupNum)

}