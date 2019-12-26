package kekmech.ru.repository.di

import androidx.room.Room
import android.content.Context
import kekmech.ru.repository.room.AppDatabase

object AppDatabaseModule {
    fun provideAppDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "mpeiapp.db")
        .addMigrations(AppDatabase.MIGRATION_V1_TO_V2)
        .build()
}