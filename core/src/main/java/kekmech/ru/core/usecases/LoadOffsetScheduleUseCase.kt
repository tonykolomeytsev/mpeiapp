package kekmech.ru.core.usecases

import kekmech.ru.core.dto.Couple
import kekmech.ru.core.UseCase

interface LoadOffsetScheduleUseCase : UseCase<Int, List<Couple>>