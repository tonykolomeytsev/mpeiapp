package kekmech.ru.repository.di

import android.arch.persistence.room.Room
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
        .build()

}