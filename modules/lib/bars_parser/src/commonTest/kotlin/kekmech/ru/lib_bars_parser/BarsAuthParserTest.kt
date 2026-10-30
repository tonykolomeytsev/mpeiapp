package kekmech.ru.lib_bars_parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** The pages here are made up, they repeat the markup of bars.mpei.ru, not anybody's data. */
class BarsAuthParserTest {

    @Test
    fun `reads the sign-in form`() {
        val form = BarsAuthParser.parseLoginForm(LOGIN_PAGE)

        assertEquals("/bars_web/", form.action)
        assertEquals("post", form.method)
        assertEquals("Account", form.accountField)
        assertEquals("Password", form.passwordField)
        assertEquals("RememberMe", form.rememberMeField)
        assertEquals("test-token", form.antiForgeryToken)
        assertEquals("False", form.hiddenFields["StopOpenDefault"])
        assertEquals(setOf("__RequestVerificationToken", "StopOpenDefault", "RememberMe"), form.hiddenFields.keys)
    }

    @Test
    fun `a page without the form is not the sign-in page`() {
        assertFailsWith<BarsParseException> { BarsAuthParser.parseLoginForm("<html><body>Hello</body></html>") }
        assertFailsWith<BarsParseException> {
            BarsAuthParser.parseLoginForm("""<html><body><input type="password" name="p"></body></html>""")
        }
    }

    @Test
    fun `detects the sign-in page and the second factor page`() {
        assertEquals(BarsPageKind.LOGIN, BarsHtmlParser.detectPage(LOGIN_PAGE))
        assertEquals(BarsPageKind.TWO_FACTOR, BarsHtmlParser.detectPage(TWO_FACTOR_TOTP_PAGE))
    }

    @Test
    fun `the second factor page that starts with a one-time code`() {
        val page = BarsAuthParser.parseTwoFactorPage(TWO_FACTOR_TOTP_PAGE)

        assertEquals("/bars_web/Auth/JSON_SendAF2_Code", page.sendCodeUrl)
        assertEquals(BarsTwoFactorKind.TOTP, page.default.kind)
        assertEquals("5", page.default.tid)
        assertEquals(6, page.default.codeLength)
        assertEquals("btnSend", page.default.buttonId)
        assertEquals("Войти по одноразовому коду", page.default.caption)
        assertNull(page.error)

        assertEquals(listOf(BarsTwoFactorKind.TOTP, BarsTwoFactorKind.MAX), page.providers.map { it.kind })
        val max = page.providers.last()
        assertEquals("btnSend3", max.buttonId)
        assertEquals("3", max.tid)
        assertEquals(4, max.codeLength)
    }

    @Test
    fun `the second factor page with messengers only`() {
        val page = BarsAuthParser.parseTwoFactorPage(TWO_FACTOR_MESSENGERS_PAGE)

        assertEquals(
            listOf(BarsTwoFactorKind.MAX, BarsTwoFactorKind.VK, BarsTwoFactorKind.TELEGRAM),
            page.providers.map { it.kind },
        )
        assertEquals(BarsTwoFactorKind.MAX, page.default.kind)
        assertEquals("3", page.default.tid)
    }

    @Test
    fun `provider 5 with a short code is an e-mail`() {
        val page = BarsAuthParser.parseTwoFactorPage(
            """<a id="b" onclick="af2_code_send('b', '/bars_web/Auth/JSON_SendAF2_Code', '5', '4');" data-text="Код">x</a>""",
        )

        assertEquals(BarsTwoFactorKind.EMAIL, page.default.kind)
        assertEquals("/bars_web/Auth/JSON_SendAF2_Code", page.sendCodeUrl)
    }

    @Test
    fun `a page without af2_code_send is not the second factor page`() {
        assertFailsWith<BarsParseException> { BarsAuthParser.parseTwoFactorPage(LOGIN_PAGE) }
    }

    @Test
    fun `reads the error that a page shows`() {
        assertEquals("Пользователь не найден", BarsAuthParser.parseFormError(LOGIN_PAGE_WITH_ERROR))
        assertEquals(
            "Некорректный код подтверждения",
            BarsAuthParser.parseTwoFactorPage(TWO_FACTOR_PAGE_WITH_ERROR).error,
        )
        assertNull(BarsAuthParser.parseFormError(LOGIN_PAGE))
    }

