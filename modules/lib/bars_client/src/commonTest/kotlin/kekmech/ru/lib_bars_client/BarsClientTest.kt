package kekmech.ru.lib_bars_client

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.Headers
import io.ktor.http.Parameters
import io.ktor.client.request.HttpResponseData
import kekmech.ru.lib_bars_parser.BarsTwoFactorKind
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** A fake BARS: the pages are made up, they have the markup of bars.mpei.ru and nothing else of it. */
class BarsClientTest {

    private val log = mutableListOf<HttpRequestData>()
    private val client = BarsClient(engine = fakeBars(log))

    @Test
    fun `login asks for a code and requests it the way the page does`() = runTest {
        val result = assertIs<BarsLoginResult.TwoFactorRequired>(client.login("user", "pass"))

        assertEquals(BarsTwoFactorKind.TOTP, result.page.default.kind)
        assertEquals(true, result.codeRequest?.success)
        assertEquals("sent", result.codeRequest?.message)

        assertEquals(3, log.size)
        assertEquals(HttpMethod.Get, log[0].method)
        assertEquals("/BARS_Web", log[0].url.encodedPath)

        val posted = log[1].form()
        assertEquals(HttpMethod.Post, log[1].method)
        assertEquals("/bars_web/", log[1].url.encodedPath)
        assertEquals("user", posted["Account"])
        assertEquals("pass", posted["Password"])
        assertEquals("false", posted["RememberMe"])
        assertEquals("tok1", posted["__RequestVerificationToken"])

        assertEquals("5", log[2].url.parameters["tid"])
        assertEquals("XMLHttpRequest", log[2].headers["X-Requested-With"])
    }

    @Test
    fun `login can skip the request for a code`() = runTest {
        val result = assertIs<BarsLoginResult.TwoFactorRequired>(client.login("user", "pass", sendCode = false))

        assertNull(result.codeRequest)
        assertEquals(2, log.size)
    }

    @Test
    fun `a wrong password is rejected and nothing waits for a code`() = runTest {
        val result = assertIs<BarsLoginResult.Rejected>(client.login("user", "bad"))

        assertEquals("Пользователь не найден", result.message)
        assertEquals(false, result.canRetryCode)
        assertFailsWith<IllegalStateException> { client.submitCode("123456") }
    }

    @Test
    fun `the right code opens the session and the cookies go along`() = runTest {
        client.login("user", "pass")
        val result = assertIs<BarsLoginResult.LoggedIn>(client.submitCode("123456"))

        assertEquals(STUDENT, result.studentId)
        assertEquals(STUDENT, client.studentId)

        val codePost = log.single { it.url.encodedPath == "/bars_web/Auth/LoginCode" }
        val posted = codePost.form()
        assertEquals("123456", posted["AF2_Code"])
        assertEquals("user", posted["Account"])
        assertEquals("tok2", posted["__RequestVerificationToken"])

        // the answer to the post is a redirect: it is followed with a GET and the session cookie goes with it
        val landing = log.last()
        assertEquals(HttpMethod.Get, landing.method)
        assertEquals("/bars_web/ST_Study/Main/Main", landing.url.encodedPath)
        val cookie = landing.headers[HttpHeaders.Cookie].orEmpty()
        assertTrue("sess=1" in cookie, cookie)
        assertTrue("auth=1" in cookie, cookie)
    }

    @Test
    fun `a wrong code can be tried again with the fresh form`() = runTest {
        client.login("user", "pass")

        val rejected = assertIs<BarsLoginResult.Rejected>(client.submitCode("000000"))
        assertEquals("Некорректный код подтверждения", rejected.message)
        assertEquals(true, rejected.canRetryCode)

        assertIs<BarsLoginResult.LoggedIn>(client.submitCode("123456"))
        val tokens = log.filter { it.url.encodedPath == "/bars_web/Auth/LoginCode" }.map { it.form()["__RequestVerificationToken"] }
        assertEquals(listOf("tok2", "tok3"), tokens)
    }

    @Test
    fun `a code can be requested again with another provider`() = runTest {
        val first = assertIs<BarsLoginResult.TwoFactorRequired>(client.login("user", "pass", sendCode = false))
        val max = first.page.providers.single { it.kind == BarsTwoFactorKind.MAX }

        val result = client.sendCode(max)

        assertEquals(true, result.success)
        assertEquals("3", log.last().url.parameters["tid"])
    }

