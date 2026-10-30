package kekmech.ru.lib_bars_client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.accept
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import kekmech.ru.lib_bars_parser.BarsAuthParser
import kekmech.ru.lib_bars_parser.BarsHtmlParser
import kekmech.ru.lib_bars_parser.BarsMarksPage
import kekmech.ru.lib_bars_parser.BarsMarksPart
import kekmech.ru.lib_bars_parser.BarsPageKind
import kekmech.ru.lib_bars_parser.BarsRating
import kekmech.ru.lib_bars_parser.BarsSendCodeResult
import kekmech.ru.lib_bars_parser.BarsSummaryPage
import kekmech.ru.lib_bars_parser.BarsTwoFactorPage
import kekmech.ru.lib_bars_parser.BarsTwoFactorProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val MAX_REDIRECTS = 8

private fun HttpClientConfig<*>.configureBars() {
    // The redirects are followed by hand in BarsClient.send(): Ktor does not follow the one that answers a POST
    followRedirects = false
    expectSuccess = false
    install(HttpCookies)
    install(UserAgent) { agent = "MPEI-Hub" }
}
private val STUDENT_ID_IN_URL = Regex("studentID=([0-9a-fA-F-]{36})", RegexOption.IGNORE_CASE)

/**
 * The client of the BARS student portal: it signs in (the login and the password, then the code of the second
 * factor), keeps the cookies of the session and downloads the pages that [BarsHtmlParser] parses.
 *
 * It does what the page does in a browser and nothing else: the forms are posted with the fields that the page
 * itself gives, the redirects are followed by hand (a POST that is answered with a redirect is read with a GET),
 * the code is requested the way the page's script requests it. The password is used for one request and is not kept.
 *
 * The session lives as long as the client object: the cookies are kept in memory only.
 * The calls are serialized, one at a time.
 *
 * @param engine the engine of the HTTP client; null takes the one of the platform (Darwin on iOS, CIO on JVM)
 */
