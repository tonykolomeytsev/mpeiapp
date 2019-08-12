package kekmech.ru.core

import android.content.Context

interface ContextProvider {
    fun provideContext(): Context
}