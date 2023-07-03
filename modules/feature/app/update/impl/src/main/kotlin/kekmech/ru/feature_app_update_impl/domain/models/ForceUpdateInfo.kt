package kekmech.ru.feature_app_update_impl.domain.models

import java.io.Serializable

data class ForceUpdateInfo(
    val actualVersion: AppVersion,
    val updateUrl: String,
    val shortDescription: String,
    val isUpdateRequired: Boolean
) : Serializable
