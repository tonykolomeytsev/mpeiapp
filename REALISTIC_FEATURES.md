# ⚡ РЕАЛИСТИЧНЫЕ ФИЧИ - Что можно сделать быстро

## 🎯 ТОП-10 ФИЧЕЙ (По сложности реализации)

### 🟢 ЛЕГКО (1-3 дня каждая)

#### 1. 🎨 Полная кастомизация темы
**Что:** Пользователь выбирает любой акцентный цвет

```kotlin
// modules/feature/settings/

@Composable
fun ThemeCustomizationScreen() {
    var primaryColor by remember { mutableStateOf(Color(0xFF0072CE)) }
    
    ColorPicker(
        color = primaryColor,
        onColorChanged = { 
            primaryColor = it
            preferences.savePrimaryColor(it.toArgb())
        }
    )
    
    // Предпросмотр
    PreviewCard(primaryColor = primaryColor)
}
```

**Зачем:** Персонализация, каждый студент уникален  
**Сложность:** ⭐ (1 день)  
**WOW-фактор:** ⭐⭐⭐

---

#### 2. 📝 Умные напоминания о дедлайнах
**Что:** Напоминания с умным тайммингом

```kotlin
class DeadlineReminder {
    fun scheduleReminders(deadline: Deadline) {
        // 1 неделя до
        scheduleNotification(deadline.date.minusDays(7), 
            "Через неделю сдача ${deadline.name}")
        
        // 3 дня до
        scheduleNotification(deadline.date.minusDays(3),
            "Осталось 3 дня до ${deadline.name}! 🔥")
        
        // 1 день до
        scheduleNotification(deadline.date.minusDays(1),
            "ЗАВТРА ${deadline.name}! Успеешь? 😱")
        
        // День сдачи
        scheduleNotification(deadline.date.atTime(9, 0),
            "СЕГОДНЯ сдача ${deadline.name}!")
    }
}
```

**Зачем:** Студенты вечно забывают о дедлайнах  
**Сложность:** ⭐ (1 день)  
**WOW-фактор:** ⭐⭐⭐⭐

---

#### 3. 🍽️ Меню столовых
**Что:** Показывать меню и цены

```kotlin
// Просто парсить с сайта столовой или ручной ввод
data class CanteenMenu(
    val canteen: String,
    val date: LocalDate,
    val dishes: List<Dish>
)

data class Dish(
    val name: String,
    val category: DishCategory, // SOUP, MAIN, DESSERT
    val price: Int,
    val calories: Int?,
    val photo: String?
)

@Composable
fun CanteenMenuScreen() {
    val menu = viewModel.getMenuForToday()
    
    LazyColumn {
        items(menu.dishes.groupBy { it.category }) { (category, dishes) ->
            CategorySection(category, dishes)
        }
    }
}
```

**Зачем:** Планировать обед, знать цены  
**Сложность:** ⭐ (2 дня, если парсить)  
**WOW-фактор:** ⭐⭐⭐

---

#### 4. 📊 Простая статистика посещаемости
**Что:** Процент посещенных пар

```kotlin
class AttendanceTracker {
    // Отмечать посещенные пары
    suspend fun markAttended(classId: String)
    
    // Статистика
    suspend fun getStats(): AttendanceStats
}

data class AttendanceStats(
    val totalClasses: Int,
    val attended: Int,
    val percentage: Float,
    val streak: Int, // дней подряд посещал
    val bySubject: Map<String, Float>
)

@Composable
fun AttendanceStatsScreen() {
    val stats = viewModel.stats.collectAsState()
    
    Column {
        // Круговой прогресс
        CircularProgressIndicator(
            progress = stats.value.percentage / 100f,
            modifier = Modifier.size(200.dp)
        )
        
        Text("${stats.value.percentage}% посещаемость")
        
        // Стрик
        if (stats.value.streak > 0) {
            Text("🔥 ${stats.value.streak} дней подряд!")
        }
    }
}
```

**Зачем:** Геймификация, мотивация  
**Сложность:** ⭐ (2 дня)  
**WOW-фактор:** ⭐⭐⭐⭐

---

#### 5. 🔍 Глобальный поиск
**Что:** Искать везде одной строкой

```kotlin
@Composable
fun GlobalSearchScreen() {
    var query by remember { mutableStateOf("") }
    val results = viewModel.search(query).collectAsState()
    
    Column {
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = "Поиск по расписанию, БАРС, карте..."
        )
        
        LazyColumn {
            // Группировка результатов
            results.value.groupBy { it.category }.forEach { (category, items) ->
                item {
                    CategoryHeader(category)
                }
                items(items) { result ->
                    SearchResultItem(result)
                }
            }
        }
    }
}

sealed class SearchResult {
    data class ClassResult(val className: String, val time: String) : SearchResult()
    data class RoomResult(val roomNumber: String, val building: String) : SearchResult()
    data class GradeResult(val subject: String, val grade: String) : SearchResult()
    data class TeacherResult(val name: String, val department: String) : SearchResult()
}
```

