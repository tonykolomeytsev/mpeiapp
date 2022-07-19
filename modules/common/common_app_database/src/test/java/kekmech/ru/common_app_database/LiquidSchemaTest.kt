package kekmech.ru.common_app_database

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import kekmech.ru.common_app_database.migrations.Executor
import kekmech.ru.common_app_database.migrations.LiquidSchema
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.assertThrows

class LiquidSchemaTest : StringSpec({
    "Test create table if database is empty" {
        val executor = setAccumulatingExecutor()
        SCHEMA_1.migrate(executor)
        executor.setOfQueries.shouldContainExactly(SCHEMA_SQL_1)
    }
    "Test create TWO tables if database is empty" {
        val executor = setAccumulatingExecutor()
        SCHEMA_2.migrate(executor)
        executor.setOfQueries.shouldContainExactly(SCHEMA_SQL_1, SCHEMA_SQL_2)
    }
    "Test create second table if one table is exists" {
        val executor = setAccumulatingExecutor(
            mapOf(
                "students" to setOf("first_name", "last_name", "age", "_group")
            )
        )
        SCHEMA_2.migrate(executor)
        executor.setOfQueries.shouldContainExactly(SCHEMA_SQL_2)
    }
    "Test update TWO tables in database (add columns)" {
        val executor = setAccumulatingExecutor(
            mapOf(
                "students" to setOf("first_name", "last_name", "age", "_group"),
                "teachers" to setOf("first_name", "last_name", "groups")
            )
        )
        SCHEMA_3.migrate(executor)
        executor.setOfQueries.shouldContainExactly(SCHEMA_SQL_3)
    }
    "Test update one table and create second one" {
        val executor = setAccumulatingExecutor(
            mapOf(
                "students" to setOf("first_name", "last_name", "age", "_group")
            )
        )
        SCHEMA_3.migrate(executor)
        executor.setOfQueries.shouldContainExactly(SCHEMA_SQL_4)
    }
    "Test error if schema has no tables" {
        assertThrows<IllegalStateException> {
            LiquidSchema {
                /* no-op */
            }
        }
    }
    "Test error if schema has tables with duplicate names" {
        assertThrows<IllegalStateException> {
            LiquidSchema {
                table("test_table") {
                    column("test_column_1", textNotNull)
                }
                table("test_table") {
                    column("test_column_2", textNotNull)
                }
            }
        }
    }
    "Test error if schema has table without columns" {
        assertThrows<IllegalStateException> {
            LiquidSchema {
                table("test_table") {
                    /* no-op */
                }
            }
        }
    }
    "Test error if schema has table with duplicate columns" {
        assertThrows<IllegalStateException> {
            LiquidSchema {
                table("test_table") {
                    column("test_column", textNotNull)
                    column("test_column", textNotNull)
                }
            }
        }
    }
}) {
    private companion object {
        private val SCHEMA_1 = LiquidSchema {
            table("students") {
                column("first_name", textNotNull)
                column("last_name", textNotNull)
                column("age", integerNotNull)
                column("_group", textNotNull)
            }
        }
        @Language("RoomSql")
        private val SCHEMA_SQL_1 = """
            create table if not exists students (
            first_name text not null,
            last_name text not null,
            age integer not null,
            _group text not null );
        """.clear()

        private val SCHEMA_2 = LiquidSchema {
            table("students") {
                column("first_name", textNotNull)
                column("last_name", textNotNull)
                column("age", integerNotNull)
                column("_group", textNotNull)
            }
            table("teachers") {
                column("first_name", textNotNull)
                column("last_name", textNotNull)
                column("groups", textNotNull)
            }
        }
        @Language("RoomSql")
        private val SCHEMA_SQL_2 = """
            create table if not exists teachers ( 
            first_name text not null,
            last_name text not null,
            groups text not null );
        """.clear()

        private val SCHEMA_3 = LiquidSchema {
            table("students") {
                column("first_name", textNotNull)
                column("last_name", textNotNull)
                column("age", integerNotNull)
                column("_group", textNotNull)
                column("organisations", textNotNull)
            }
            table("teachers") {
                column("first_name", textNotNull)
                column("last_name", textNotNull)
                column("groups", textNotNull)
                column("age", integerNotNull)
            }
        }
        @Language("RoomSql")
        private val SCHEMA_SQL_3 = listOf(
            "alter table students add column organisations text not null;",
            "alter table teachers add column age integer not null;"
        )

        @Language("RoomSql")
        private val SCHEMA_SQL_4 = listOf(
            """ create table if not exists teachers ( 
                first_name text not null,
                last_name text not null,
                groups text not null,
                age integer not null );""".clear(),
            "alter table students add column organisations text not null;"
        )

        private fun String.clear() =
            replace("\n", " ")
                .replace("\\s{2,}".toRegex(), " ")
                .toLowerCase()
                .trim()

        private fun setAccumulatingExecutor(
            tablesAndColumns: Map<String, Set<String>> = emptyMap()
        ) = object : Executor {

            val setOfQueries = mutableSetOf<String>()

            override fun execSQL(sql: String) {
                setOfQueries += sql.clear()
            }

            override fun getTablesNames(): Set<String> = tablesAndColumns.keys

            override fun getColumnsNamesFor(tableName: String): Set<String> =
                tablesAndColumns[tableName].orEmpty()
        }
    }
}