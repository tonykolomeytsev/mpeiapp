package kekmech.ru.addscreen.model

import kekmech.ru.core.dto.AcademGroup
import kekmech.ru.core.usecases.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AddFragmentModelImpl constructor(
    private val getAllSchedulesUseCase: GetAllSchedulesUseCase,
    private val changeCurrentScheduleUseCase: ChangeCurrentScheduleUseCase,
    private val getGroupNumberUseCase: GetGroupNumberUseCase,
    private val loadNewScheduleUseCase: LoadNewScheduleUseCase,
    private val invokeUpdateScheduleUseCase: InvokeUpdateScheduleUseCase
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

    override fun launchUpdate() {
        GlobalScope.launch(IO) {
            try {
                invokeUpdateScheduleUseCase()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override suspend fun loadNewSchedule(groupNum: String): Boolean = loadNewScheduleUseCase(groupNum)

}