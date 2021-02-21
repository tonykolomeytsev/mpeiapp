package kekmech.ru.common_app_database.migrations

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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

    override fun toString() = "table $name (\n" + columns.joinToString("\n    ") + ")"
}

internal data class Column(val name: String, val type: String)

internal interface Executor {
    fun getTablesNames(): Set<String>
    fun getColumnsNamesFor(tableName: String): Set<String>
    fun execSQL(@Language("RoomSql") sql: String)
}

internal class AndroidSQLiteExecutor(private val db: SQLiteDatabase) : Executor {

    override fun execSQL(sql: String) = db.execSQL(sql)

    override fun getTablesNames(): Set<String> {
        val existingTablesNames = mutableSetOf<String>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table';", null)
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    existingTablesNames += cursor.getString(0)
                    cursor.moveToNext()
                }
            }
        } finally {
            cursor?.close()
        }
        return existingTablesNames
    }

    override fun getColumnsNamesFor(tableName: String): Set<String> {
        val existingColumnsNames = mutableSetOf<String>()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName LIMIT 1;", null)
            existingColumnsNames.addAll(cursor.columnNames)
        } finally {
            cursor?.close()
        }
        return existingColumnsNames
    }
}

internal open class LiquidMigration(tablesProvider: GeneralScope.() -> Unit) {

    private val tables = GeneralScope().let { scope ->
        tablesProvider(scope)
        scope.get()
    }

    init {
        tables.forEach(::println)
        val tablesNames = tables.map { it.name }
        check(tablesNames.toSet().size == tablesNames.size) { "All tables name should be unique!" }
        check(tables.all { it.columns.isNotEmpty() }) { "All tables should have at least one column!" }
    }

    fun migrate(db: Executor) {
        createTablesIfNotExist(db)
        createColumnsIfNotExist(db)
    }

    private fun createTablesIfNotExist(db: Executor) {
        val necessaryTablesNames = tables.map { it.name }.toSet()
        val existingTablesNames = db.getTablesNames()
        val tablesToCreate = necessaryTablesNames - existingTablesNames
        tablesToCreate
            .map { name -> tables.first { it.name == name } }
            .forEach { db.execSQL(it.sqlQuery()) }
    }

    private fun createColumnsIfNotExist(db: Executor) {
        for (table in tables) {
            val necessaryColumnsNames = table.columns.map { it.name }.toSet()
            val existingColumnsNames = db.getColumnsNamesFor(table.name)
            val columnsToCreate = necessaryColumnsNames - existingColumnsNames
            columnsToCreate
                .map { name -> table.columns.first { it.name == name } }
                .forEach { (columnName, columnType) ->
                    db.execSQL("ALTER TABLE ${table.name} ADD COLUMN $columnName $columnType;")
                }
        }
    }
}