    @Test
    fun `reads the answer to a request for a code`() {
        val ok = BarsAuthParser.parseSendCodeResult("""{"success":true,"message":"Сообщение отправлено в 'MAX'"}""")
        assertEquals(true, ok.success)
        assertEquals("Сообщение отправлено в 'MAX'", ok.message)

        val failed = BarsAuthParser.parseSendCodeResult("""{"success":false,"message":"Слишком часто"}""")
        assertFalse(failed.success == true)
        assertEquals("Слишком часто", failed.message)

        val garbage = BarsAuthParser.parseSendCodeResult("<html></html>")
        assertNull(garbage.success)
        assertNull(garbage.message)
    }

    @Test
    fun `a page after the sign-in is the marks page, not the form`() {
        assertTrue(BarsHtmlParser.detectPage("""<div id="div-Student_SemesterSheet__Mark"></div>""") == BarsPageKind.MARKS)
    }

    private companion object {

        const val LOGIN_PAGE = """
<html><head><title>БАРС МЭИ</title></head><body>
<div class="form-signin"><span class="h2">БАРС МЭИ</span>
<form action="/bars_web/" class="mt-3" method="post">
  <input name="__RequestVerificationToken" type="hidden" value="test-token">
  <input data-val="true" id="StopOpenDefault" name="StopOpenDefault" type="hidden" value="False">
  <div class="offset-1 col-10"><div class="input-group"><span class="input-group-text"><i class="fa fa-user"></i></span>
    <input autocomplete="true" class="form-control" id="Account" name="Account" placeholder="Введите логин" type="text" value=""></div>
    <span class="field-validation-valid" data-valmsg-for="Account"></span></div>
  <div class="offset-1 col-10 mt-2"><div class="input-group"><span class="input-group-text"><i class="fa fa-key"></i></span>
    <input autocomplete="false" class="form-control" id="Password" name="Password" placeholder="Введите пароль" type="password">
    <button type="button" class="btn btn-light icc-pwd-toggle"></button></div>
    <span class="field-validation-valid" data-valmsg-for="Password"></span></div>
  <div class="offset-1 col-10 mt-2"><button id="btnLogin" type="button" class="btn btn-primary w-100">Войти</button></div>
  <div><input id="RememberMe" name="RememberMe" type="checkbox" value="true"><input name="RememberMe" type="hidden" value="false"> Запомнить меня</div>
  <a href="/help">2-х факторная аутентификация (обновлено)</a>
</form></div>
</body></html>
"""

        const val LOGIN_PAGE_WITH_ERROR = """
<html><body><div class="alert alert-danger">Пользователь не найден</div>
<form action="/bars_web/" method="post">
  <input name="Account" type="text"><input name="Password" type="password">
</form></body></html>
"""

        const val TWO_FACTOR_TOTP_PAGE = """
<html><body>
<script>
    af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6');
</script>
<a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6');" data-text="Войти по одноразовому коду">Код</a>
<a id="btnSend3" href="#" onclick="af2_code_send('btnSend3', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" data-text="Отправить повторно через МАКС">МАКС</a>
</body></html>
"""

        const val TWO_FACTOR_MESSENGERS_PAGE = """
<html><body>
<script>af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');</script>
<a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" data-text="MAX">MAX</a>
<a id="btnSend2" href="#" onclick="af2_code_send('btnSend2', '/bars_web/Auth/JSON_SendAF2_Code', '2', '4');" data-text="VK">VK</a>
<a id="btnSend1" href="#" onclick="af2_code_send('btnSend1', '/bars_web/Auth/JSON_SendAF2_Code', '1', '4');" data-text="Telegram">Telegram</a>
</body></html>
"""

        const val TWO_FACTOR_PAGE_WITH_ERROR = """
<html><body>
<span class="field-validation-error">Некорректный код подтверждения</span>
<script>af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6');</script>
</body></html>
"""
    }
}
