package kekmech.ru.core.usecases

import kekmech.ru.core.gateways.PicassoFirebaseInstance

interface GetPicassoInstanceUseCase {
    operator fun invoke(): PicassoFirebaseInstance
}