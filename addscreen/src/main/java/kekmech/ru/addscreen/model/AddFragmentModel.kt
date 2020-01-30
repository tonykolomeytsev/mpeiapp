package kekmech.ru.addscreen.model

import kekmech.ru.core.dto.AcademGroup

interface AddFragmentModel {
    suspend fun getGroupsAsync(): List<AcademGroup>

    suspend fun setCurrentGroup(groupNumber: String)
    fun getGroupNumber()

    suspend fun loadNewSchedule(groupNum: String)
}