    @Test
    fun `loads the marks page of the student`() = runTest {
        signIn()

        val page = client.loadMarksPage()

        assertEquals(listOf("Химия"), page.disciplines.map { it.name })
        assertEquals(STUDENT, log.last().url.parameters["studentID"])
    }

    @Test
    fun `the marks page without a session says that the session is gone`() = runTest {
        assertFailsWith<BarsSessionExpiredException> { client.loadMarksPage() }
    }

    @Test
    fun `loads the summary of a semester with the request of the page`() = runTest {
        signIn()

        val summary = client.loadSummary(semesterId = "27")

        assertEquals("2025/2026, Весенний семестр", summary.semesterName)
        assertEquals(listOf("Химия"), summary.rows.map { it.discipline })
        val request = log.last()
        assertEquals("/bars_web/ST_Study/Main/_PartialSummary", request.url.encodedPath)
        assertEquals(STUDENT, request.url.parameters["studentID"])
        assertEquals("""{"ID":"$STUDENT","FilterSemester":{"Value":"27"}}""", request.url.parameters["query"])
    }

    @Test
    fun `an id that is not an id never gets into a request`() = runTest {
        signIn()
        val before = log.size

        assertFailsWith<IllegalArgumentException> { client.loadSummary(semesterId = "27\"}") }
        assertFailsWith<IllegalArgumentException> { client.loadMarksPage(studentId = "x&y=z") }
        assertEquals(before, log.size)
    }

    @Test
    fun `logout forgets the session`() = runTest {
        signIn()

        client.logout()

        assertEquals("/BARS_Web/Auth/Exit", log.last().url.encodedPath)
        assertNull(client.studentId)
        assertFailsWith<BarsSessionExpiredException> { client.loadMarksPage() }
        assertNull(log.last().headers[HttpHeaders.Cookie])
    }

    @Test
    fun `an error status of the server is an exception`() = runTest {
        val broken = BarsClient(engine = MockEngine { respond("oops", HttpStatusCode.InternalServerError) })

        assertFailsWith<BarsNetworkException> { broken.login("user", "pass") }
    }

    private suspend fun signIn() {
        client.login("user", "pass", sendCode = false)
        assertIs<BarsLoginResult.LoggedIn>(client.submitCode("123456"))
    }

    private fun HttpRequestData.form(): Parameters = (body as FormDataContent).formData

