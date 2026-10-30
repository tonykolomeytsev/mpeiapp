package kekmech.ru.lib_bars_parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** The pages here are made up, they repeat the markup of bars.mpei.ru, not anybody's data. */
class BarsHtmlParserTest {

    @Test
    fun `detects the kind of the page`() {
        assertEquals(BarsPageKind.MARKS, BarsHtmlParser.detectPage(MARKS_PAGE))
        assertEquals(BarsPageKind.LOGIN, BarsHtmlParser.detectPage(LOGIN_PAGE))
        assertEquals(BarsPageKind.STUDENT_LIST, BarsHtmlParser.detectPage(STUDENT_LIST_PAGE))
        assertEquals(BarsPageKind.UNKNOWN, BarsHtmlParser.detectPage("<html><body><p>Hello</p></body></html>"))
    }

    @Test
    fun `refuses a page that is not the marks page`() {
        assertFailsWith<BarsParseException> { BarsHtmlParser.parseMarksPage(LOGIN_PAGE) }
    }

    @Test
    fun `reads the student header`() {
        val page = BarsHtmlParser.parseMarksPage(MARKS_PAGE)

        assertEquals("Иванов Иван Иванович", page.studentName)
        assertEquals("ТЕСТ-01-25", page.studentGroup)
        assertEquals(
            listOf(
                BarsMetaItem("Почта", "test@example.com"),
                BarsMetaItem("Курс", "2"),
                BarsMetaItem("Средний балл ПА", "3,12"),
            ),
            page.meta,
        )
    }

    @Test
    fun `reads the semesters and the selected one`() {
        val page = BarsHtmlParser.parseMarksPage(MARKS_PAGE)

        assertEquals(listOf("28", "27"), page.semesters.map { it.id })
        assertEquals("2025/2026, Весенний семестр", page.selectedSemester?.name)
        assertEquals("27", page.selectedSemester?.id)
    }

    @Test
    fun `reads the disciplines in the order of the page`() {
        val disciplines = BarsHtmlParser.parseMarksPage(MARKS_PAGE).disciplines

        assertEquals(
            listOf("Дискретная математика", "Физика (часть 1, лекции)", "Философия"),
            disciplines.map { it.name },
        )
    }

    @Test
    fun `reads the header of a discipline`() {
        val (math, physics) = BarsHtmlParser.parseMarksPage(MARKS_PAGE).disciplines

        assertEquals("Петров П.П.", math.person)
        assertEquals("экзамен", math.assessmentType)
        assertEquals("4ЗЕ", math.units)
        assertEquals("07.06.2026", math.deadline)
        assertTrue(math.notes.isEmpty())

        assertEquals("Сидоров С.С. (руководитель - Сидоров С.С.)", physics.person)
        assertEquals("зачёт с оценкой (по совокупности)", physics.assessmentType)
        assertEquals("3ЗЕ", physics.units)
        assertEquals(listOf("частичная переаттестация"), physics.notes)
    }

    @Test
    fun `reads control events with the marks, the retakes and the criteria`() {
        val math = BarsHtmlParser.parseMarksPage(MARKS_PAGE).disciplines.first()
        val events = math.activities.filter { it.type == BarsActivityType.CONTROL_ACTIVITY }

        assertEquals(2, events.size)

        val first = events[0]
        assertEquals("1. Контрольная работа №1", first.name)
        assertEquals("50", first.weight)
        assertEquals("14 (18.05.26-24.05.26)", first.weekNum)
        assertEquals(14, parseBarsWeekNumber(first.weekNum))
        assertEquals(4f, first.mark?.value)
        assertEquals("06.06.26", first.mark?.date)
        assertEquals("0", first.mark?.retakenFrom)
        assertEquals("11.04.26", first.mark?.retakenDate)
        assertEquals("5: (полностью) 4: (почти) 3: () 2: ()", first.criteria)

        val second = events[1]
        assertEquals("2. Контрольная работа №2", second.name)
        assertNull(second.mark)
        assertNull(second.criteria)
    }

    @Test
    fun `criteria of the same id in another table are not mixed up`() {
        val philosophy = BarsHtmlParser.parseMarksPage(MARKS_PAGE).disciplines[2]
        val event = philosophy.activities.single { it.type == BarsActivityType.CONTROL_ACTIVITY }

        assertEquals("Критерии философии", event.criteria)
        assertEquals(3f, event.mark?.value)
    }

