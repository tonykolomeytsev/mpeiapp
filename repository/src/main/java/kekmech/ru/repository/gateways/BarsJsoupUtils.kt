package kekmech.ru.repository.gateways

import org.jsoup.Jsoup

object BarsJsoupUtils {

    fun hiddenFieldsExtractor(login: String, password: String): (String) -> Map<String, String> = {
        Jsoup.parse(it).select("input[type=hidden]")
            .map { it.attr("name") to it.`val`() }
            .toMap()
            .toMutableMap()
            .apply { this["Username"] = login; this["Password"] = password }
    }

    fun studentPageLocationExtractor(): (String) -> String = { html ->
        println(html)
        Jsoup.parse(html).select("table[id*=tbl__PartialListStudent] > tbody > tr")
            .map { tr -> tr.select("td").let { td -> td[2].html() to td[4].select("a").attr("href") } }
            .map { (groupName, href) -> (".*-.*-(.*)".toRegex().find(groupName)?.groups?.get(1)?.value ?: "0").toInt() to href }
            .maxBy { it.first }
            ?.let { "https://bars.mpei.ru/" + it.second }
            ?: ""
    }

    fun semesterListExtractor(): (String) -> Map<String, String> = { html ->
        Jsoup.parse(html).select("select[class*=ddl_FilterSemester] > option")
            .map { it.`val`().toString() to it.html() }
            .toMap()
            .apply { println(this) }
    }


}