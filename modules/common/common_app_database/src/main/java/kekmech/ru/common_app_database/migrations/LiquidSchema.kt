package kekmech.ru.common_app_database.migrations

import org.intellij.lang.annotations.Language

internal class TableScope {

    private val columns = mutableListOf<Column>()

    val integerPrimaryKeyAutoIncrement = "integer primary key autoincrement"
    val integerNotNull = "integer not null"
    val textNotNull = "text not null"
    val textDefaultNull = "text default null"

    fun column(name: String, type: String) {
        columns += Column(name, type)
    }

    fun get() = columns
}

internal class GeneralScope {

    private val tables = mutableListOf<Table>()


    fun table(name: String, columnsProvider: TableScope.() -> Unit) {
        tables += Table(name, TableScope().let { scope ->
            columnsProvider(scope)
            scope.get()
        })
    }

    fun get() = tables
}

internal data class Table(val name: String, val columns: List<Column>) {

    @Language("RoomSql")
    fun sqlQuery(): String {
        val columnsSqlQuery = columns.joinToString(", ") { "${it.name} ${it.type}" }
        return "CREATE TABLE IF NOT EXISTS $name ( $columnsSqlQuery );"
    }

    override fun toString() = "table $name (\n    " +
            columns.joinToString(",\n    ") { "${it.name} ${it.type}" } + "\n)"
}

internal data class Column(val name: String, val type: String)



internal open class LiquidSchema(tablesProvider: GeneralScope.() -> Unit) {

    private val tables = GeneralScope().let { scope ->
        tablesProvider(scope)
        scope.get()
    }

    init {
        check(tables.isNotEmpty())
        val tablesNames = tables.map { it.name }
        check(tablesNames.toSet().size == tablesNames.size) { "All tables names must be unique!" }
        check(tables.all { it.columns.isNotEmpty() }) { "All tables must have at least one column!" }
        check(tables.all { table -> table.columns.let { it.toSet().size == it.size } }) {
            "All columns in a table must be unique!"
        }
    }

    fun migrate(db: Executor) {
        val tablesForUpdate = createTablesIfNotExist(db)
        createColumnsIfNotExist(db, tablesForUpdate)
    }

    private fun createTablesIfNotExist(db: Executor): Set<String> {
        val necessaryTablesNames = tables.map { it.name }.toSet()
        val existingTablesNames = db.getTablesNames()
        val tablesToCreate = necessaryTablesNames - existingTablesNames
        tablesToCreate
            .map { name -> tables.first { it.name == name } }
            .forEach { db.execSQL(it.sqlQuery()) }
        return necessaryTablesNames - tablesToCreate
    }

    private fun createColumnsIfNotExist(db: Executor, tablesForUpdate: Set<String>) {
        for (tableName in tablesForUpdate) {
            val table = tables.first { it.name == tableName }
            val necessaryColumnsNames = table.columns.map { it.name }.toSet()
            val existingColumnsNames = db.getColumnsNamesFor(table.name)
            val columnsToCreate = necessaryColumnsNames - existingColumnsNames
            columnsToCreate
                .map { name -> table.columns.first { it.name == name } }
                .forEach { (columnName, columnType) ->
                    db.execSQL("ALTER TABLE $tableName ADD COLUMN $columnName $columnType;")
                }
        }
    }
}
