package kekmech.ru.common_app_database.migrations

import kekmech.ru.common_app_database_migrations.MigrationV6V7

internal class MigrationV6V7Impl(migrations: List<MigrationV6V7>) :
    BaseMigration<MigrationV6V7>(
        migrations = migrations,
        fromVersion = 6,
        toVersion = 7,
    )
