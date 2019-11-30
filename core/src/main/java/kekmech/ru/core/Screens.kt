package kekmech.ru.core

enum class Screens {
    FEED,
    TIMETABLE,
    SETTINGS,
    ADD,
    MAP,

    // actions
    ADD_TO_FEED,
    FEED_TO_ADD,
    FEED_TO_FORCE,
    FEED_TO_NOTE,

    //bars deep nav
    BARS_TO_RIGHTS,
    BARS_TO_BARS_DETAILS,
    BARS_TO_FORCE,

    // timetable deep nav
    TIMETABLE_TO_FORCE,
    TIMETABLE_TO_NOTE
}