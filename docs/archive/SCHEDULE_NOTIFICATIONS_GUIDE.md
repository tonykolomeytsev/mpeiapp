# 🔔 СИСТЕМА УВЕДОМЛЕНИЙ ОБ ИЗМЕНЕНИЯХ РАСПИСАНИЯ

## ✅ Что создано

### 📦 Компоненты (5 файлов)

1. **ScheduleChangeDetector.kt** - Определение изменений в расписании
2. **ScheduleChangeWorker.kt** - Background worker для проверки
3. **ScheduleNotificationManager.kt** - Управление уведомлениями
4. **ScheduleCacheManager.kt** - Кеширование расписания
5. **ScheduleChangeScheduler.kt** - Планирование проверок
6. **ScheduleNotificationSettingsScreen.kt** - UI настроек (Compose)

---

## 🏗️ Архитектура

```
┌─────────────────────────────────────────────┐
│  ScheduleChangeScheduler                     │
│  (Запускает периодические проверки)         │
└────────────────┬────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────┐
│  ScheduleChangeWorker (WorkManager)         │
│  (Выполняется в фоне каждые N часов)        │
└────────────────┬────────────────────────────┘
                 │
    ┌────────────┼────────────┐
    ▼            ▼             ▼
┌────────┐  ┌──────────┐  ┌─────────────┐
│Schedule│  │Schedule  │  │Schedule     │
│Repo    │  │Cache     │  │Change       │
│        │  │Manager   │  │Detector     │
└────┬───┘  └────┬─────┘  └─────┬───────┘
     │           │               │
     │  Fetch    │  Compare      │  Detect
     │  Fresh    │  Old vs New   │  Changes
     │           │               │
     └───────────┴───────────────┘
                 │
                 ▼
      ┌──────────────────────┐
      │ScheduleNotification  │
      │ Manager              │
      │ (Показывает уведом.) │
      └──────────────────────┘
```

---

## 🔧 ШАГИ ДЛЯ ИНТЕГРАЦИИ

### Шаг 1: Добавить зависимости

Откройте `modules/feature/schedule/impl/build.gradle` и добавьте:

```gradle
dependencies {
    // ... existing dependencies ...
    
    // WorkManager для фоновых задач
    implementation(libs.androidx.work.runtime.ktx)
    
    // Kotlinx Serialization для кеширования
    implementation(libs.kotlinx.serialization.json)
    
    // Timber для логирования (уже должен быть)
    implementation(libs.timber)
}
```

### Шаг 2: Обновить gradle/libs.versions.toml

Добавьте версии, если их нет:

```toml
[versions]
work = "2.9.0"
kotlinxSerialization = "1.6.2"

[libraries]
androidx-work-runtime-ktx = { module = "androidx.work:work-runtime-ktx", version.ref = "work" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
```

### Шаг 3: Сделать Schedule Serializable

Обновите `modules/feature/schedule/api/src/main/kotlin/.../Schedule.kt`:

```kotlin
import kotlinx.serialization.Serializable

@Serializable
public data class Schedule(
    val name: String,
    val id: String,
    val type: ScheduleType,
    val weeks: List<Week>,
) : java.io.Serializable

@Serializable
public data class Week(
    val weekOfYear: Int,
    val weekOfSemester: WeekOfSemester,
    val firstDayOfWeek: LocalDate,
    val days: List<Day>,
) : java.io.Serializable

// ... и так далее для всех data classes
```

**Примечание:** Для LocalDate и LocalTime нужны custom serializers:

```kotlin
// Добавьте в Schedule.kt
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalTime

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())
}

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: LocalTime) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): LocalTime = LocalTime.parse(decoder.decodeString())
}

// Используйте аннотацию
@Serializable
public data class Day(
    val dayOfWeek: Int,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val classes: List<Classes>,
) : java.io.Serializable
```

### Шаг 4: Добавить в Koin DI

Создайте или обновите DI модуль:

```kotlin
// modules/feature/schedule/impl/src/main/kotlin/.../di/FeatureScheduleModule.kt

val scheduleNotificationsModule = module {
    // Managers
    single { ScheduleNotificationManager(androidContext()) }
    single { ScheduleCacheManager(androidContext(), get()) }
    single { ScheduleChangeScheduler(androidContext()) }
    
    // Json for serialization
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = false
        }
    }
}

// В главном модуле добавьте:
val featureScheduleModule = module {
    includes(scheduleNotificationsModule)
    // ... остальные модули
}
```

### Шаг 5: Запустить планировщик при старте приложения

В `Application.onCreate()` или в главном экране:

```kotlin
class MpeixApplication : Application(), KoinComponent {
    
    private val scheduleChangeScheduler: ScheduleChangeScheduler by inject()
    
    override fun onCreate() {
        super.onCreate()
        // ... другая инициализация
        
        // Запустить проверку изменений расписания
        scheduleChangeScheduler.startPeriodicChecks(intervalHours = 6)
    }
}
```

### Шаг 6: Добавить настройки в Settings экран

В экране настроек добавьте навигацию к настройкам уведомлений:

```kotlin
@Composable
fun SettingsScreen() {
    // ... другие настройки
    
    SettingsItem(
        title = "Уведомления об изменениях",
        subtitle = "Настроить проверку расписания",
        icon = Icons.Default.Notifications,
        onClick = { navController.navigate("notification_settings") }
    )
}
```

### Шаг 7: Добавить иконку уведомления

Создайте `modules/feature/schedule/impl/src/main/res/drawable/ic_notification_schedule.xml`:

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24"
    android:tint="?attr/colorControlNormal">
    <path
        android:fillColor="@android:color/white"
        android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10s10,-4.48 10,-10S17.52,2 12,2zM12,20c-4.41,0 -8,-3.59 -8,-8s3.59,-8 8,-8s8,3.59 8,8S16.41,20 12,20zM12.5,7H11v6l5.25,3.15l0.75,-1.23l-4.5,-2.67V7z"/>
