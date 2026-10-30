package kekmech.ru.lib_bars_parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BarsTextTest {

    @Test
    fun `a mark with a date`() {
        val mark = assertNotNull(parseBarsMark("4  (06.06.26)"))

        assertEquals(4f, mark.value)
        assertEquals("06.06.26", mark.date)
        assertNull(mark.note)
        assertFalse(mark.isRetaken)
    }

    @Test
    fun `a decimal mark with a comma`() {
        assertEquals(2.5f, parseBarsMark("2,5")?.value)
        assertEquals(0f, parseBarsMark("0,00")?.value)
    }

    @Test
    fun `a mark with a retake`() {
        val mark = assertNotNull(parseBarsMark("4  (06.06.26)", "Пересдана: 0 (11.04.26)"))

        assertTrue(mark.isRetaken)
        assertEquals("0", mark.retakenFrom)
        assertEquals("11.04.26", mark.retakenDate)
        assertEquals("4 (06.06.26) Пересдана: 0 (11.04.26)", mark.raw)
    }

    @Test
    fun `a mark with a note`() {
        val mark = assertNotNull(parseBarsMark("2 ППА1 - 3 (не уч.)"))

        assertEquals(2f, mark.value)
        assertNull(mark.date)
        assertEquals("ППА1 - 3 (не уч.)", mark.note)
    }

    @Test
    fun `text without a number keeps its raw value`() {
        val mark = assertNotNull(parseBarsMark("зачтено (06.06.26)"))

        assertNull(mark.value)
        assertEquals("06.06.26", mark.date)
        assertEquals("зачтено", mark.note)
    }

    @Test
    fun `an empty cell is no mark`() {
        assertNull(parseBarsMark(""))
        assertNull(parseBarsMark("    "))
    }

    @Test
    fun `week number`() {
        assertEquals(15, parseBarsWeekNumber("15 (07.12.26-13.12.26)"))
        assertEquals(4, parseBarsWeekNumber("4"))
        assertNull(parseBarsWeekNumber(""))
        assertNull(parseBarsWeekNumber(null))
    }

    @Test
    fun `header of a discipline`() {
        val header = parseDisciplineHeader("Сети и системы передачи информации, Баронов О.Р., экзамен, 1ЗЕ")

        assertEquals("Сети и системы передачи информации", header.name)
        assertEquals("Баронов О.Р.", header.person)
        assertEquals("экзамен", header.assessmentType)
        assertEquals("1ЗЕ", header.units)
        assertNull(header.note)
    }

    @Test
    fun `header with a comma in the name and a supervisor in brackets`() {
        val header = parseDisciplineHeader(
            "Практика, учебная, Власенко М.Н. (руководитель - Власенко М.Н.), зачёт с оценкой, 2 ЗЕ частичная переаттестация"
        )

        assertEquals("Практика, учебная", header.name)
        assertEquals("Власенко М.Н. (руководитель - Власенко М.Н.)", header.person)
        assertEquals("зачёт с оценкой", header.assessmentType)
        assertEquals("2ЗЕ", header.units)
        assertEquals("частичная переаттестация", header.note)
    }

    @Test
    fun `header without units`() {
        val header = parseDisciplineHeader("Химия, Орлов О.О., экзамен")

        assertEquals("Химия", header.name)
        assertEquals("Орлов О.О.", header.person)
        assertEquals("экзамен", header.assessmentType)
        assertEquals("", header.units)
    }

    @Test
    fun `short headers do not fail`() {
        assertEquals("Химия", parseDisciplineHeader("Химия").name)
        assertEquals("экзамен", parseDisciplineHeader("Химия, экзамен").assessmentType)
        assertEquals("", parseDisciplineHeader("").name)
    }
}