    private companion object {
        const val STUDENT = "11111111-2222-3333-4444-555555555555"

        fun fakeBars(log: MutableList<HttpRequestData>) = MockEngine { request ->
            log += request
            // IIS does not care about the case of a path, and neither does the fake
            val path = request.url.encodedPath.lowercase()
            val signedIn = request.headers[HttpHeaders.Cookie].orEmpty().contains("auth=1")
            when {
                request.method == HttpMethod.Get && path == "/bars_web" ->
                    html(LOGIN_PAGE, cookie = "sess=1; path=/; HttpOnly")

                request.method == HttpMethod.Post && path == "/bars_web/" -> {
                    val form = (request.body as FormDataContent).formData
                    if (form["Account"] == "user" && form["Password"] == "pass") {
                        html(twoFactorPage(token = "tok2", error = null))
                    } else {
                        html(LOGIN_PAGE_WITH_ERROR)
                    }
                }

                path == "/bars_web/auth/json_sendaf2_code" ->
                    html("""{"success":true,"message":"sent"}""")

                request.method == HttpMethod.Post && path == "/bars_web/auth/logincode" -> {
                    val form = (request.body as FormDataContent).formData
                    if (form["AF2_Code"] == "123456") {
                        respond(
                            "",
                            HttpStatusCode.Found,
                            Headers.build {
                                append(HttpHeaders.Location, "/bars_web/ST_Study/Main/Main?studentID=$STUDENT")
                                append(HttpHeaders.SetCookie, "auth=1; path=/")
                            },
                        )
                    } else {
                        html(twoFactorPage(token = "tok3", error = "Некорректный код подтверждения"))
                    }
                }

                path == "/bars_web/st_study/main/main" -> html(if (signedIn) MARKS_PAGE else LOGIN_PAGE)

                path == "/bars_web/st_study/main/_partialsummary" || path == "/bars_web/st_study/main/summary" ->
                    html(if (signedIn) SUMMARY_PART else LOGIN_PAGE)

                path == "/bars_web/auth/exit" -> html(LOGIN_PAGE)

                else -> respond("", HttpStatusCode.NotFound)
            }
        }

        fun MockRequestHandleScope.html(body: String, cookie: String? = null): HttpResponseData = respond(
            body,
            HttpStatusCode.OK,
            Headers.build {
                append(HttpHeaders.ContentType, "text/html; charset=utf-8")
                if (cookie != null) append(HttpHeaders.SetCookie, cookie)
            },
        )

        const val LOGIN_PAGE = """
<html><body><form action="/bars_web/" method="post">
  <input name="__RequestVerificationToken" type="hidden" value="tok1">
  <input id="Account" name="Account" type="text">
  <input id="Password" name="Password" type="password">
  <input id="RememberMe" name="RememberMe" type="checkbox" value="true"><input name="RememberMe" type="hidden" value="false">
</form></body></html>
"""

        const val LOGIN_PAGE_WITH_ERROR = """
<html><body><div class="alert alert-danger">Пользователь не найден</div>
<form action="/bars_web/" method="post">
  <input name="__RequestVerificationToken" type="hidden" value="tok1">
  <input name="Account" type="text"><input name="Password" type="password">
</form></body></html>
"""

        fun twoFactorPage(token: String, error: String?) = """
<html><body>
<form action="/bars_web/Auth/LoginCode" method="post">
  <input name="__RequestVerificationToken" type="hidden" value="$token">
  <input id="Account" name="Account" type="hidden" value="user">
  <input id="RememberMe" name="RememberMe" type="hidden" value="False">
  ${if (error != null) """<span class="field-validation-error">$error</span>""" else ""}
  <input id="AF2_Code" name="AF2_Code" type="text" maxlength="6">
  <a id="btnSend" href="#" onclick="af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6');" data-text="Войти по одноразовому коду">Код</a>
  <a id="btnSend3" href="#" onclick="af2_code_send('btnSend3', '/bars_web/Auth/JSON_SendAF2_Code', '3', '4');" data-text="Отправить повторно через МАКС">МАКС</a>
</form>
<script>
  function af2_code_send(btnId, url, tid, af2_len) { }
  jQuery(document).ready(function() { af2_code_send('btnSend', '/bars_web/Auth/JSON_SendAF2_Code', '5', '6'); });
</script>
</body></html>
"""

        const val MARKS_PAGE = """
<html><body>
<a href="/bars_web/ST_Study/Main/Main?studentID=11111111-2222-3333-4444-555555555555">Оценки</a>
<div id="div-FormHeader"><div class="row"><div class="col-sm"><span class="fw-bold">Иванов Иван Иванович (№ЗК - 0)</span></div></div></div>
<div id="div-Student_SemesterSheet__Mark">
  <div class="my-2"><div data-bs-toggle="collapse" data-bs-target="#s_ss_0">Химия, Орлов О.О., экзамен, 2ЗЕ</div></div>
  <div id="s_ss_0" class="collapse"><table><tbody>
    <tr><td>1. КМ</td><td>100</td><td>5</td><td>4  (01.01.26)</td></tr>
  </tbody></table></div>
</div>
</body></html>
"""

        const val SUMMARY_PART = """
<div id="div-Student_SemesterSheet__MarkSummary">
<h5>2025/2026, Весенний семестр, успеваемость</h5>
<table id="tableMarkSummary"><thead>
  <tr><th>Дисциплина</th><th colspan="2">Неделя обучения</th><th>ТБ</th><th>ПА</th><th>И</th></tr>
  <tr><th>1</th><th>2</th></tr>
</thead><tbody>
  <tr><td class="summary-td-row-header">Химия (экзамен)</td><td></td><td><span class="summary-mark" title="КМ">4</span></td><td>4,00</td><td></td><td></td></tr>
</tbody></table></div>
"""
    }
}
