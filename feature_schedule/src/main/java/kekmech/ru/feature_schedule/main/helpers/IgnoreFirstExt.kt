package kekmech.ru.feature_schedule.main.helpers

object IgnoreFirst {
    val actionsToIgnore = mutableMapOf<() -> Unit, Boolean>()
}

fun ignoreFirst(action: () -> Unit) {
    if (IgnoreFirst.actionsToIgnore.getOrDefault(action, false)) {
        action()
    } else {
        IgnoreFirst.actionsToIgnore[action] = true
    }
}