    @Test
    fun `reads the summary rows`() {
        val math = BarsHtmlParser.parseMarksPage(MARKS_PAGE).disciplines.first()
        val byType = math.activities.filter { it.type != BarsActivityType.CONTROL_ACTIVITY }

        assertEquals(
            listOf(
                BarsActivityType.CURRENT_SCORE,
                BarsActivityType.CONTROL_WEEK,
                BarsActivityType.CONTROL_WEEK,
                BarsActivityType.FINAL_MARK,
            ),
            byType.map { it.type },
        )
        assertEquals(2f, byType[0].mark?.value)

        assertEquals("Контрольная неделя №1", byType[1].name)
        assertEquals("4", byType[1].weekNum)
        assertNull(byType[1].mark)
        assertEquals(0f, byType[2].mark?.value)

        val finalMark = byType[3].mark
        assertNotNull(finalMark)
        assertEquals(2f, finalMark.value)
        assertEquals("ППА1 - 3 (не уч.)", finalMark.note)
    }

    @Test
    fun `skips the header row of the table and the section titles`() {
        val math = BarsHtmlParser.parseMarksPage(MARKS_PAGE).disciplines.first()

        assertTrue(math.activities.none { it.name.startsWith("Текущий контроль") })
        assertTrue(math.activities.none { it.name.startsWith("Наименование") })
    }

    @Test
    fun `a discipline without a table is skipped, not a failure`() {
        val page = BarsHtmlParser.parseMarksPage(MARKS_PAGE_ORPHAN_CARD)

        assertEquals(listOf("Химия"), page.disciplines.map { it.name })
    }

    @Test
    fun `pairs cards and tables by position when the card has no target`() {
        val disciplines = BarsHtmlParser.parseMarksPage(MARKS_PAGE_OLD_LAYOUT).disciplines

        assertEquals(1, disciplines.size)
        assertEquals("Химия", disciplines.single().name)
        assertEquals("Орлов О.О.", disciplines.single().person)
        assertEquals(1, disciplines.single().activities.size)
    }

    @Test
    fun `reads the list of students`() {
        val students = BarsHtmlParser.parseStudentList(STUDENT_LIST_PAGE)

        assertEquals(
            listOf(
                BarsStudentLink("11111111-2222-3333-4444-555555555555", "Иванов Иван Иванович"),
                BarsStudentLink("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee", "Иванова Мария Ивановна"),
            ),
            students,
        )
    }

