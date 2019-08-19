package kekmech.ru.repository

import io.realm.Realm
import kekmech.ru.core.Repository
import javax.inject.Inject

class RepositoryImpl(
    val realm: Realm
) : Repository