</vector>
```

### Шаг 8: Добавить разрешения в AndroidManifest

В `modules/feature/schedule/impl/src/main/AndroidManifest.xml`:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    
    <!-- Разрешение на уведомления (Android 13+) -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
    
    <!-- Разрешение на интернет (для проверки расписания) -->
    <uses-permission android:name="android.permission.INTERNET"/>
    
</manifest>
```

### Шаг 9: Запросить разрешение на уведомления (Android 13+)

```kotlin
// В настройках или при первом запуске
@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Разрешение получено
                Toast.makeText(context, "Уведомления включены", Toast.LENGTH_SHORT).show()
            }
        }
        
        LaunchedEffect(Unit) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
```

---

## 🧪 ТЕСТИРОВАНИЕ

### Ручное тестирование

```kotlin
// В любом месте приложения для теста
class TestScheduleNotifications {
    fun test() {
        val scheduler = ScheduleChangeScheduler(context)
        
        // Триггернуть немедленную проверку
        scheduler.triggerImmediateCheck()
        
        // Подождать 30 секунд и проверить уведомления
    }
}
```

### Имитация изменений

Для тестирования создайте fake изменения:

```kotlin
val testChanges = listOf(
    ScheduleChange(
        type = ScheduleChange.ChangeType.MODIFIED,
        date = LocalDate.now().plusDays(1),
        oldClass = Classes(/* ... */),
        newClass = Classes(/* ... измененная пара */)
    )
)

notificationManager.showScheduleChangesNotification(testChanges)
```

---

## 📊 ПРИМЕР ИСПОЛЬЗОВАНИЯ

### 1. Пользователь включает уведомления

```kotlin
// Пользователь включает настройку
viewModel.toggleNotifications(true)

// Внутри:
scheduleChangeScheduler.startPeriodicChecks(intervalHours = 6)
```

### 2. Фоновая проверка (каждые 6 часов)

```
WorkManager → ScheduleChangeWorker.doWork()
    ↓
Fetch fresh schedule from API
    ↓
Compare with cached schedule
    ↓
Detect changes (if any)
    ↓
Send notification
    ↓
Update cache
```

### 3. Пользователь видит уведомление

```
Notification:
┌─────────────────────────────────────┐
│ 🔔 Изменения в расписании          │
│                                     │
│ У вас 2 изменения на этой неделе   │
│                                     │
│ Завтра:                            │
│ • Отменена пара: Математика        │
│ • Изменена пара Физика:            │
│   аудитория (А-305 → А-401)        │
└─────────────────────────────────────┘
     │
     │ tap
     ▼
Открывается экран расписания
```

---

## ⚙️ НАСТРОЙКИ

### Частота проверки

По умолчанию: **6 часов**

Можно изменить:
- Минимум: 1 час
- Максимум: 24 часа

### Условия проверки

- ✅ Только при подключении к интернету
- ✅ Только когда батарея не разряжена
- ✅ Только для выбранного расписания

---

## 🔍 ОТЛАДКА

### Логи

Все компоненты логируют через Timber:

```bash
# Фильтр logcat
adb logcat | grep "ScheduleChange"
```

Смотрите логи:
- `ScheduleChangeWorker: Starting schedule check`
- `ScheduleChangeDetector: Detected X changes`
- `ScheduleNotificationManager: Notification sent`

### Проверка WorkManager

```kotlin
// Получить информацию о задаче
val workInfos = workManager.getWorkInfosForUniqueWork(
    ScheduleChangeWorker.WORK_NAME
).get()

workInfos.forEach { workInfo ->
    Log.d("WorkManager", "State: ${workInfo.state}")
    Log.d("WorkManager", "Run attempt: ${workInfo.runAttemptCount}")
}
```

---

## 🚀 БЫСТРЫЙ СТАРТ

### Минимальная интеграция (30 минут)

1. Скопируйте все 6 созданных файлов в проект
2. Добавьте зависимости в `build.gradle`
3. Добавьте Koin модуль
4. Запустите в `Application.onCreate()`:
   ```kotlin
   scheduleChangeScheduler.startPeriodicChecks()
   ```
5. Готово! Уведомления работают 🎉

### Полная интеграция (2-3 часа)

Все шаги из раздела "Шаги для интеграции" выше.

---

## 📈 МЕТРИКИ

После внедрения отслеживайте:

- Количество активных пользователей с уведомлениями
- Частота обнаружения изменений
- Время ответа воркера
- Click-through rate уведомлений

---

## 🐛 ИЗВЕСТНЫЕ ПРОБЛЕМЫ

### 1. WorkManager может не работать на Xiaomi/Huawei

**Решение:** Добавить инструкцию пользователям разрешить фоновую работу

### 2. Уведомления не приходят в Doze Mode

**Решение:** Используется `setRequiresBatteryNotLow(true)` - это норма

### 3. Кеш расписания большой

**Решение:** Очищается автоматически при смене расписания

---

## 🎯 СЛЕДУЮЩИЕ ШАГИ

### Улучшения

1. **Умные уведомления** - только важные изменения
2. **История изменений** - показывать что изменилось за неделю
3. **Уведомления перед парой** - "Через 15 минут пара в А-305"
4. **Push через FCM** - мгновенные уведомления от сервера

---

**Статус:** ✅ Готово к интеграции  
**Время интеграции:** 30 минут - 3 часа  
**Сложность:** ⭐⭐ (Средняя)

Удачи! 🚀
