package kekmech.ru.lib_bars_parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element

private const val MARKS_CONTAINER_ID = "div-Student_SemesterSheet__Mark"
private const val SEMESTER_SELECT_ID = "ddl_StudyFilterSemester"
private const val SUMMARY_TABLE_ID = "tableMarkSummary"
private const val SUMMARY_BLOCK_ID = "div-Student_SemesterSheet__MarkSummary"
private const val DEADLINE_PREFIX = "сдать до"
private val STUDENT_ID = Regex("studentID=([0-9a-fA-F-]{36})", RegexOption.IGNORE_CASE)

/**
 * Parser of the pages of bars.mpei.ru. It only reads text: downloading, cookies and the sign-in are somebody else's job.
 *
 * The markup is the one of "ST_Study/Main/Main" (bootstrap 5, the disciplines are cards that toggle a collapsed table).
 * The parser tries not to depend on details: a row of a table is recognized by its text, not by its position.
 */
object BarsHtmlParser {

    fun detectPage(html: String): BarsPageKind {
        val document = Ksoup.parse(html)
        return when {
            document.getElementById(MARKS_CONTAINER_ID) != null -> BarsPageKind.MARKS
            document.getElementById(SUMMARY_TABLE_ID) != null -> BarsPageKind.SUMMARY
            "af2_code_send" in html || "JSON_SendAF2_Code" in html -> BarsPageKind.TWO_FACTOR
            document.select("input[type=password]").isNotEmpty() -> BarsPageKind.LOGIN
            parseStudentLinks(document).isNotEmpty() -> BarsPageKind.STUDENT_LIST
            else -> BarsPageKind.UNKNOWN
        }
    }

    /** @throws BarsParseException if [html] is not the marks page */
    fun parseMarksPage(html: String): BarsMarksPage {
        val document = Ksoup.parse(html)
        val container = document.getElementById(MARKS_CONTAINER_ID)
            ?: throw BarsParseException("There is no #$MARKS_CONTAINER_ID on the page, it is not the marks page")

        val header = document.selectFirst("#div-FormHeader .col-sm")
        val spans = header?.children()?.filter { it.tagName() == "span" && it.hasClass("fw-bold") }.orEmpty()
        return BarsMarksPage(
            studentName = spans.getOrNull(0)?.text()?.substringBefore('(')?.let(::normalizeBarsText)?.ifEmpty { null },
            studentGroup = spans.getOrNull(1)?.text()?.let(::normalizeBarsText)?.ifEmpty { null },
            meta = header?.let { parseMeta(it) }.orEmpty(),
            semesters = parseSemesters(document),
            disciplines = parseDisciplines(document, container),
            rating = parseRating(document),
        )
    }

    /**
     * The answer of the ajax request that the marks page makes when the semester is changed
     * (`_PartialListStudent_SemesterSheet__Mark`): the inside of the marks container, without the container itself.
     * A whole marks page is accepted too.
     */
    fun parseMarksPart(html: String): BarsMarksPart {
        val document = Ksoup.parse(html)
        val container = document.getElementById(MARKS_CONTAINER_ID) ?: document.body()
        return BarsMarksPart(
            semesterName = container.selectFirst("span.h3")?.text()?.let(::normalizeBarsText)?.ifEmpty { null },
            disciplines = parseDisciplines(document, container),
        )
    }

    /**
     * The rating block, null if there is none. It is on the marks page and in the answer of the ajax request that
     * changes the semester of the rating (`_PartialHeader`), so this works on a part of a page too.
     */
    fun parseRating(html: String): BarsRating? = parseRating(Ksoup.parse(html))

    /**
     * The page "Сводка" or the answer of its ajax request (`_PartialSummary`), which is only the part with the table.
     * @throws BarsParseException if there is no table of the summary in [html]
     */
    fun parseSummaryPage(html: String): BarsSummaryPage {
        val document = Ksoup.parse(html)
        val table = document.getElementById(SUMMARY_TABLE_ID)
            ?: throw BarsParseException("There is no #$SUMMARY_TABLE_ID in the text, it is not the summary")

        val weekHeaders = table.select("thead tr").lastOrNull()?.select("th").orEmpty()
        val weekNumbers = weekHeaders.mapNotNull { it.text().trim().toIntOrNull() }
        val currentWeek = weekHeaders.firstOrNull { it.hasClass("summary-td-current-week") }
            ?.text()?.trim()?.toIntOrNull()

        return BarsSummaryPage(
            semesterName = document.selectFirst("#$SUMMARY_BLOCK_ID h5")?.text()
                ?.let(::normalizeBarsText)?.removeSuffix(", успеваемость")?.ifEmpty { null },
            semesters = parseSemesters(document),
            currentWeek = currentWeek,
            rows = table.select("tbody tr")
                .filter { it.children().firstOrNull()?.hasClass("summary-td-row-header") == true }
                .mapNotNull { parseSummaryRow(it, weekNumbers) },
        )
    }

