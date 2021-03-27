package kekmech.ru.bars.screen.main

import kekmech.ru.bars.screen.main.elm.BarsState

internal class BarsListConverter {

    fun map(state: BarsState): List<Any> {
        return if (state.userBars != null) {
            state.userBars.assessedDisciplines.orEmpty()
        } else emptyList()
    }
}