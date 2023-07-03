package kekmech.ru.library_app_database.migrations

import kekmech.ru.library_app_database.api.MigrationV6V7

internal class MigrationV6V7Impl(migrations: List<MigrationV6V7>) :
    BaseMigration<MigrationV6V7>(
        migrations = migrations,
        fromVersion = 6,
        toVersion = 7,
    )
