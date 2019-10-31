package kekmech.ru.repository

import kekmech.ru.core.repositories.BarsRepository
import org.jsoup.Jsoup
import javax.inject.Inject

class BarsRepositoryImpl @Inject constructor() : BarsRepository {


    private fun loadFromBars() {
        val mainPage = Jsoup.connect("https://bars.mpei.ru/bars_web/")
            .get()
        val stoken = mainPage
            .select("input[name=SToken]")
            .`val`()
        val requestVirificationToken = mainPage
            .select("input[name=__RequestVerificationToken]")
            .`val`()
        println(stoken)
        println(requestVirificationToken)

        val response = Jsoup.connect("https://bars.mpei.ru/bars_web/")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .data("Password", "")
            .data("Username", "")
            .data("Remember", "false")
            .data("SToken", stoken)
            .data("__RequestVerificationToken", requestVirificationToken)
            .post()
    }
}