    private companion object {

        const val MARKS_PAGE = """
<html><body>
<div id="div-FormHeader"><div class="row"><div class="col m-0 p-0 px-2"><div class="row"><div class="col-sm">
  <span class="fw-bold">Иванов Иван Иванович (№ЗК - 0000000000)</span>
  <span class="fw-bold">ТЕСТ-01-25</span>
  <a href="/x" target="_blank"><em>Расписание</em></a>
  <ul class="list-inline mb-0">
    <li class="list-inline-item">Почта: <strong>test@example.com</strong></li>
    <li class="list-inline-item">Курс: <strong>2</strong></li>
    <li class="list-inline-item">Средний балл ПА: <strong>3,12</strong></li>
    <li class="list-inline-item"></li>
  </ul>
</div></div></div></div></div>
<select id="ddl_StudyFilterSemester" name="ddl_StudyFilterSemester">
  <option value="28">2026/2027, Осенний семестр</option>
  <option value="27" selected>2025/2026, Весенний семестр</option>
</select>
<div id="div-Student_SemesterSheet__Mark">
  <div class="my-2">
    <div data-bs-toggle="collapse" data-bs-target="#s_ss_0" aria-expanded="false">Дискретная математика, Петров П.П., экзамен, 4ЗЕ <span class="badge bg-light" title="в 23.59">сдать до 07.06.2026</span></div>
    <div><a href="#" role="button">Развернуть</a></div>
    <div><a href="#" target="_blank"><span class="badge bg-primary">оцени курс!</span></a></div>
  </div>
  <div id="s_ss_0" class="collapse"><div class="table-responsive"><table class="table table-sm"><thead>
    <tr><th>Наименование контрольного мероприятия</th><th>Вес, %</th><th>Срок проведения, № недели (дата)</th><th>Оценка, дата получения</th></tr></thead><tbody>
    <tr><td colspan="4"><em>Текущий контроль:</em></td></tr>
    <tr class="tr-odd"><td>1. Контрольная работа №1<div data-bs-toggle="collapse" data-bs-target="#divMERes0"><a class="btn btn-link py-0">(критерии)</a></div></td><td>50</td><td>14 (18.05.26-24.05.26)</td><td>4  (06.06.26)<br><span class="km-retaken">Пересдана: 0 (11.04.26)</span></td></tr>
    <tr id="divMERes0" class="collapse tr-odd"><td colspan="4"><div class="tr-me-res">5: (полностью) 4: (почти) 3: () 2: ()</div></td></tr>
    <tr><td>2. Контрольная работа №2</td><td>50</td><td>15 (25.05.26-31.05.26)</td><td></td></tr>
    <tr class="tr-odd"><td colspan="3">Балл текущего контроля:</td><td>2,00</td></tr>
    <tr><td colspan="3">Контрольная неделя №1</td><td>4</td><td></td></tr>
    <tr class="tr-odd"><td colspan="3">Контрольная неделя №2</td><td>8</td><td>0,00</td></tr>
    <tr><td colspan="3">Итоговая оценка:</td><td>2 ППА1 - 3 (не уч.)</td></tr>
  </tbody></table></div></div>

  <div class="my-2">
    <div data-bs-toggle="collapse" data-bs-target="#s_ss_1" aria-expanded="false">Физика (часть 1, лекции), Сидоров С.С. (руководитель - Сидоров С.С.), зачёт с оценкой (по совокупности), 3ЗЕ <span class="badge bg-light">сдать до 07.06.2026</span> частичная переаттестация</div>
    <div><a href="#" role="button">Развернуть</a></div>
  </div>
  <div id="s_ss_1" class="collapse"><div class="table-responsive"><table class="table"><tbody>
    <tr><td>1. Тест</td><td>100</td><td>3 (02.03.26-08.03.26)</td><td>5  (09.03.26)</td></tr>
  </tbody></table></div></div>

  <div class="my-2">
    <div data-bs-toggle="collapse" data-bs-target="#s_ss_2" aria-expanded="false">Философия, Кузнецов К.К., зачёт (без оценки), 2ЗЕ <span class="badge bg-light">сдать до 07.06.2026</span></div>
  </div>
  <div id="s_ss_2" class="collapse"><div class="table-responsive"><table class="table"><tbody>
    <tr class="tr-odd"><td>1. КМ-1<div data-bs-toggle="collapse" data-bs-target="#divMERes0"><a class="btn btn-link py-0">(критерии)</a></div></td><td>100</td><td>12 (04.05.26-10.05.26)</td><td>3  (06.06.26)</td></tr>
    <tr id="divMERes0" class="collapse tr-odd"><td colspan="4"><div class="tr-me-res">Критерии философии</div></td></tr>
  </tbody></table></div></div>
</div>
</body></html>
"""

        const val MARKS_PAGE_ORPHAN_CARD = """
<html><body><div id="div-Student_SemesterSheet__Mark">
  <div class="my-2"><div data-bs-toggle="collapse" data-bs-target="#s_ss_0">Без таблицы, Автор А.А., экзамен, 1ЗЕ</div></div>
  <div class="my-2"><div data-bs-toggle="collapse" data-bs-target="#s_ss_1">Химия, Орлов О.О., экзамен, 2ЗЕ</div></div>
  <div id="s_ss_1" class="collapse"><table><tbody><tr><td>1. КМ</td><td>100</td><td>5</td><td>4  (01.01.26)</td></tr></tbody></table></div>
</div></body></html>
"""

        const val MARKS_PAGE_OLD_LAYOUT = """
<html><body><div id="div-Student_SemesterSheet__Mark">
  <div class="my-2"><div data-bs-toggle="collapse">Химия, Орлов О.О., экзамен, 2ЗЕ</div></div>
  <div class="collapse"><table><tbody><tr><td>1. КМ</td><td>100</td><td>5</td><td>4  (01.01.26)</td></tr></tbody></table></div>
</div></body></html>
"""

        const val LOGIN_PAGE = """
<html><body><form method="post" action="/BARS_Web/Auth/Login">
  <input type="text" name="login"><input type="password" name="password"><button type="submit">Войти</button>
</form></body></html>
"""

        const val STUDENT_LIST_PAGE = """
<html><body><table><tbody>
  <tr><td><a href="/bars_web/ST_Study/Main/Main?studentID=11111111-2222-3333-4444-555555555555">Иванов Иван Иванович</a></td></tr>
  <tr><td><a href="/bars_web/ST_Study/Main/Main?studentID=aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee">Иванова Мария Ивановна</a></td></tr>
  <tr><td><a href="/bars_web/ST_Study/Main/Main?studentID=11111111-2222-3333-4444-555555555555">Иванов Иван Иванович</a></td></tr>
</tbody></table></body></html>
"""
    }
}
