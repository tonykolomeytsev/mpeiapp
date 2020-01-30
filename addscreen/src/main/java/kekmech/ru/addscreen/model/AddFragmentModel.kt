package kekmech.ru.addscreen.model

import kekmech.ru.core.dto.AcademGroup

interface AddFragmentModel {
    suspend fun getGroupsAsync(): List<AcademGroup>

    suspend fun setCurrentGroup(groupNumber: String)
    fun getGroupNumber()

    /**
     * Returns false if error
     */
    suspend fun loadNewSchedule(groupNum: String): Boolean
}