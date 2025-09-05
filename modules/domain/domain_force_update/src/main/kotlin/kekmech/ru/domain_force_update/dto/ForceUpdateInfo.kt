package kekmech.ru.domain_force_update.dto

import java.io.Serializable

public data class ForceUpdateInfo(
    val actualVersion: AppVersion,
    val updateUrl: String,
    val shortDescription: String,
    val isUpdateRequired: Boolean
) : Serializable
