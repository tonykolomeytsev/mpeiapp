package kekmech.ru.common_app_database.migrations

internal object Schema : LiquidMigration({
    table("notes") {
        column("_id", integerPrimaryKeyAutoIncrement)
        column("content", textNotNull)
        column("datetime", textNotNull)
        column("cls_name", textNotNull)
        column("grp_name", textNotNull)
        column("p_attachments", textDefaultNull)
        column("target", textDefaultNull)
    }
    table("favorite_schedules") {
        column("_id", integerPrimaryKeyAutoIncrement)
        column("grp_num", textNotNull)
        column("description", textNotNull)
        column("ord", integerNotNull)
    }
})