package kekmech.ru.bars.screen.main

import kekmech.ru.bars.items.MenusItem
import kekmech.ru.bars.items.UserNameHeaderItem
import kekmech.ru.bars.screen.main.elm.BarsState
import kekmech.ru.coreui.items.SpaceItem

internal class BarsListConverter {

    fun map(state: BarsState): List<Any> {
        return if (state.userBars != null) {
            listOf(UserNameHeaderItem(state.userBars.name.orEmpty()), SpaceItem.VERTICAL_16, MenusItem, SpaceItem.VERTICAL_12) + state.userBars.assessedDisciplines.orEmpty()
        } else emptyList()
    }
}