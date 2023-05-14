package kekmech.ru.mpeiapp

import android.content.Context
import io.kotest.core.spec.style.StringSpec
import kekmech.ru.mpeiapp.di.AppModule
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import java.io.File

@OptIn(KoinExperimentalAPI::class)
class KoinConfigurationCheck : StringSpec({
    "Check Koin DI configuration" {
        AppModule.verify(
            extraTypes = listOf(
                List::class,
                Map::class,
                File::class,
                Context::class,
            ),
        )
    }
})
