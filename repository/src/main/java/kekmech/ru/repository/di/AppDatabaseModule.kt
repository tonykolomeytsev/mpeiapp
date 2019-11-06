package kekmech.ru.repository.di

import androidx.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import kekmech.ru.repository.room.AppDatabase
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, "mpeiapp.db")
        .addMigrations(AppDatabase.MIGRATION_V1_TO_V2)
        .build()

}