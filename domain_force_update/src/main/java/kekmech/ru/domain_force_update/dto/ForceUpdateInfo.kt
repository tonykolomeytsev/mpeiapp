package kekmech.ru.domain_force_update.dto

data class ForceUpdateInfo(
    val actualVersion: AppVersion,
    val updateUrl: String,
    val shortDescription: String,
    val isUpdateRequired: Boolean
)