    private fun parseSummaryRow(row: Element, weekNumbers: List<Int>): BarsSummaryRow? {
        val cells = row.children().filter { it.tagName() == "td" }
        // the title, the weeks, then ТБ, ПА and И
        if (cells.size < 4) return null

        val (discipline, assessmentType) = splitTrailingBrackets(cells[0].text())
        val weekCells = cells.subList(1, cells.size - 3)
        val (currentScore, intermediate, finalMark) = cells.takeLast(3).map { parseMarkCell(it) }
        return BarsSummaryRow(
            discipline = discipline,
            assessmentType = assessmentType,
            weeks = weekCells.withIndex().mapNotNull { (index, cell) ->
                val marks = parseSummaryMarks(cell)
                if (marks.isEmpty()) null else BarsSummaryWeek(weekNumbers.getOrNull(index) ?: (index + 1), marks)
            },
            currentScore = currentScore,
            intermediate = intermediate,
            finalMark = finalMark,
        )
    }

    private fun parseSummaryMarks(cell: Element): List<BarsSummaryMark> {
        val spans = cell.select("span.summary-mark")
        if (spans.isNotEmpty()) {
            return spans.mapNotNull { span ->
                val mark = parseBarsMark(span.text()) ?: return@mapNotNull null
                BarsSummaryMark(title = normalizeBarsText(span.attr("title")).ifEmpty { null }, mark = mark)
            }
        }
        // No tooltips: the marks go through a space
        return normalizeBarsText(cell.text()).split(' ')
            .mapNotNull { parseBarsMark(it) }
            .map { BarsSummaryMark(title = null, mark = it) }
    }

    private fun parseRating(document: Document): BarsRating? {
        val total = document.getElementById("total_score") ?: return null
        val list = total.parents().firstOrNull { it.tagName() == "ul" } ?: return null
        val items = list.select("li.list-inline-item").mapNotNull { item ->
            val label = item.children().firstOrNull { it.tagName() == "span" } ?: return@mapNotNull null
            val value = item.selectFirst("strong") ?: return@mapNotNull null
            val raw = normalizeBarsText(value.text())
            BarsRatingItem(
                label = normalizeBarsText(label.text()).removeSuffix(":").trim(),
                value = parseBarsNumber(raw),
                raw = raw,
            )
        }
        return BarsRating(
            semesterId = total.attr("data-sem-id").ifEmpty { null },
            items = items,
            semesters = document.select("#divRatingSemesterFilter a[data-value]").map { option ->
                BarsSemester(
                    id = option.attr("data-value"),
                    name = normalizeBarsText(option.text()),
                    selected = option.hasClass("active"),
                )
            },
        )
    }

    /** Students of an account with several students. A single student is redirected to the marks page by BARS. */
    fun parseStudentList(html: String): List<BarsStudentLink> = parseStudentLinks(Ksoup.parse(html))

    private fun parseStudentLinks(document: Document): List<BarsStudentLink> =
        document.select("a[href]")
            .mapNotNull { link ->
                val id = STUDENT_ID.find(link.attr("href"))?.groupValues?.get(1) ?: return@mapNotNull null
                BarsStudentLink(id = id, name = normalizeBarsText(link.text()))
            }
            .distinctBy { it.id }

    private fun parseMeta(header: Element): List<BarsMetaItem> =
        (header.selectFirst("ul.list-inline") ?: header).select("li.list-inline-item").mapNotNull { item ->
            // The rating items (a span and a strong) may sit in the same list, they are not the meta
            if (item.children().any { it.tagName() == "span" }) return@mapNotNull null
            val text = normalizeBarsText(item.text())
            if (':' !in text) return@mapNotNull null
            val value = item.selectFirst("strong")?.text() ?: text.substringAfter(':')
            BarsMetaItem(description = text.substringBefore(':').trim(), value = normalizeBarsText(value))
        }

    private fun parseSemesters(document: Document): List<BarsSemester> {
        val options = document.select("select#$SEMESTER_SELECT_ID option")
            .ifEmpty { document.select("select[name=$SEMESTER_SELECT_ID] option") }
        return options.map { option ->
            BarsSemester(
                id = option.attr("value"),
                name = normalizeBarsText(option.text()),
                selected = option.hasAttr("selected"),
            )
        }
    }

