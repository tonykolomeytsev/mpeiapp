package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.GetLoginScriptUseCase

class GetLoginScriptUseCaseImpl(
    private val barsRepository: BarsRepository
) : GetLoginScriptUseCase {
    override operator fun invoke() = barsRepository.getLoginScript()
}