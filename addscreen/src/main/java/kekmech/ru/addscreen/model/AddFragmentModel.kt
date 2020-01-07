package kekmech.ru.addscreen.model

import kekmech.ru.core.dto.AcademGroup

interface AddFragmentModel {
    suspend fun getGroupsAsync(): List<AcademGroup>

    suspend fun setCurrentGroup(id: Int)
    fun getGroupNumber()

    suspend fun loadNewSchedule(groupNum: String)
}