    private fun parseDisciplines(document: Document, container: Element): List<BarsDiscipline> {
        val toggles = container.select("[data-bs-toggle=collapse][data-bs-target^=\"#s_ss_\"]")
        val pairs = if (toggles.isNotEmpty()) {
            toggles.mapNotNull { toggle ->
                val table = document.getElementById(toggle.attr("data-bs-target").removePrefix("#"))
                table?.let { toggle to it }
            }
        } else {
            pairBySiblings(container)
        }
        return pairs.map { (toggle, table) -> parseDiscipline(toggle, table) }
    }

    /** The old layout (and the one extract.js knew): a card div followed by the div.collapse with its table. */
    private fun pairBySiblings(container: Element): List<Pair<Element, Element>> {
        val blocks = container.children().filter { it.tagName() == "div" }
        return blocks.withIndex()
            .filter { (index, block) -> index > 0 && block.hasClass("collapse") }
            .map { (index, block) -> (blocks[index - 1].selectFirst("[data-bs-toggle]") ?: blocks[index - 1]) to block }
    }

    private fun parseDiscipline(toggle: Element, table: Element): BarsDiscipline {
        val header = parseDisciplineHeader(toggle.ownText())
        val badges = toggle.select("span.badge").map { normalizeBarsText(it.text()) }.filter { it.isNotEmpty() }
        val deadline = badges.firstOrNull { it.startsWith(DEADLINE_PREFIX) }?.removePrefix(DEADLINE_PREFIX)?.trim()
        return BarsDiscipline(
            name = header.name,
            person = header.person,
            assessmentType = header.assessmentType,
            units = header.units,
            deadline = deadline,
            notes = listOfNotNull(header.note) + badges.filter { !it.startsWith(DEADLINE_PREFIX) },
            activities = table.select("tr")
                .filter { row -> !row.hasClass("collapse") && row.parent()?.tagName() != "thead" }
                .mapNotNull { row -> parseActivity(row, table) },
        )
    }

    private fun parseActivity(row: Element, table: Element): BarsActivity? {
        val cells = row.children().filter { it.tagName() == "td" }
        val texts = cells.map { normalizeBarsText(it.text()) }
        val first = texts.firstOrNull() ?: return null

        return when {
            cells.size == 4 -> BarsActivity(
                type = BarsActivityType.CONTROL_ACTIVITY,
                name = controlActivityName(cells[0]),
                weight = texts[1].ifEmpty { null },
                weekNum = texts[2].ifEmpty { null },
                mark = parseMarkCell(cells[3]),
                criteria = findCriteria(cells[0], table),
            )
            first.startsWith("Балл текущего") -> summaryRow(BarsActivityType.CURRENT_SCORE, cells)
            first.startsWith("Контрольная неделя №") -> BarsActivity(
                type = BarsActivityType.CONTROL_WEEK,
                name = first,
                weight = null,
                weekNum = texts.getOrNull(1)?.ifEmpty { null },
                mark = cells.getOrNull(2)?.let { parseMarkCell(it) },
                criteria = null,
            )
            first.startsWith("Промежуточная") -> summaryRow(BarsActivityType.INTERMEDIATE_MARK, cells)
            first.startsWith("Итоговая") -> summaryRow(BarsActivityType.FINAL_MARK, cells)
            else -> null
        }
    }

    private fun summaryRow(type: BarsActivityType, cells: List<Element>) = BarsActivity(
        type = type,
        name = normalizeBarsText(cells[0].text()),
        weight = null,
        weekNum = null,
        mark = cells.getOrNull(1)?.let { parseMarkCell(it) },
        criteria = null,
    )

    /** The name without the "(критерии)" link that sits in the same cell. */
    private fun controlActivityName(cell: Element): String {
        val copy = cell.clone()
        copy.select("[data-bs-toggle], a").remove()
        return normalizeBarsText(copy.text()).removeSuffix("(критерии)").trim()
    }

    private fun findCriteria(cell: Element, table: Element): String? {
        val target = cell.selectFirst("[data-bs-target]")?.attr("data-bs-target")?.removePrefix("#") ?: return null
        // The ids (divMERes0, divMERes1, ...) repeat from table to table, so look inside this table only
        val row = table.select("tr").firstOrNull { it.id() == target } ?: return null
        return row.selectFirst(".tr-me-res")?.text()?.let(::normalizeBarsText)?.ifEmpty { null }
    }

    private fun parseMarkCell(cell: Element): BarsMark? {
        val retake = cell.selectFirst(".km-retaken")?.text()
        val own = cell.clone()
        own.select(".km-retaken").remove()
        return parseBarsMark(mainText = own.text(), retakeText = retake)
    }
}
