package kekmech.ru.lib_navigation

import kotlinx.coroutines.flow.Flow

public interface BottomTabsSwitcher {

    /**
     * Заменяет весь back stack на корневой экран таба [tab].
     * Повторное нажатие на уже открытый таб отправляет событие в [reselects].
     */
    public fun changeTab(tab: BottomTab)

    public val reselects: Flow<BottomTab>
}
