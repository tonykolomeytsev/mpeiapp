package kekmech.ru.lib_bars_parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** The pages here are made up, they repeat the markup of bars.mpei.ru, not anybody's data. */
class BarsRatingSummaryTest {

    @Test
    fun `reads the rating next to the meta list`() {
        val page = BarsHtmlParser.parseMarksPage(PAGE_WITH_RATING)

        assertEquals(listOf(BarsMetaItem("Курс", "2")), page.meta)
        assertRating(assertNotNull(page.rating))
    }

    @Test
    fun `reads the rating when it is in the same list as the meta`() {
        val page = BarsHtmlParser.parseMarksPage(PAGE_WITH_RATING_IN_ONE_LIST)

        // the rating items must not get into the meta
        assertEquals(listOf(BarsMetaItem("Курс", "2")), page.meta)
        assertRating(assertNotNull(page.rating))
    }

    @Test
    fun `reads the rating of a part of a page`() {
        assertRating(assertNotNull(BarsHtmlParser.parseRating(PAGE_WITH_RATING)))
    }

    @Test
    fun `no rating on a page without it`() {
        assertNull(BarsHtmlParser.parseRating("<html><body><p>Hello</p></body></html>"))
        assertNull(BarsHtmlParser.parseMarksPage(PAGE_WITHOUT_RATING).rating)
    }

    @Test
    fun `detects and reads the summary`() {
        assertEquals(BarsPageKind.SUMMARY, BarsHtmlParser.detectPage(SUMMARY_PART))

        val summary = BarsHtmlParser.parseSummaryPage(SUMMARY_PART)

        assertEquals("2025/2026, Весенний семестр", summary.semesterName)
        assertEquals(listOf("28", "27"), summary.semesters.map { it.id })
        assertEquals("27", summary.semesters.single { it.selected }.id)
        assertEquals(3, summary.currentWeek)
        assertEquals(2, summary.rows.size)
    }

    @Test
    fun `reads a row of the summary with the tooltips`() {
        val row = BarsHtmlParser.parseSummaryPage(SUMMARY_PART).rows[0]

        assertEquals("Дискретная математика", row.discipline)
        assertEquals("экзамен", row.assessmentType)

        val week = row.weeks.single()
        assertEquals(2, week.week)
        assertEquals(listOf("Контрольная работа №1", "Контрольная работа №2"), week.marks.map { it.title })
        assertEquals(listOf(4f, 5f), week.marks.map { it.mark.value })

        assertEquals(4.5f, row.currentScore?.value)
        assertEquals(5f, row.intermediate?.value)
        assertEquals(5f, row.finalMark?.value)
    }

    @Test
    fun `reads a row of the summary without the tooltips and with a note`() {
        val row = BarsHtmlParser.parseSummaryPage(SUMMARY_PART).rows[1]

        assertEquals("Физика (часть 1)", row.discipline)
        assertEquals("зачёт с оценкой", row.assessmentType)

        val week = row.weeks.single()
        assertEquals(1, week.week)
        assertNull(week.marks.single().title)
        assertEquals(3f, week.marks.single().mark.value)

        assertEquals(3f, row.currentScore?.value)
        assertNull(row.intermediate)
        assertEquals(3f, row.finalMark?.value)
        assertEquals("(ППА1)", row.finalMark?.note)
    }

    @Test
    fun `a semester without marks gives an empty summary`() {
        val summary = BarsHtmlParser.parseSummaryPage(SUMMARY_EMPTY)

        assertTrue(summary.rows.isEmpty())
        assertEquals("2026/2027, Осенний семестр", summary.semesterName)
    }

    @Test
    fun `refuses a page that is not the summary`() {
        assertFailsWith<BarsParseException> { BarsHtmlParser.parseSummaryPage("<html><body></body></html>") }
    }

    private fun assertRating(rating: BarsRating) {
        assertEquals("28", rating.semesterId)
        assertEquals(
            listOf(
                "Учебная деятельность",
                "Учебный",
                "Учебный за семестр",
                "Комплексный",
                "Комплексный за семестр",
            ),
            rating.items.map { it.label },
        )
        assertEquals(12f, rating.studyActivity)
        assertEquals(11.5f, rating.study)
        assertEquals(12f, rating.studySemester)
        assertEquals(6f, rating.complex)
        assertEquals(7f, rating.complexSemester)
        assertEquals("11,5", rating.items[1].raw)
        assertNull(rating.value("Такого нет"))

        assertEquals(listOf("28", "27"), rating.semesters.map { it.id })
        assertEquals(listOf(true, false), rating.semesters.map { it.selected })
    }