**Зачем:** Быстро найти что угодно  
**Сложность:** ⭐⭐ (3 дня)  
**WOW-фактор:** ⭐⭐⭐⭐⭐

---

### 🟡 СРЕДНЕ (4-7 дней каждая)

#### 6. 🗓️ Экспорт расписания в Calendar
**Что:** Синхронизация с Google/Apple Calendar

```kotlin
class CalendarSync {
    suspend fun exportToCalendar(schedule: Schedule) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, schedule.className)
            putExtra(CalendarContract.Events.EVENT_LOCATION, schedule.room)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, schedule.startTime.toEpochMilli())
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, schedule.endTime.toEpochMilli())
            putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY") // повтор
        }
        context.startActivity(intent)
    }
    
    // Или массовый экспорт
    suspend fun exportWeek(week: Int) {
        val events = getScheduleForWeek(week)
        events.forEach { exportToCalendar(it) }
    }
}
```

**Зачем:** Интеграция с системным календарем  
**Сложность:** ⭐⭐ (4 дня)  
**WOW-фактор:** ⭐⭐⭐⭐

---

#### 7. 👥 Простая соцсеть (профили + чаты)
**Что:** Базовые профили и чаты по предметам

```kotlin
// Firebase Firestore для простоты
data class UserProfile(
    val userId: String,
    val name: String,
    val group: String,
    val photoUrl: String?,
    val skills: List<String>,
    val bio: String
)

data class SubjectChat(
    val subjectId: String,
    val subjectName: String,
    val members: List<String>,
    val messages: List<Message>
)

// Используем готовые библиотеки для чата
// Stream Chat или SendBird
```

**Зачем:** Общение, обмен конспектами  
**Сложность:** ⭐⭐ (5-7 дней с Firebase)  
**WOW-фактор:** ⭐⭐⭐⭐⭐

---

#### 8. 🎓 Базовый AI помощник (через Gemini API)
**Что:** Чат-бот для ответов на вопросы

```kotlin
class MPEIAssistant(private val gemini: GenerativeModel) {
    
    suspend fun ask(question: String): String {
        val context = """
        Ты помощник студентов МЭИ (Московский Энергетический Институт).
        Отвечай кратко и по делу. Если не знаешь - так и скажи.
        """
        
        val prompt = "$context\n\nВопрос: $question\n\nОтвет:"
        
        return gemini.generateContent(prompt).text ?: "Не могу ответить"
    }
    
    // Предзагруженные знания о МЭИ
    private val meiKnowledge = """
    - Главный корпус: м. Авиамоторная
    - Библиотека работает: Пн-Пт 9:00-19:00
    - Деканат А: корпус А, 3 этаж
    ...
    """
}

@Composable
fun AssistantScreen() {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var input by remember { mutableStateOf("") }
    
    Column {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                MessageBubble(message)
            }
        }
        
        Row {
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Спроси что-нибудь...") }
            )
            Button(onClick = {
                viewModel.sendMessage(input)
                input = ""
            }) {
                Icon(Icons.Default.Send, null)
            }
        }
    }
}
```

**Зачем:** Помощь студентам 24/7  
**Сложность:** ⭐⭐ (5 дней)  
**WOW-фактор:** ⭐⭐⭐⭐⭐

---

#### 9. 🎮 Простые обучающие игры
**Что:** Викторина по предметам

```kotlin
data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int,
    val explanation: String?
)

class QuizGame {
    suspend fun generateQuiz(subject: String, count: Int): List<Question> {
        // Можно генерировать через Gemini или брать из БД
        return database.getQuestions(subject, count)
    }
}

@Composable
fun QuizScreen() {
    var currentQuestion by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    val questions = viewModel.questions.collectAsState()
    
    Column {
        // Progress
        LinearProgressIndicator(
            progress = currentQuestion.toFloat() / questions.value.size
        )
        
        // Question
        Text(
            text = questions.value[currentQuestion].text,
            style = MaterialTheme.typography.headlineMedium
        )
        
        // Answers
        questions.value[currentQuestion].answers.forEachIndexed { index, answer ->
            Button(
                onClick = {
                    if (index == questions.value[currentQuestion].correctIndex) {
                        score++
                    }
                    currentQuestion++
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(answer)
            }
        }
    }
}
```