class BarsClient(
    private val endpoints: BarsEndpoints = BarsEndpoints(),
    private val engine: HttpClientEngine? = null,
) {

    private val mutex = Mutex()
    private var http: HttpClient = createHttp()
    private var pending: BarsTwoFactorPage? = null

    /** The student whose pages are asked for, known after the sign-in or the first page. */
    var studentId: String? = null
        private set

    /**
     * Starts a sign-in. A new session is started: whatever session the client had is dropped.
     *
     * @param sendCode makes the request for a code that the page makes when it opens; set false if the code comes from
     * an authenticator app and nothing has to be sent
     */
    suspend fun login(
        login: String,
        password: String,
        rememberMe: Boolean = false,
        sendCode: Boolean = true,
    ): BarsLoginResult = mutex.withLock {
        resetSession()

        val loginPage = send(HttpMethod.Get, endpoints.login)
        if (BarsHtmlParser.detectPage(loginPage.body) != BarsPageKind.LOGIN) {
            throw BarsNetworkException("The sign-in page is not the sign-in page (HTTP ${loginPage.status})")
        }
        val form = BarsAuthParser.parseLoginForm(loginPage.body)

        val fields = LinkedHashMap(form.hiddenFields)
        fields[form.accountField] = login
        fields[form.passwordField] = password
        form.rememberMeField?.let { fields[it] = rememberMe.toString() }

        val response = send(HttpMethod.Post, resolve(loginPage.url, form.action), form = fields)
        handleAuthResponse(response, sendCode = sendCode, afterCode = false)
    }

    /**
     * Asks BARS to send a code with [provider] (the default one of the page if null). The page does the same with its
     * "send again" buttons.
     */
    suspend fun sendCode(provider: BarsTwoFactorProvider? = null): BarsSendCodeResult = mutex.withLock {
        val page = pending ?: error("There is no sign-in waiting for a code, call login() first")
        requestCode(page, provider ?: page.default)
    }

    /** Posts the code of the second factor. */
    suspend fun submitCode(code: String): BarsLoginResult = mutex.withLock {
        val page = pending ?: error("There is no sign-in waiting for a code, call login() first")
        val form = page.form ?: throw BarsNetworkException("The second factor page has no form to post the code with")
        require(code.isNotBlank()) { "The code is empty" }

        val fields = LinkedHashMap(form.hiddenFields)
        fields[form.codeField] = code.trim()
        val response = send(HttpMethod.Post, resolve(endpoints.login, form.action), form = fields)
        handleAuthResponse(response, sendCode = false, afterCode = true)
    }

    /**
     * The marks page of the student ([studentId] or the one of the sign-in).
     * @throws BarsSessionExpiredException if BARS wants the sign-in again
     */
    suspend fun loadMarksPage(studentId: String? = null): BarsMarksPage = mutex.withLock {
        val id = studentId ?: this.studentId
        val response = send(
            HttpMethod.Get,
            endpoints.marks,
            query = if (id != null) listOf("studentID" to checkedId(id)) else emptyList(),
        )
        when (BarsHtmlParser.detectPage(response.body)) {
            BarsPageKind.MARKS -> {
                rememberStudent(response)
                BarsHtmlParser.parseMarksPage(response.body)
            }
            else -> throw unexpectedPage(response)
        }
    }

    /**
     * The summary of the student: the page itself for the semester that BARS opens, or its table for [semesterId]
     * (a value of the semester filter, see `BarsSummaryPage.semesters`).
     */
    suspend fun loadSummary(semesterId: String? = null, studentId: String? = null): BarsSummaryPage = mutex.withLock {
        val response = if (semesterId == null) {
            send(HttpMethod.Get, endpoints.summary, query = listOf("studentID" to knownStudent(studentId)))
        } else {
            sendSemesterPart(endpoints.summaryPart, semesterId, studentId)
        }
        when (BarsHtmlParser.detectPage(response.body)) {
            BarsPageKind.SUMMARY -> BarsHtmlParser.parseSummaryPage(response.body)
            else -> throw unexpectedPage(response)
        }
    }

    /**
     * The marks of another semester ([semesterId] is a value of the semester filter, see `BarsMarksPage.semesters`):
     * the same request that the marks page makes when the semester is changed.
     * @throws BarsSessionExpiredException if BARS wants the sign-in again
     */
    suspend fun loadSemesterMarks(semesterId: String, studentId: String? = null): BarsMarksPart = mutex.withLock {
        BarsHtmlParser.parseMarksPart(sendSemesterPart(endpoints.marksPart, semesterId, studentId).body)
    }

    /**
     * The rating of a semester: what the marks page asks for when the semester of the rating is changed. Null if BARS
     * answers without a rating.
     */
    suspend fun loadRating(semesterId: String, studentId: String? = null): BarsRating? = mutex.withLock {
        BarsHtmlParser.parseRating(sendSemesterPart(endpoints.headerPart, semesterId, studentId).body)
    }

    private fun knownStudent(studentId: String?): String = checkedId(
        studentId ?: this.studentId ?: error("The student is not known yet, call login() or loadMarksPage() first"),
    )

    /** The ajax request of the pages that have a semester filter: `studentID` and a JSON `query` with the semester. */
    private suspend fun sendSemesterPart(address: String, semesterId: String, studentId: String?): BarsResponse {
        val id = knownStudent(studentId)
        val query = """{"ID":"$id","FilterSemester":{"Value":"${checkedId(semesterId)}"}}"""
        val response = send(
            HttpMethod.Get,
            address,
            query = listOf("studentID" to id, "query" to query),
            ajax = true,
        )
        // A part is not a page, but the sign-in page comes instead of it when the session is gone
        val kind = BarsHtmlParser.detectPage(response.body)
        if (kind == BarsPageKind.LOGIN || kind == BarsPageKind.TWO_FACTOR) throw BarsSessionExpiredException()
        return response
    }

    /** Ends the session on the server and forgets the cookies. */
    suspend fun logout() {
        mutex.withLock {
            runCatching { send(HttpMethod.Get, endpoints.logout) }
            resetSession()
        }
    }

    /** Releases the HTTP client. The object is not usable after it. */
    fun close() {
        http.close()
    }

    private suspend fun handleAuthResponse(
        response: BarsResponse,
        sendCode: Boolean,
        afterCode: Boolean,
    ): BarsLoginResult {
        val body = response.body
        return when (BarsHtmlParser.detectPage(body)) {
            BarsPageKind.TWO_FACTOR -> {
                val page = BarsAuthParser.parseTwoFactorPage(body)
                pending = page
                // After a wrong code the page comes back with its error and a fresh token
                val pageError = page.error
                if (afterCode && pageError != null) {
                    BarsLoginResult.Rejected(pageError, canRetryCode = true)
                } else {
                    val sent = if (sendCode) requestCode(page, page.default) else null
                    BarsLoginResult.TwoFactorRequired(page, sent)
                }
            }
            BarsPageKind.LOGIN -> {
                pending = null
                BarsLoginResult.Rejected(BarsAuthParser.parseFormError(body) ?: "Не удалось войти", canRetryCode = false)
            }
            BarsPageKind.MARKS, BarsPageKind.SUMMARY, BarsPageKind.STUDENT_LIST -> {
                pending = null
                val students = BarsHtmlParser.parseStudentList(body)
                rememberStudent(response)
                BarsLoginResult.LoggedIn(studentId, students)
            }
            BarsPageKind.UNKNOWN -> {
                val formError = BarsAuthParser.parseFormError(body)
                if (formError != null && response.status < SERVER_ERROR) {
                    BarsLoginResult.Rejected(formError, canRetryCode = pending != null && afterCode)
                } else {
                    throw unexpectedPage(response)
                }
            }
        }
    }

    private suspend fun requestCode(page: BarsTwoFactorPage, provider: BarsTwoFactorProvider): BarsSendCodeResult {
        val response = send(
            HttpMethod.Get,
            resolve(endpoints.login, page.sendCodeUrl),
            query = listOf("tid" to provider.tid),
            ajax = true,
        )
        if (response.status >= SERVER_ERROR) throw BarsNetworkException("HTTP ${response.status} on the request for a code")
        return BarsAuthParser.parseSendCodeResult(response.body)
    }

    /** The id comes from the url of the page ("...Main?studentID=...") or, if the page has no id in it, from its links. */
    private fun rememberStudent(response: BarsResponse) {
        val fromUrl = STUDENT_ID_IN_URL.find(response.url)?.groupValues?.get(1)
        val fromLinks = BarsHtmlParser.parseStudentList(response.body).singleOrNull()?.id
        (fromUrl ?: fromLinks)?.let { studentId = it }
    }

    private fun checkedId(value: String): String {
        // The value goes into a query and into a JSON by hand, so it is limited to what an id is made of
        require(value.isNotEmpty() && value.all { it.isLetterOrDigit() || it == '-' }) { "Not an id: $value" }
        return value
    }

    private fun unexpectedPage(response: BarsResponse): Exception = when (BarsHtmlParser.detectPage(response.body)) {
        BarsPageKind.LOGIN, BarsPageKind.TWO_FACTOR -> BarsSessionExpiredException()
        BarsPageKind.STUDENT_LIST -> BarsNetworkException("The account has several students, pass studentId")
        else -> BarsNetworkException("Unexpected page (HTTP ${response.status}) at ${response.url.substringBefore('?')}")
    }

    private fun resolve(baseUrl: String, reference: String): String =
        URLBuilder(baseUrl).takeFrom(reference).buildString()

    private fun resetSession() {
        http.close()
        http = createHttp()
        pending = null
        studentId = null
    }

    private fun createHttp(): HttpClient =
        engine?.let { HttpClient(it) { configureBars() } } ?: HttpClient { configureBars() }

    private class BarsResponse(val status: Int, val body: String, val url: String)

    private suspend fun send(
        method: HttpMethod,
        address: String,
        query: List<Pair<String, String>> = emptyList(),
        form: Map<String, String>? = null,
        ajax: Boolean = false,
    ): BarsResponse {
        var currentUrl = address
        var currentMethod = method
        var currentQuery = query
        var currentForm = form
        var hops = 0
        while (true) {
            val queryNow = currentQuery
            val formNow = currentForm
            val methodNow = currentMethod
            val response = http.request(currentUrl) {
                this.method = methodNow
                url { queryNow.forEach { (name, value) -> parameters.append(name, value) } }
                if (formNow != null) {
                    setBody(FormDataContent(Parameters.build { formNow.forEach { (name, value) -> append(name, value) } }))
                }
                if (ajax) {
                    header("X-Requested-With", "XMLHttpRequest")
                    accept(ContentType.Application.Json)
                }
            }
            val status = response.status.value
            val location = response.headers[HttpHeaders.Location]
            if (location != null && status in REDIRECTS) {
                if (++hops > MAX_REDIRECTS) throw BarsNetworkException("Too many redirects")
                currentUrl = resolve(currentUrl, location)
                currentMethod = HttpMethod.Get
                currentQuery = emptyList()
                currentForm = null
                continue
            }
            if (status >= SERVER_ERROR) throw BarsNetworkException("HTTP $status at ${currentUrl.substringBefore('?')}")
            return BarsResponse(status, response.bodyAsText(), currentUrl + queryString(currentQuery))
        }
    }

    private fun queryString(query: List<Pair<String, String>>): String =
        if (query.isEmpty()) "" else "?" + query.joinToString("&") { (name, value) -> "$name=$value" }

    private companion object {
        const val SERVER_ERROR = 500
        val REDIRECTS = setOf(301, 302, 303, 307, 308)
    }
}
