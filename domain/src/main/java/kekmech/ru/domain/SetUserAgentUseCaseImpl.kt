package kekmech.ru.domain

import kekmech.ru.core.repositories.BarsRepository
import kekmech.ru.core.usecases.SetUserAgentUseCase

class SetUserAgentUseCaseImpl(
    private val barsRepository: BarsRepository
) : SetUserAgentUseCase {
    override fun invoke(us: String) {
        barsRepository.setUserAgent(us)
    }
}