**Зачем:** Геймификация обучения  
**Сложность:** ⭐⭐ (4 дня)  
**WOW-фактор:** ⭐⭐⭐⭐

---

#### 10. 📚 Умная библиотека заметок
**Что:** AI для конспектов

```kotlin
class SmartNotes(private val gemini: GenerativeModel) {
    
    // Краткое содержание
    suspend fun summarize(text: String): String {
        return gemini.generateContent(
            "Сократи этот текст до ключевых моментов:\n\n$text"
        ).text ?: text
    }
    
    // Ключевые термины
    suspend fun extractKeyTerms(text: String): List<String> {
        val response = gemini.generateContent(
            "Выдели ключевые термины из этого текста списком:\n\n$text"
        ).text ?: ""
        
        return response.lines().filter { it.isNotBlank() }
    }
    
    // Генерация вопросов для самопроверки
    suspend fun generateQuestions(text: String): List<String> {
        val response = gemini.generateContent(
            "Составь 5 вопросов для самопроверки по этому материалу:\n\n$text"
        ).text ?: ""
        
        return response.lines().filter { it.startsWith("1.") || it.contains("?") }
    }
    
    // Объяснение простыми словами
    suspend fun explainSimply(text: String): String {
        return gemini.generateContent(
            "Объясни это простыми словами для студента:\n\n$text"
        ).text ?: text
    }
}

@Composable
fun EnhancedNoteScreen(noteId: String) {
    val note = viewModel.getNote(noteId).collectAsState()
    var aiSummary by remember { mutableStateOf<String?>(null) }
    
    Column {
        // Оригинальная заметка
        Text(note.value.text)
        
        Divider()
        
        // AI улучшения
        Row {
            Button(onClick = {
                viewModel.summarize(note.value.text)
            }) {
                Icon(Icons.Default.Compress, null)
                Text("Сократить")
            }
            
            Button(onClick = {
                viewModel.extractKeyTerms(note.value.text)
            }) {
                Icon(Icons.Default.Key, null)
                Text("Термины")
            }
            
            Button(onClick = {
                viewModel.generateQuestions(note.value.text)
            }) {
                Icon(Icons.Default.Quiz, null)
                Text("Вопросы")
            }
        }
        
        // Результат AI
        if (aiSummary != null) {
            Card {
                Text(aiSummary!!)
            }
        }
    }
}
```

**Зачем:** Помощь в обучении  
**Сложность:** ⭐⭐ (5 дней)  
**WOW-фактор:** ⭐⭐⭐⭐⭐

---

## 💰 Стоимость реализации

### Gemini API (для AI)
- **Free tier:** 60 запросов/минуту
- **Paid:** $0.00025 за 1K символов input
- **Достаточно** для 1000+ студентов бесплатно!

### Firebase (для чатов/соцсети)
- **Free tier:** 
  - 10GB storage
  - 50K reads/day
  - 20K writes/day
- **Достаточно** для старта!

### SendBird / Stream (готовые чаты)
- **Free tier:** до 100 MAU
- **Paid:** от $399/мес для unlimited

**Рекомендация:** Начать с Firebase + Gemini (бесплатно!)

---

## 🚀 ПЛАН НА 2 НЕДЕЛИ

### Неделя 1
- День 1-2: Кастомизация темы
- День 3-4: Умные напоминания
- День 5-7: Глобальный поиск

### Неделя 2  
- День 8-10: AI помощник (базовый)
- День 11-12: Меню столовых
- День 13-14: Статистика посещаемости

**Результат:** 6 новых крутых фич за 2 недели! 🎉

---

## 🎯 QUICK START: AI Помощник за выходные

### Суббота (4-6 часов)
```kotlin
// 1. Добавить Gemini SDK
implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

// 2. Создать базовый ассистент
class MPEIAssistant {
    private val gemini = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    
    suspend fun ask(question: String) = gemini.generateContent(question)
}

// 3. Простой UI
@Composable
fun AssistantScreen() {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var input by remember { mutableStateOf("") }
    
    Column {
        LazyColumn(Modifier.weight(1f)) {
            items(messages) { MessageBubble(it) }
        }
        Row {
            TextField(input, { input = it })
            IconButton(onClick = { 
                viewModel.send(input)
                input = ""
            }) {
                Icon(Icons.Default.Send, null)
            }
        }
    }
}
```

### Воскресенье (4-6 часов)
- Улучшить промпты для МЭИ
- Добавить контекст о университете
- Интеграция с расписанием
- Полировка UI

**Результат:** Рабочий AI помощник к понедельнику! 🤖

---

**Следующий шаг:** Какую фичу делаем первой? 🚀