    private companion object {

        const val RATING_LIST = """
    <li class="list-inline-item"><strong>Рейтинг</strong></li>
    <li class="list-inline-item"><span>Учебная деятельность</span><strong><span id="RST_Study">12,0</span></strong></li>
    <li class="list-inline-item"><span>Учебный</span><strong><span id="RST">11,5</span></strong></li>
    <li class="list-inline-item"><span>Учебный за семестр</span><strong><span id="RST">12,0</span></strong></li>
    <li></li>
    <li class="list-inline-item"><span>Комплексный:</span><strong><span id="total_score" data-sem-id="28">6</span></strong></li>
    <li class="list-inline-item"><span>Комплексный за семестр:</span><strong><span id="total_score_sem" data-sem-id="28">7</span></strong></li>
    <li class="list-inline-item"><div class="btn-group"><button type="button" class="btn dropdown-toggle"><span class="caret">2026/2027-1</span></button>
      <div id="divRatingSemesterFilter" class="dropdown-menu">
        <a class="dropdown-item active" data-value="28">2026/2027, Осенний семестр</a>
        <a class="dropdown-item" data-value="27">2025/2026, Весенний семестр</a>
      </div></div></li>
"""

        const val PAGE_WITH_RATING = """
<html><body>
<div id="div-FormHeader"><div class="row"><div class="col-sm">
  <span class="fw-bold">Иванов Иван Иванович (№ЗК - 0000000000)</span>
  <span class="fw-bold">ТЕСТ-01-25</span>
  <ul class="list-inline mb-0"><li class="list-inline-item">Курс: <strong>2</strong></li></ul>
  <ul class="list-inline mb-0">$RATING_LIST</ul>
</div></div></div>
<div id="div-Student_SemesterSheet__Mark"></div>
</body></html>
"""

        const val PAGE_WITH_RATING_IN_ONE_LIST = """
<html><body>
<div id="div-FormHeader"><div class="row"><div class="col-sm">
  <span class="fw-bold">Иванов Иван Иванович (№ЗК - 0000000000)</span>
  <span class="fw-bold">ТЕСТ-01-25</span>
  <ul class="list-inline mb-0">
    <li class="list-inline-item">Курс: <strong>2</strong></li>
    $RATING_LIST
  </ul>
</div></div></div>
<div id="div-Student_SemesterSheet__Mark"></div>
</body></html>
"""

        const val PAGE_WITHOUT_RATING = """
<html><body>
<div id="div-FormHeader"><div class="row"><div class="col-sm">
  <span class="fw-bold">Иванов Иван Иванович (№ЗК - 0000000000)</span>
  <ul class="list-inline mb-0"><li class="list-inline-item">Курс: <strong>2</strong></li></ul>
</div></div></div>
<div id="div-Student_SemesterSheet__Mark"></div>
</body></html>
"""

        const val SUMMARY_PART = """
<select id="ddl_StudyFilterSemester" name="ddl_StudyFilterSemester">
  <option value="28">2026/2027, Осенний семестр</option>
  <option value="27" selected>2025/2026, Весенний семестр</option>
</select>
<div id="div-Student_SemesterSheet__MarkSummary">
<h5>2025/2026, Весенний семестр, успеваемость</h5>
<div class="table-responsive"><table id="tableMarkSummary" class="table table-bordered table-sm">
<thead>
  <tr><th rowspan="2">Дисциплина</th><th colspan="4">Неделя обучения</th><th rowspan="2">ТБ</th><th rowspan="2">ПА</th><th rowspan="2">И</th></tr>
  <tr><th class="summary-td-col-header">1</th><th class="summary-td-col-header">2</th><th class="summary-td-col-header summary-td-current-week">3</th><th class="summary-td-col-header">4</th></tr>
</thead>
<tbody>
  <tr class="summary-header-min"><td class="summary-header-max"></td><td colspan="7">Дискретная математика (экзамен)</td></tr>
  <tr class="summary-tr-even"><td class="summary-td-row-header summary-header-max">Дискретная математика (экзамен)</td>
    <td></td>
    <td><span class="summary-mark" title="Контрольная работа №1">4</span><br><span class="summary-mark" title="Контрольная работа №2">5</span></td>
    <td></td><td></td>
    <td>4,50</td><td>5</td><td>5</td></tr>
  <tr class="summary-header-min"><td class="summary-header-max"></td><td colspan="7">Физика (часть 1) (зачёт с оценкой)</td></tr>
  <tr><td class="summary-td-row-header summary-header-max">Физика (часть 1) (зачёт с оценкой)</td>
    <td>3</td><td></td><td></td><td></td>
    <td>3,00</td><td></td><td>3 (ППА1)</td></tr>
</tbody></table></div></div>
"""

        const val SUMMARY_EMPTY = """
<div id="div-Student_SemesterSheet__MarkSummary">
<h5>2026/2027, Осенний семестр, успеваемость</h5>
<table id="tableMarkSummary"><thead>
  <tr><th>Дисциплина</th><th colspan="2">Неделя обучения</th><th>ТБ</th><th>ПА</th><th>И</th></tr>
  <tr><th>1</th><th>2</th></tr>
</thead><tbody></tbody></table></div>
"""
    }
}
