package kekmech.ru.core.gateways

interface ScheduleRemoteGateway {
    fun get(dayNum: Int)
}