# Комплексный архитектурный анализ перехода «МЭИ.Хаб» на Compose Multiplatform (Android + iOS)

> **Статус документа:** Исследовательский / Проектно-архитектурный анализ  
> **Целевая аудитория:** Архитекторы, Mobile Tech Lead, Android/iOS инженеры  
> **Проект:** «МЭИ.Хаб» (`mpei-next`)  
> **Версия документа:** 1.0  
> **Дата:** Октябрь 2026 г.  

---

## Оглавление
1. [Введение и стратегическое резюме](#1-введение-и-стратегическое-резюме)
2. [Текущее состояние архитектуры «МЭИ.Хаб» (Android)](#2-текущее-состояние-архитектуры-мэихаб-android)
3. [Целевая мультиплатформенная топология](#3-целевая-мультиплатформенная-топология)
4. [Детальный анализ модулей и стратегия миграции](#4-детальный-анализ-модулей-и-стратегия-миграции)
5. [Пользовательский интерфейс, рендеринг и дизайн-система](#5-пользовательский-интерфейс-рендеринг-и-дизайн-система)
6. [Навигация: полный отказ от Fragments](#6-навигация-полный-отказ-от-fragments)
7. [Сетевой слой: миграция Retrofit -> Ktor Client](#7-сетевой-слой-миграция-retrofit---ktor-client)
8. [База данных и локальное хранилище](#8-база-данных-и-локальное-хранилище)
9. [Виджеты рабочего стола: Glance (Android) vs WidgetKit (iOS)](#9-виджеты-рабочего-стола-glance-android-vs-widgetkit-ios)
10. [Фоновые процессы и система уведомлений](#10-фоновые-процессы-и-система-уведомлений)
11. [Локализация и переключение языков](#11-локализация-и-переключение-языков)
12. [Анимации, физика жестов и экраны с высокой частотой (120 Hz)](#12-анимации-физика-жестов-и-экраны-с-высокой-частотой-120-hz)
13. [Темы оформления и специфика OLED](#13-темы-оформления-и-специфика-oled)
14. [Картография: решение проблемы OSMDroid](#14-картография-решение-проблемы-osmdroid)
15. [Влияние на существующую платформу Android (Нюансы и риски)](#15-влияние-на-существующую-платформу-android-нюансы-и-риски)
16. [Плюсы и минусы перехода на Compose Multiplatform](#16-плюсы-и-минусы-перехода-на-compose-multiplatform)
17. [Сборочный процесс, тулинг и стратегия валидации без Mac](#17-сборочный-процесс-тулинг-и-стратегия-валидации-без-mac)
18. [Пошаговая дорожная карта (Roadmap)](#18-пошаговая-дорожная-карта-roadmap)

---

## 1. Введение и стратегическое резюме

Приложение «МЭИ.Хаб» — это развитая клиентская система для студентов и преподавателей НИУ «МЭИ», включающая отображение расписания, интеграцию с личным кабинетом БАРС с поддержкой 2FA, заметки, карту кампуса и виджеты.

Выбор **Compose Multiplatform (CMP)** в качестве целевой технологии портирования на iOS обусловлен тем, что:
- Кодовая база уже написана на современном **Kotlin (2.2.x)**;
- Значительная часть UI уже реализована с помощью декларативного **Jetpack Compose**;
- Архитектура выстроена по принципам **Clean Architecture + TEA/MVI (Elmslie)**, где состояние и бизнес-логика отделены от представления;
- Compose Multiplatform обеспечивает переиспользование **до 85–90% кодовой базы**, избавляя от необходимости создавать и поддерживать две независимые реализации сложной логики (парсинг расписания, протокол 2FA БАРС, кэширование).

### Схема 1: Текущая архитектура Android vs Целевая Multiplatform-архитектура

```
+-----------------------------------------------------------------------------------+
|                        ТЕКУЩАЯ АРХИТЕКТУРА (ANDROID ONLY)                        |
+-----------------------------------------------------------------------------------+
|  UI Слой:      [Jetpack Compose]  +  [Android ViewBinding / Fragments]            |
|  Навигация:    [androidx.fragment.app.FragmentManager]                            |
|  MVI:          [Elmslie Android] -> UIState / Effect                              |
|  Сеть:         [Retrofit 3] + [OkHttp 5] (JVM Only)                               |
|  БД:           [Android Room 2.8] (Android SQLite Framework)                      |
|  Виджеты:      [Jetpack Glance (AppWidgetProvider)]                              |
|  Фон:          [AndroidX WorkManager]                                             |
|  Среда:        Android Runtime (ART / JVM 17-21)                                  |
+-----------------------------------------------------------------------------------+
                                         |
                                         V МИГРАЦИЯ В
+-----------------------------------------------------------------------------------+
|                  ЦЕЛЕВАЯ АРХИТЕКТУРА (COMPOSE MULTIPLATFORM)                      |
+-----------------------------------------------------------------------------------+
|                                                                                   |
|  +-----------------------------------------------------------------------------+  |
|  |                          ОБЩИЙ КОД (commonMain)                             |  |
|  |  * UI: Compose Multiplatform (org.jetbrains.compose) + MpeixTheme Kit       |  |
|  |  * Навигация: Pure Compose Navigation / Router (без Fragment)               |  |
|  |  * MVI: Elmslie Core (чистый Kotlin KMP)                                    |  |
|  |  * Бизнес-логика: UseCases, Репозитории, Мапперы, Валидаторы                 |  |
|  |  * Сеть: Ktor 3 Client + kotlinx.serialization                              |  |
|  |  * БД: Room 2.8+ KMP (SQLite Bundled Driver)                                |  |
|  |  * Ресурсы: Compose Multiplatform Resources (Res.string, Res.drawable)       |  |
|  |  * DI: Koin Core / Koin Compose Multiplatform                               |  |
|  +-----------------------------------------------------------------------------+  |
|                                         |                                         |
|                  +----------------------+----------------------+                  |
|                  |                                             |                  |
|                  V                                             V                  |
|  +-------------------------------+             +-------------------------------+  |
|  |    ANDROID (androidMain)      |             |        IOS (iosMain)          |  |
|  |  * MainActivity (Edge-to-Edge)|             |  * SwiftUI App Shell (iosApp) |  |
|  |  * Ktor OkHttp Engine         |             |  * ComposeUIViewController    |  |
|  |  * Glance AppWidget           |             |  * Ktor Darwin Engine         |  |
|  |  * WorkManager (Background)   |             |  * WidgetKit (SwiftUI Widget) |  |
|  |  * OSMDroid Map Engine        |             |  * BGAppRefreshTask (Bg tasks)|  |
|  |  * System NotificationManager |             |  * Apple MapKit / Leaflet Web |  |
|  |  * ART / DEX компиляция       |             |  * UNUserNotificationCenter   |  |
|  +-------------------------------+             |  * Kotlin/Native (LLVM/Mach-O)|  |
|                                                +-------------------------------+  |
+-----------------------------------------------------------------------------------+
```

---

## 2. Текущее состояние архитектуры «МЭИ.Хаб» (Android)

Проект представляет собой модульный Gradle-монорепозиторий (более 40 модулей).
Основные модульные группы:
- `build-logic`: Композитный билд Gradle (`convention plugins`), инкапсулирующий типовые конфигурации модулей.
- `modules/app`: Главный модуль запуска, `MainActivity`, инициализация DI Koin, SplashScreen.
- `modules/feature`: Фичи приложения (`schedule`, `bars`, `notes`, `map`, `dashboard`, `onboarding`, `settings`, `search`).
- `modules/lib`: Общие библиотеки инфраструктуры (`network`, `app_database`, `navigation`, `elm`, `elm_compose`, `schedule`, `persistent_cache`).
- `modules/ui`: Дизайн-система (`theme`, `kit/*`, `icons`, `shimmer`, `splash`).
- `modules/res`: Ресурсные модули (`strings`, `icons`, `fonts`, `images`).

### Ключевые препятствия для немедленной сборки под iOS:
1. **Жесткая связь с Android Fragments:** Интерфейсы навигации `Router` оперируют `FragmentManager`, экраны расширяют `androidx.fragment.app.Fragment` или `ComposeFragment`.
2. **JVM-зависимый сетевой слой:** Библиотека `Retrofit` работает только на JVM.
3. **Android View-компоненты:** Экраны карт используют `osmdroid` и Android Canvas/Views.
4. **Хранилище настроек:** Модуль `ext_shared_preferences` завязан на `android.content.SharedPreferences`.
5. **Виджеты:** Реализация на базе `androidx.glance`, ориентированная на Android RemoteViews.
6. **Фоновая работа:** Использование `androidx.work.WorkManager`.

---

## 3. Целевая мультиплатформенная топология

При переходе на KMP/CMP модульная структура сохраняет соглашения `feature_*_api` / `feature_*_impl`, но преобразуется в мультиплатформенный формат с иерархией `source sets`.

### Схема 2: Модульная организация и распространение зависимостей

```
+-------------------------------------------------------------------------------------+
|                                   :modules:app                                      |
|            (Android Application Module -> com.android.application)                  |
+-------------------------------------------------------------------------------------+
        |                                                            ^
        | depends on                                                 | framework export
        V                                                            |
+-------------------------------------------------------------------------------------+
|                                  :modules:shared                                    |
|              (KMP Umbrella Framework -> XCFramework for Xcode / Swift)              |
+-------------------------------------------------------------------------------------+
        |
        +----------------------------+----------------------------+
        |                            |                            |
        V                            V                            V
+-----------------------+    +-----------------------+    +-----------------------+
| :feature_schedule_impl|    |   :feature_bars_impl  |    |   :feature_map_impl   |
+-----------------------+    +-----------------------+    +-----------------------+
        |                            |                            |
        V                            V                            V
+-----------------------+    +-----------------------+    +-----------------------+
|  :feature_schedule_api|    |   :feature_bars_api   |    |    :feature_map_api   |
+-----------------------+    +-----------------------+    +-----------------------+
        |                            |                            |
        +----------------------------+----------------------------+
                                     |
                                     V
        +---------------------------------------------------------+
        |                  :modules:ui:kit & :ui:theme            |
        |              (Compose Multiplatform Design System)      |
        +---------------------------------------------------------+
                                     |
                                     V
        +---------------------------------------------------------+
        |          :modules:lib:network & :lib:app_database       |
        |           (Ktor Client Engine + Room KMP Driver)        |
        +---------------------------------------------------------+
```

### Схема 3: Иерархия Source Sets в KMP-модулях

```
                                    +--------------------+
                                    |     commonMain     |
                                    | (Kotlin + CMP UI)  |
                                    +--------------------+
                                      /                \
                                     /                  \
                                    V                    V
                         +--------------------+    +--------------------+
                         |    androidMain     |    |      iosMain       |
                         |  (Android SDK API) |    | (Apple / UIKit API)|
                         +--------------------+    +--------------------+
                                                    /         |        \
                                                   /          |         \
                                                  V           V          V
                                        +------------+ +------------+ +------------+
                                        |  iosArm64  | |iosSimulator| |   iosX64   |
                                        | (iPhone HW)| | Arm64 (Mac)| |(Intel Mac) |
                                        +------------+ +------------+ +------------+
```

---

## 4. Детальный анализ модулей и стратегия миграции

### Таблица миграции модулей

| Модуль | Текущий плагин Gradle | Целевой плагин | Сложность | Действия при миграции |
|---|---|---|---|---|
| `build-logic` | Composite build (JVM/AGP) | Composite build (KMP/AGP/CMP) | Средняя | Добавить конвеншн-плагины для KMP и CMP |
| `modules/app` | `mpeix.plugins.app` | Android App + связь с `:shared` | Низкая | Тонкий запуск Compose на Android |
| `modules/shared` | *Отсутствует* | `mpeix.plugins.shared.umbrella` | Высокая | Новый модуль для экспорта XCFramework |
| `feature_*_api` | `mpeix.plugins.feature.api` | `mpeix.plugins.feature.kmp.api` | Низкая | Перевод на чистый Kotlin KMP |
| `feature_*_impl` | `mpeix.plugins.feature.impl.compose` | `mpeix.plugins.feature.kmp.impl` | Высокая | Удаление Fragment, миграция на CMP |
| `lib_network` | `mpeix.plugins.lib.kotlin` | `mpeix.plugins.lib.kmp` | Высокая | Замена Retrofit/OkHttp на Ktor 3 |
| `lib_app_database`| `mpeix.plugins.lib.android` | `mpeix.plugins.lib.kmp` | Средняя | Room 2.8 KMP + SQLite Driver |
| `lib_navigation` | `mpeix.plugins.lib.android` | `mpeix.plugins.lib.kmp` | Высокая | Замена Fragment Router на Compose Router |
| `ui_theme`, `ui_kit`| `mpeix.plugins.lib.android.compose` | `mpeix.plugins.ui.kmp` | Средняя | Перенос на Compose Multiplatform |
| `res_strings` | Android Library (XML res) | CMP Resources (`Res.string`) | Средняя | Конвертация в `composeResources` |

---

## 5. Пользовательский интерфейс, рендеринг и дизайн-система

### Архитектура рендеринга: ART vs Metal/Skiko

На Android Jetpack Compose компилируется в стандартные инструкции JVM/ART и рисует на системном `android.graphics.Canvas`.
На iOS Compose Multiplatform использует графический движок **Skiko** (библиотека JetBrains на базе **Skia**, портированная на Apple Metal).

### Схема 4: Сравнение пайплайнов рендеринга Compose

```
ANDROID:
[Compose Runtime] -> [Layout & Draw Nodes] -> [Android RenderNode] -> [HWUI / Vulkan] -> [SurfaceFlinger]

IOS:
[Compose Runtime] -> [Layout & Draw Nodes] -> [Skiko Canvas (Skia)] -> [Apple Metal API] -> [CAMetalLayer]
```

### Нюансы и особенности рендеринга:
1. **Производительность:** На чипах Apple Silicon (A15–A18, M-серия) Compose Multiplatform рендерит интерфейс с высокой производительностью благодаря прямому сопоставлению команд Skia с вызовами Apple Metal.
2. **Шрифты:** Android использует шрифтовые файлы через `androidx.compose.ui.text.font`. В CMP шрифты должны лежать в `composeResources/font/` для идентичного отображения глифов без опоры на системные шрифты iOS (San Francisco).
3. **Safe Area и Dynamic Island:** На iOS статус-бар, «челка» и Dynamic Island требуют обработки отступов через `WindowInsets.safeDrawing` или `WindowInsets.statusBars`.

---

## 6. Навигация: полный отказ от Fragments

### Проблема текущего решения
В Android-версии приложения навигация реализована через паттерн `Router` + `Command`, которые выполняют `commitNow` транзакции во `fragmentManager`.
Для iOS фрагменты не существуют в принципе.

### Схема 5: Жизненный цикл экрана (Было / Станет)

```
БЫЛО (Android Fragments):
[Activity] 
   └── [FragmentManager]
          └── [Fragment (Lifecycle: onAttach -> onCreate -> onCreateView -> onViewCreated)]
                 └── [ComposeView / XML Binding]
                        └── [Elmslie Store Subscription]

СТАНЕТ (Compose Multiplatform Navigation):
[UI Window (Android Activity / iOS ComposeUIViewController)]
   └── [Compose Navigation Host / Router]
          └── [NavEntry (Route)]
                 └── [@Composable Screen]
                        └── [Koin Inject Store -> Elmslie Store (CoroutineScope)]
```

### Схема 6: Мультиплатформенный роутинг и стек навигации

```
+------------------------------------------------------------------------------------+
|                                  AppNavigationHost                                 |
+------------------------------------------------------------------------------------+
|                                                                                    |
|  +--------------------+      +--------------------+      +--------------------+    |
|  |   ScheduleScreen   | <--> |   DashboardScreen  | <--> |     BarsScreen     |    |
|  |     (Tab 1)        |      |      (Tab 2)       |      |      (Tab 3)       |    |
|  +--------------------+      +--------------------+      +--------------------+    |
|            |                                                        |              |
|            V push                                                   V push         |
|  +--------------------+                                  +--------------------+    |
|  | FindScheduleScreen |                                  |  BarsSubjectDetail |    |
|  +--------------------+                                  +--------------------+    |
|                                                                                    |
+------------------------------------------------------------------------------------+
| Навигационный стек: Stack<NavTarget> (Хранится в общем StateFlow / NavController)  |
+------------------------------------------------------------------------------------+
```

### Выбор навигационного стека
Рекомендуется использовать:
- Либо **Jetpack Navigation 2.8+ Compose KMP** (официально поддерживается Google и JetBrains для KMP с сохранением типобезопасности Type-Safe Navigation);
- Либо **Voyager** — легковесная multiplatform библиотека навигации с встроенной поддержкой вкладок, экранов и Koin.

---

## 7. Сетевой слой: миграция Retrofit -> Ktor Client

### Проблема Retrofit
`Retrofit` является Java/Android-библиотекой, использующей рефлексию `java.lang.reflect.Proxy`, которая не поддерживается в Kotlin/Native (AOT компиляция под iOS).

### Решение: Ktor 3.x Client
`Ktor Client` работает на всех платформах через переключаемые сетевые движки:
- На Android: `OkHttp Engine` (поддержка HTTP/2, пула соединений, Chucker);
- На iOS: `Darwin Engine` (на базе `NSURLSession`, поддержка HTTP/3, системного VPN, Keychain и прокси iOS).

### Схема 7: Сетевой стек Ktor Client в Multiplatform

```
+------------------------------------------------------------------------------------+
|                         Общий сетевой сервис (commonMain)                          |
|             ScheduleRestApi / BarsRestApi (Ktor HttpClient + JSON Plugin)          |
+------------------------------------------------------------------------------------+
                                          |
                        +-----------------+-----------------+
                        |                                   |
                        V                                   V
        +-------------------------------+   +-------------------------------+
        |    Android (androidMain)      |   |        iOS (iosMain)          |
        |       OkHttp Engine           |   |       Darwin Engine           |
        |   (OkHttpClient + Chucker)    |   |     (NSURLSession API)        |
        +-------------------------------+   +-------------------------------+
```

### Специфика авторизации БАРС:
Личный кабинет БАРС требует сохранения кук сессий (`ASP.NET_SessionId`) и 2FA токенов. В Ktor Client это реализуется через общий мультиплатформенный плагин:
`HttpCookies` с хранилищем `AcceptAllCookiesStorage()` или кастомным персистентным хранилищем.

---

## 8. База данных и локальное хранилище

### Room 2.8+ KMP
Начиная с версии 2.7/2.8, Google Room поддерживает Kotlin Multiplatform.
- На Android база работает через `AndroidSQLiteDriver`;
- На iOS база работает через `SQLiteDriver` (`androidx.sqlite:sqlite-bundled`), использующий статически слинкованную библиотеку SQLite3.

### Схема 8: Архитектура Room Database в KMP

```
+------------------------------------------------------------------------------------+
|                                    commonMain                                      |
|  * @Database: AppDatabase : RoomDatabase                                           |
|  * @Dao: ScheduleDao, NotesDao                                                     |
|  * @Entity: ScheduleEntity, NoteEntity                                             |
+------------------------------------------------------------------------------------+
                                          |
                        +-----------------+-----------------+
                        |                                   |
                        V                                   V
        +-------------------------------+   +-------------------------------+
        |          androidMain          |   |            iosMain            |
        |  getDatabaseBuilder(...)      |   |  getDatabaseBuilder(...)      |
        |  Driver: AndroidSQLiteDriver  |   |  Driver: BundledSQLiteDriver  |
        |  Путь: context.getDatabase... |   |  Путь: NSFileManager / Docs   |
        +-------------------------------+   +-------------------------------+
```

### Хранилище ключ-значение (Настройки приложения)
Замена `SharedPreferences` на **`multiplatform-settings`**:
- На Android: работает поверх `SharedPreferences` или `EncryptedSharedPreferences`;
- На iOS: работает поверх системного `NSUserDefaults(suiteName: "group.mpeix")`.

### Схема 9: Слой настроек приложения

```
+------------------------------------------------------------------------------------+
|                               AppSettingsRepository                                |
|                        (Интерфейс в :feature_app_settings_api)                     |
+------------------------------------------------------------------------------------+
                                          |
                                          V
+------------------------------------------------------------------------------------+
|                         ObservableSettings (multiplatform-settings)                |
+------------------------------------------------------------------------------------+
                        |                                   |
                        V                                   V
        +-------------------------------+   +-------------------------------+
        |    Android SharedPreferences  |   |        iOS NSUserDefaults     |
        +-------------------------------+   +-------------------------------+
```

---

## 9. Виджеты рабочего стола: Glance (Android) vs WidgetKit (iOS)

Это важнейшая функциональная часть расписания. Невозможно написать один виджет на Compose для двух систем.

### Различия платформ:
1. **Android Glance:** Рендерится через Jetpack Compose Glance в стандартные `RemoteViews` Android. Обновляется через `GlanceAppWidgetManager`.
2. **iOS WidgetKit:** Не поддерживает сторонние движки рендеринга (включая Compose/Skiko). Виджет пишется **только на SwiftUI** в нативном таргете Xcode (`ScheduleWidgetExtension`).

### Решение для iOS: Shared Data Layer (App Group)
Общий Kotlin-модуль формирует легковесный JSON-снапшот текущего расписания и сохраняет его в директорию `App Group` или `NSUserDefaults(suiteName: "group.ru.kekmech.mpeiapp")`. Swift WidgetKit читает этот JSON и отображает виджет с нулевым оверхедом.

### Схема 10: Архитектура виджетов двух платформ

```
+------------------------------------------------------------------------------------+
|                       ScheduleWidgetDataSynchronizer (KMP)                         |
|                 (Формирует WidgetTimelineData: пары на сегодня/завтра)              |
+------------------------------------------------------------------------------------+
                        |                                   |
                        V                                   V
+-------------------------------+   +------------------------------------------------+
|     ANDROID (androidMain)     |   |                  IOS (iosMain)                 |
|                               |   |                                                |
|  [GlanceAppWidget.update()]   |   |  [Запись JSON в AppGroup Shared Directory]     |
|              |                |   |                       |                        |
|              V                |   |                       V                        |
|   [Android RemoteViews UI]    |   |  [WidgetCenter.shared.reloadAllTimelines()]    |
|   (Отображение на Home Screen)|   |                       |                        |
|                               |   |                       V                        |
|                               |   |          [SwiftUI WidgetExtension]             |
|                               |   |          (Нативный WidgetKit на iOS)           |
+-------------------------------+   +------------------------------------------------+
```

---

## 10. Фоновые процессы и система уведомлений

### Фоновые задачи: WorkManager vs BackgroundTasks
В Android-версии приложения `ScheduleChangeWorker` запускается периодически через `WorkManager` для отслеживания изменений аудиторий или отмены пар.

- **Android:** `PeriodicWorkRequest` запускается с гибким интервалом (от 15 минут) с гарантированным выполнением при наличии сети (`Constraints(NetworkType.CONNECTED)`).
- **iOS:** iOS накладывает жесткие ограничения на фоновое выполнение. Механизм `BGAppRefreshTask` (BackgroundTasks framework) не гарантирует запуск в точное время — iOS сама выбирает момент пробуждения на основе машинного обучения (когда пользователь чаще всего открывает приложение) и заряда батареи. Время работы ограничено **30 секундами**.

### Локальные уведомления
- **Android:** `NotificationCompat.Builder`, каналы `NotificationChannel` (`NotificationManagerCompat`).
- **iOS:** `UNUserNotificationCenter` (`UNMutableNotificationContent`, `UNTimeIntervalNotificationTrigger`).

### Схема 11: Пайплайн фоновых задач и уведомлений

```
+------------------------------------------------------------------------------------+
|                         ScheduleChangeDetector (commonMain)                        |
|                  (Сравнивает кэш расписания с сервером ts.mpei.ru)                 |
+------------------------------------------------------------------------------------+
                        |                                   |
                        V                                   V
+-------------------------------+   +------------------------------------------------+
|          androidMain          |   |                    iosMain                     |
|                               |   |                                                |
|  [androidx.work.WorkManager]  |   |  [BGTaskScheduler.register("schedule.refresh")]|
|  * Constraints(NetworkType)   |   |  * Запуск BGAppRefreshTask (макс 30 сек)       |
|  * PeriodicWorkRequest (6h)   |   |  * URLSession download in background           |
|              |                |   |                       |                        |
|              V                |   |                       V                        |
|  [NotificationManagerCompat]  |   |  [UNUserNotificationCenter.current().add()]    |
|  (Heads-up push notification) |   |  (Системный баннер уведомления iOS)            |
+-------------------------------+   +------------------------------------------------+
```

---

## 11. Локализация и переключение языков

### Проблема Android LocaleWrapper
Сейчас `MainActivity` переопределяет язык вызовом:
`attachBaseContext(LocaleContextWrapper.wrapContext(newBase))`
Это Android-специфичный хак с подменой `Configuration.locale`.

### Решение в Compose Multiplatform: CMP Resources
Compose Multiplatform 1.6+ включает официальную систему ресурсов:
- Строки размещаются в `commonMain/composeResources/values/strings.xml`, `values-ru/strings.xml`, `values-en/strings.xml`.
- Генератор компилирует их в типобезопасный класс `Res.string.*`.
- В коде интерфейса используется функция: `stringResource(Res.string.schedule_title)`.

### Схема 12: Пайплайн локализации и переключения языков

```
+------------------------------------------------------------------------------------+
|                               composeResources (XML)                              |
|  values/strings.xml (en)  |  values-ru/strings.xml (ru)                            |
+------------------------------------------------------------------------------------+
                                          |
                                          V компиляция CMP
+------------------------------------------------------------------------------------+
|                                Res.string.xxx (KMP)                                |
+------------------------------------------------------------------------------------+
                                          |
                                          V
+------------------------------------------------------------------------------------+
|                            CompositionLocalProvider                                |
|        (LocalLocalization / AppLanguageState: "ru_RU" | "en_US" | "system")        |
+------------------------------------------------------------------------------------+
                        |                                   |
                        V                                   V
        +-------------------------------+   +-------------------------------+
        |            Android            |   |              iOS              |
        |  AppCompatDelegate.setLocales |   |  Settings.bundle / Per-App    |
        |   (Поддержка Android 13+)     |   |  Language меню iOS            |
        +-------------------------------+   +-------------------------------+
```

---

## 12. Анимации, физика жестов и экраны с высокой частотой (120 Hz)

### 1. Поддержка Apple ProMotion (120 FPS)
* На смартфонах iPhone 13 Pro–16 Pro установлены дисплеи ProMotion с адаптивной частотой до 120 Гц.
* Для того чтобы Compose Multiplatform отрисовывал анимации с честными 120 FPS, в `Info.plist` iOS-приложения обязательно включается ключ:
  ```xml
  <key>CADisableMinimumFrameDurationOnPhone</key>
  <true/>
  ```
  Без этого ключа iOS принудительно ограничивает частоту CADisplayLink до 60 Гц.

### 2. Свайп «Назад» (iOS Interactive Back Gesture)
* На Android пользователь использует жест от края экрана или системную кнопку «Назад» (`PredictiveBackHandler`).
* На iOS пользователи ожидают плавный интерактивный жест свайпа от левого края с частичным просмотром предыдущего экрана. В CMP навигации это реализуется через нативную интеграцию с `UINavigationController` или предиктивные переходы в Compose Navigation.

### 3. Клавиатура и вырезы экрана (Keyboard & Window Insets)
* В Android клавиатура сдвигает контент через `WindowInsets.ime`.
* В iOS Skiko транслирует события появления клавиатуры в `WindowInsets.ime.asPaddingValues()`, что позволяет единообразно верстать экраны авторизации и заметок.

### Схема 13: Обработка системных жестов и ввода

```
+------------------------------------------------------------------------------------+
|                                  Screen Layout Tree                                |
+------------------------------------------------------------------------------------+
|  [Modifier.windowInsetsPadding(WindowInsets.statusBars)] -> Top Safe Area          |
|  [Modifier.windowInsetsPadding(WindowInsets.ime)]        -> Клавиатура (iOS/Android)|
|  [Modifier.windowInsetsPadding(WindowInsets.navigationBars)] -> Home Indicator (iOS)
+------------------------------------------------------------------------------------+
                                          ^
                                          |
                        +-----------------+-----------------+
                        |                                   |
                        V                                   V
        +-------------------------------+   +-------------------------------+
        |            Android            |   |              iOS              |
        |  WindowInsetsCompat           |   |  UIKeyboardNotification /     |
        |  EdgeToEdge.enable()          |   |  UIEdgeInsets (Safe Area)     |
        +-------------------------------+   +-------------------------------+
```

---

## 13. Темы оформления и специфика OLED

### Дизайн-система `MpeixTheme`
Дизайн-система проекта включает три режима:
1. `Light` (светлая тема);
2. `Dark` (классическая серая темная тема);
3. `OLED / Pure Black` (абсолютно черный цвет `#000000` фона для экономии энергии на Super AMOLED / OLED экранах).

### Схема 14: Разрешение цветовой палитры в MpeixTheme

```
+------------------------------------------------------------------------------------+
|                             AppSettings.appTheme                                   |
|                     (Light | Dark | OLED | SystemDefault)                          |
+------------------------------------------------------------------------------------+
                                          |
                                          V
+------------------------------------------------------------------------------------+
|                              MpeixPalette Resolution                               |
+------------------------------------------------------------------------------------+
|  * Light   -> LightMpeixPalette (Background: #FFFFFF, Surface: #F5F5F5)            |
|  * Dark    -> DarkMpeixPalette  (Background: #1E1E1E, Surface: #2C2C2C)            |
|  * OLED    -> OledMpeixPalette  (Background: #000000, Surface: #121212)            |
+------------------------------------------------------------------------------------+
                                          |
                                          V
+------------------------------------------------------------------------------------+
|                       CompositionLocalProvider(LocalMpeixPalette)                  |
|                 (Доступен во всех Composable экранах обеих платформ)               |
+------------------------------------------------------------------------------------+
```

### Нюанс iOS Dynamic System Theme:
На iOS переключение светлой/темной темы в шторке управления (Control Center) автоматически отслеживается `traitCollectionDidChange` контроллера `ComposeUIViewController` и транслируется в Compose `isSystemInDarkTheme()`.

---

## 14. Картография: решение проблемы OSMDroid

### Проблема:
Модуль `feature_map_impl` использует библиотеку `org.osmdroid:osmdroid-android`. Это исключительно Android-библиотека, основанная на `android.view.View`, Android Canvas и SQLite файлах кэша тайлов. На iOS она не компилируется.

### Варианты решения для мультиплатформы:

```
+------------------------------------------------------------------------------------+
|                        АРХИТЕКТУРА ИЗОЛЯЦИИ КАРТЫ КАМПУСА                          |
+------------------------------------------------------------------------------------+
|                                  :feature_map_api                                  |
|         CampusMapLauncher / CampusMapState (Точки корпусов, маршруты, пины)        |
+------------------------------------------------------------------------------------+
                                          |
                                          V
+------------------------------------------------------------------------------------+
|                             CampusMapScreen (Composable)                           |
+------------------------------------------------------------------------------------+
                        |                                   |
                        V                                   V
        +-------------------------------+   +-------------------------------+
        |    Android (androidMain)      |   |        iOS (iosMain)          |
        |                               |   |                               |
        |  [AndroidView { OSMDroid }]   |   |  ВАРИАНТ 1:                   |
        |  Существующая реализация      |   |  UIKitView { MKMapView }      |
        |  без изменений                |   |  (Apple MapKit с кастомными   |
        |                               |   |  тайлами OpenStreetMap)       |
        |                               |   |                               |
        |                               |   |  ВАРИАНТ 2 (Рекомендуемый):   |
        |                               |   |  Compose WebView + Leaflet    |
        |                               |   |  (100% одинаковый вид карты)  |
        +-------------------------------+   +-------------------------------+
```

**Рекомендация:**
Для быстрого старта на iOS обернуть легковесный локальный Leaflet.js с тайлами OpenStreetMap через `UIKitView` / `WKWebView`. Это гарантирует абсолютную визуальную идентичность карты Лефортово на обеих ОС без привязки к Google Play Services или платным ключам.

---

## 15. Влияние на существующую платформу Android (Нюансы и риски)

Переход на Compose Multiplatform неизбежно затронет существующий Android-клиент. Важно зафиксировать все риски:

### Схема 15: Модель перехода и зона стабильности Android

```
+------------------------------------------------------------------------------------+
|                              ЗОНА СТАБИЛЬНОСТИ ANDROID                             |
+------------------------------------------------------------------------------------+
|  [НЕ МЕНЯЕТСЯ] -> Room Schema (таблицы расписания и заметок сохраняют структуру)   |
|  [НЕ МЕНЯЕТСЯ] -> Koin DI синтаксис и регистрация зависимостей                     |
|  [НЕ МЕНЯЕТСЯ] -> Elmslie MVI стейт-машины и Reducer логика                        |
|  [НЕ МЕНЯЕТСЯ] -> Android Keystore / Сборка релизного APK/AAB                      |
+------------------------------------------------------------------------------------+
                                          |
                                          V
+------------------------------------------------------------------------------------+
|                          ЗОНА РИСКА И ПРОВЕРКИ ANDROID                             |
+------------------------------------------------------------------------------------+
|  1. Удаление Fragments -> изменение жизненного цикла экранов (ViewModel / Store)  |
|  2. Замена Retrofit -> Ktor: строгая валидация заголовков и кук БАРС               |
|  3. Разделение ProGuard / R8 правил: Android R8 vs Kotlin/Native LLVM Strip        |
|  4. Размер APK: замена OkHttp на Ktor практически не изменит размер                |
+------------------------------------------------------------------------------------+
```

### Детальные нюансы Android:
1. **Жизненный цикл экранов:** Во фрагментах существовал жизненный цикл `onDestroyView` vs `onDestroy`. В чистом Compose жизненный цикл управляется `DisposableEffect` и `LifecycleEffect`. Необходимо проверить, чтобы очистка ресурсов (например, плеера или подписок на геопозицию) происходила вовремя.
2. **Desugaring:** Проект использует `coreLibraryDesugaring` для Java 8 Time API (`java.time.LocalDate`) на старых Android (Android 8.0, API 26). В KMP следует переходить на `kotlinx-datetime`, который работает на всех версиях Android и на iOS без desugaring.
3. **Сохранение совместимости с Android 8.0 (API 26):** Переход на Compose Multiplatform не увеличивает минимальный SDK Android. Он остается `minSdk = 26`.

---

## 16. Плюсы и минусы перехода на Compose Multiplatform

### Плюсы (Преимущества):
1. **Единая кодовая база:** Один разработчик может поддерживать обе платформы. Фичи (например, изменения в алгоритмах семестрового рейтинга БАРС) пишутся один раз.
2. **Идентичный UI:** Дизайн-система `MpeixTheme`, цвета, шрифты, отступы, анимации чипов расписания выглядят абсолютно идентично на экранах Samsung, Pixel и iPhone.
3. **Безопасность типов:** Единые DTO-модели сериализации исключают расхождения в парсинге JSON между Android и iOS.
4. **Легковесность:** В отличие от Flutter, KMP компилируется в нативный машинный код под iOS (Objective-C/Swift совместимый Mach-O бинарник) и не тянет за собой чужеродный Dart Runtime.

### Минусы (Сложности и компромиссы):
1. **Размер приложения для iOS:** Исполняемый файл iOS будет на 15–25 МБ больше из-за включения движка рендеринга Skiko (Skia).
2. **Виджеты требуют Swift:** Для домашнего экрана iOS всё равно придется написать небольшой объем Swift-кода (WidgetKit).
3. **Время первой компиляции Kotlin/Native:** Компиляция iOS-таргетов через LLVM занимает значительно больше времени, чем сборка Android DEX.
4. **Интеграция сторонних нативных библиотек:** Если понадобится специфическая нативная iOS-библиотека, потребуется писать `cinterop` def-файлы или обертку на Swift.

---

## 17. Сборочный процесс, тулинг и стратегия валидации без Mac

### Схема 16: Точка интеграции iOS Native Shell (iosApp)

```
+------------------------------------------------------------------------------------+
|                         Xcode Project (iosApp.xcodeproj)                           |
+------------------------------------------------------------------------------------+
|                                                                                    |
|  // iOS App Entry Point (SwiftUI)                                                  |
|  @main                                                                             |
|  struct MpeiApp: App {                                                             |
|      var body: some Scene {                                                        |
|          WindowGroup {                                                             |
|              ContentView() // Обертка над UIViewController                         |
|          }                                                                         |
|      }                                                                             |
|  }                                                                                 |
|                                                                                    |
|  // ContentView.swift (Bridge)                                                     |
|  struct ContentView: UIViewControllerRepresentable {                               |
|      func makeUIViewController(context: Context) -> UIViewController {             |
|          return MainViewControllerKt.MainViewController() // Вызов из Kotlin/KMP   |
|      }                                                                             |
|  }                                                                                 |
|                                                                                    |
+------------------------------------------------------------------------------------+
                                          |
                                          V линковка
+------------------------------------------------------------------------------------+
|                  shared.xcframework (Сгенерирован Gradle KMP)                      |
|              (Содержит скомпилированный Compose UI + Skiko + Ktor)                 |
+------------------------------------------------------------------------------------+
```

### Как валидировать сборку iOS прямо сейчас на Windows (до приезда Mac):
Так как локально на Windows компилятор Kotlin/Native не может линковать финальные фреймворки iOS из-за отсутствия Apple SDK, используется **CI/CD в GitHub Actions**.

### Схема 17: Пайплайн валидации на Windows через GitHub Actions

```
+------------------------------------------------------------------------------------+
|                          РАБОЧАЯ СТАНЦИЯ РАЗРАБОТЧИКА (WINDOWS)                     |
+------------------------------------------------------------------------------------+
|  1. Разработка общего кода (commonMain): UseCases, Ktor, Room, Compose UI          |
|  2. Локальная проверка на Android: ./gradlew assembleDebug testDebugUnitTest       |
|  3. git commit & push в ветку `feature/kmp-migration`                             |
+------------------------------------------------------------------------------------+
                                          |
                                          V push
+------------------------------------------------------------------------------------+
|                        GITHUB ACTIONS (macOS-14 / M1 Runner)                       |
+------------------------------------------------------------------------------------+
|  * Автоматический запуск на бесплатном macOS-раннере                               |
|  * Выполнение команды:                                                             |
|    ./gradlew :modules:shared:compileKotlinIosSimulatorArm64                        |
|    ./gradlew :modules:shared:linkPodDebugFrameworkIosSimulatorArm64                |
|  * Результат: зеленый статус билда подтверждает, что код 100% компилируется под iOS!
+------------------------------------------------------------------------------------+
                                          |
                                          V через 1.5 недели
+------------------------------------------------------------------------------------+
|                           РЕАЛЬНЫЙ MACBOOK (ЧЕРЕЗ 1.5 НЕДЕЛИ)                      |
+------------------------------------------------------------------------------------+
|  * Клонирование готовой, уже скомпилированной ветки                                |
|  * Открытие iosApp в Xcode                                                         |
|  * Запуск на симуляторе iPhone 15/16 Pro нажатием одной кнопки Cmd+R               |
+------------------------------------------------------------------------------------+
```

---

## 18. Пошаговая дорожная карта (Roadmap)

### Этап 1: Подготовка на Windows (Текущая неделя)
* [ ] **Шаг 1.1:** Создать KMP convention plugins в `build-logic` (`kotlin("multiplatform")`, `compose.multiplatform`).
* [ ] **Шаг 1.2:** Изолировать навигацию: перевести модуль `feature_schedule_impl` с `Fragment` на чистую `@Composable ScheduleScreen` функцию.
* [ ] **Шаг 1.3:** Переписать `modules/lib/network` на **Ktor 3 Client**, проверив корректность работы расписания и авторизации БАРС на Android.
* [ ] **Шаг 1.4:** Подготовить `modules/lib/app_database`: перевести конфигурацию Room на мультиплатформенный драйвер `sqlite-bundled`.
* [ ] **Шаг 1.5:** Заменить `SharedPreferences` на `multiplatform-settings`.
* [ ] **Шаг 1.6:** Настроить GitHub Actions workflow на `macos-14` раннере для сборки `iosSimulatorArm64`.

### Этап 2: Финализация KMP и Umbrella Framework (Конец недели 1)
* [ ] **Шаг 2.1:** Собрать зонтичный модуль `:modules:shared` с экспортом `MainViewController`.
* [ ] **Шаг 2.2:** Сконвертировать строковые ресурсы `res_strings` в формат `composeResources`.
* [ ] **Шаг 2.3:** Добиться успешной компиляции всех модулей под `iosSimulatorArm64` в GitHub Actions.

### Этап 3: Тестирование и запуск на реальном MacBook (Неделя 2)
* [ ] **Шаг 3.1:** Развернуть проект в Xcode на MacBook.
* [ ] **Шаг 3.2:** Запустить `iosApp` на iOS Simulator (проверить ProMotion 120 FPS, скролл, темную/светлую тему).
* [ ] **Шаг 3.3:** Настроить Safe Area отступы для Dynamic Island и наэкранной клавиатуры.
* [ ] **Шаг 3.4:** Реализовать компактный нативный виджет расписания на Swift (`WidgetKit`) через общий JSON из App Group.
* [ ] **Шаг 3.5:** Собрать релизную версию на физический iPhone через Apple Free Personal Team / Apple Developer Account.

---

> **Итог анализа:** Переход на Compose Multiplatform для «МЭИ.Хаб» технически полностью обоснован, архитектурно реализуем и позволяет переиспользовать до 90% кода проекта. Подготовительные работы можно проводить на Windows, валидируя сборку через облачный macOS CI.
