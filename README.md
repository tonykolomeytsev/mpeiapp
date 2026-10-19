# «МЭИ.Хаб» / MPEI.Hub (ранее MPEI-NEXT) — Альтернатива для МЭИ

**Когда мы форкнули этот проект, только три человека на планете знали как он работает. Теперь ни одна живая душа этого не знает**

## Форк https://github.com/tonykolomeytsev/mpeiapp

### Расписание пар, заметки, личный кабинет БАРС, офлайн-карта кампуса и домашний виджет для студентов и преподавателей **НИУ «МЭИ»**. Самое быстрое, современное и независимое студенческое приложение.

## ✨ Возможности

+ 📅 **Расписание без посредников**: Прямая интеграция с официальным API расписания МЭИ (`ts.mpei.ru/api`) для всех студенческих групп и преподавателей.
+ 🧩 **Сетка расписания «Слева дни, справа пары»**:
  - Вертикальная колонка дней слева с интерактивным выбором, индикацией дат и счётчиками пар.
  - Режим «По дням» и режим «Вся неделя» (Weekly Timeline Grid) для полного обзора пар.
  - Поиск и фильтрация по названию предмета, типам занятий (Лекции, Практики, Лабораторные, Курсовые) и быстрые чипы дисциплин текущей недели.
+ ⏱️ **Умный счётчик пар на главном экране**: Автоматическое определение окончания занятий, безопасная обработка выходных и обратный отсчёт дней и часов до ближайшей пары.
+ 📱 **Домашний виджет (Jetpack Glance)**:
  - Расписание на сегодня прямо на рабочем столе Android со временем, аудиториями и типами пар.
  - Реактивное автообновление виджета при загрузке расписания, смене выбранной группы и смене недели (`ScheduleGlanceWidgetUpdater`).
+ 🗺️ **Свободная карта без утечек памяти (OpenStreetMap)**:
  - Корпуса, общежития, столовые, спорткомплекс и кафедры на базе `osmdroid` — работает без Google Play Services и без API-ключей.
  - Строгое разделение слоёв: MVI State содержит только чистые доменные сущности `MapMarker`, а жизненный цикл `MapView` и оверлеев привязан к `MapFragment`, исключая утечки `Activity` и контекста.
+ 🎓 **Личный кабинет БАРС с надёжной 2FA**:
  - Полная поддержка двухфакторной аутентификации БАРС МЭИ.
  - Автоматическое распознавание и раздельная маршрутизация каналов доставки: приложения-аутентификаторы (TOTP: Google Authenticator, Яндекс Ключ), мессенджер МАКС, Telegram (`tid=1`), VK, E-mail (`tid=5`).
  - Мгновенный ввод 6-значного кода при TOTP без лишних сетевых вызовов отправки кода.
  - Сохранение незавершённой сессии авторизации в `SharedPreferences`, гарантирующее выживание 2FA-состояния при переключении студента между приложениями.
+ 🔔 **Детектор изменений расписания с поддержкой подгрупп**:
  - Слотовая группировка по временным ячейкам пар с многоступенчатым сопоставлением дисциплин и подгрупп, исключающая ложные уведомления `REPLACED` для параллельных занятий подгрупп.
+ 📝 **Интерактивные заметки к парам в Compose**:
  - Прямой переход к экрану заметок по тапу на карточку пары в Compose-расписании с реактивной обратной связью и обновлением статуса заметок.
+ 🎨 **Jetpack Compose + MpeiX UI Kit**: Современный декларативный интерфейс (экраны расписания, настроек) с поддержкой светлой, тёмной и OLED-тем.
+ 🚀 **Плавный Splash Screen (Android 12+)**:
  - Синхронизированный хронометраж векторной анимации (1000 мс) с `setKeepOnScreenCondition`.
  - Мягкий выходной переход (fade-out + scale-out) через `setOnExitAnimationListener`.
+ 🔒 **Сетевая безопасность**:
  - Режим подробного логирования тела запросов/ответов (`HttpLoggingInterceptor.Level.BODY`) изолирован исключительно для отладочных окружений (`STAGING`, `MOCK`), а для `PROD` принудительно отключён (`Level.NONE`), предотвращая утечку сессионных cookie и персональных данных.
+ 💾 **Offline-first**: Расписание, заметки и геометки кэшируются на устройстве в базе данных Room. Приложение работает даже при нестабильной связи в подвальных аудиториях.
+ ⚡ **Блиц-доступ**: Быстрое переключение между избранными группами в один тап.
+ 🌐 **Двуязычность**: Полная поддержка русского и английского языков.

---

## 📱 Совместимость с устройствами, ОС и экранами

Приложение оптимизировано для работы на максимально широком спектре оборудования и прошивок:

### 1. Версии Android (от Android 8.0 Oreo до Android 15 / 16 Preview)
- **Минимальная версия:** **Android 8.0 (API 26)** — покрывает 97%+ активных устройств в мире. Благодаря интеграции `coreLibraryDesugaring` (`desugar_jdk_libs:2.1.5`) современные Java 8+ API времени (`java.time`) и многопоточности работают стабильно без сбоев на старых версиях.
- **Целевая версия:** **Android 16 / 15 (Target SDK 36, Compile SDK 36)** с полной поддержкой современных требований Google:
  - Динамическое разрешение на уведомления `POST_NOTIFICATIONS` (Android 13+).
  - Безопасное планирование периодических задач через `WorkManager` (вместо устаревших `AlarmManager`, подверженных Doze mode).
  - Предиктивный жест «Назад» (`enableOnBackInvokedCallback="true"`).
  - Нативная интеграция Android 12+ SplashScreen API.

### 2. Складные смартфоны (Foldables & Flip)
- **Устройства:** Samsung Galaxy Z Fold (1–6), Samsung Galaxy Z Flip (1–6), Tecno Phantom V Flip / V Fold, Google Pixel Fold, Xiaomi MIX Fold, HONOR Magic V.
- **Поддержка непрерывности (Continuity):**
  - Флаг `android.supports_size_changes="true"` обеспечивает бесшовное переключение интерфейса при складывании/раскладывании смартфона без потери контекста.
  - Конфигурации `orientation|screenSize|smallestScreenSize|screenLayout|keyboardHidden` перехватываются `MainActivity` без деструктивного пересоздания Activity.
  - Поддержка нестандартных соотношений сторон экрана: от ультраузких внешних дисплеев (~24.5:9) до внутренних дисплеев (~4:3 и ~1:1).

### 3. Планшеты и большие экраны (Large Screens & DeX)
- **Адаптивные лейауты:**
  - Специализированная разметка `layout-sw600dp` для планшетов и настольных режимов (Samsung DeX, Motorola Ready For).
  - Нижняя панель навигации центрируется с эргономичным ограничением максимальной ширины (`maxWidth="640dp"`), предотвращая неудобное растягивание иконок на экранах от 10 до 14 дюймов.
  - Модули `schedule`, `bars`, `dashboard`, `notes`, `map`, `search` и `onboarding` оснащены оптимизированными tablet-лейаутами.
  - Полноценная поддержка Split-Screen и свободного плавающего окна благодаря `android:resizeableActivity="true"`.

### 4. Сторонние прошивки и дегуглизированные системы
- **Open-source & независимость от сервисов Google:**
  - Карта кампуса построена на **OpenStreetMap (`osmdroid`)**, работает оффлайн и полностью независима от Google Play Services и проприетарных API-ключей.
  - Приложение стабильно функционирует на независимых прошивках: **GrapheneOS, CalyxOS, LineageOS, AOSP**, а также на устройствах **Huawei (EMUI / HarmonyOS)** без Google Mobile Services.
  - Все сетевые и GPS-модули снабжены защитными блоками `uses-feature android:required="false"`, позволяя установку на планшеты без модуля сотовой связи и на десктопные среды.
- **Кастомные оболочки (HyperOS/MIUI, One UI, ColorOS, OriginOS):**
  - Сессии 2FA БАРС сохраняются в защищенном локальном хранилище, предотвращая сброс авторизации при агрессивной выгрузке фоновых процессов диспетчерами памяти вендоров.

---

## 🛠️ Стек технологий и архитектура

Проект построен на принципах **Clean Architecture** и многомодульности (`modules/feature/*`, `modules/ui/*`, `modules/lib/*`):

| Слой / Компонент | Используемые технологии |
|---|---|
| **Язык & Сборка** | Kotlin 2.2.10, Java 17/21, AGP 8.12.3, Gradle Composite Builds (`build-logic`) |
| **UI** | Jetpack Compose, Compose Material 3, собственная дизайн-система `MpeixTheme` + UI Kit (`ui_kit_lists`, `ui_kit_switch`, `ui_kit_topappbar`, `ui_kit_dialogs`, `ui_kit_buttons`) |
| **Виджеты** | Jetpack Glance (`glance-appwidget`, `glance-material3`) |
| **MVI / Presentation** | TEA/ELM на базе [Vivid Money **Elmslie**](https://github.com/vivid-money/elmslie) + `lib_elm_compose` |
| **Карты** | **OpenStreetMap (osmdroid 6.1.20)** — независим от Google Maps SDK, очищен от утечек `View` |
| **Сеть** | **Retrofit 3**, OkHttp 5, Kotlinx Serialization, прямой клиент к `ts.mpei.ru/api`, защищенное логирование |
| **База данных & Кэш** | **Room 2.8** (KSP), MMKV, SharedPreferences (с персистентностью 2FA) |
| **Dependency Injection** | **Koin 4.1** (`koin-android`, `koin-compose`) |
| **Тестирование** | JUnit 5 Platform, **Kotest** (StringSpec, BehaviorSpec) |

Подробное описание слоев и взаимодействия модулей см. в [ARCHITECTURE.md](./ARCHITECTURE.md).

---

## 🚀 Быстрый старт (Сборка проекта)

Больше **никаких сложных настроек, скрытых секретов и блокирующих API-ключей**. Проект подготовлен для чистой локальной сборки «из коробки»:

### Требования
- **JDK 17** или **JDK 21** (рекомендуется Eclipse Adoptium Temurin 21)
- **Android Studio** (Ladybug / Iguana / Hedgehog или новее)
- Android SDK (API 34/35/36)

### Сборка из командной строки
```bash
# Клонирование репозитория
git clone https://github.com/ghostoftheeleven/mpei-next.git
cd mpei-next

# Сборка debug APK
./gradlew assembleDebug

# Запуск юнит-тестов (расписание, БАРС 2FA, мапперы)
./gradlew :feature_schedule_impl:testDebugUnitTest :feature_bars_impl:testDebugUnitTest
```

### ⚙️ Специфика для Windows и ПК с 8 ГБ ОЗУ

1. **Параметры памяти JVM:**
   В `gradle.properties` уже настроены безопасные лимиты, не приводящие к падению Gradle/Kotlin демонов по OutOfMemoryError:
   ```properties
   org.gradle.jvmargs=-Xms512m -Xmx2048m -XX:MaxMetaspaceSize=768m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8 -Dkotlin.daemon.jvm.options=-Xms512m,-Xmx1536m
   ```
2. **Пути с кириллицей или пробелами на Windows:**
   Если проект клонирован в директорию с не-ASCII символами (например, `C:\Users\...\Документы\Проекты\mpei-next`), во избежание проблем с `Class-Path` в pathing jar раннера Gradle рекомендуется создать NTFS-симлинк (junction) в чистый ASCII-путь:
   ```cmd
   mklink /J C:\Users\<Username>\mpei-next "C:\Users\<Username>\Documents\Проекты\mpei-next"
   ```
   И открывать проект / запускать сборку из `C:\Users\<Username>\mpei-next`.

> 💡 **Секреты и ключи не требуются:**
> - Для Google Services в репозиторий уже включены безопасные локальные конфигурации `google-services.json`.
> - Google Maps SDK полностью удален в пользу OpenStreetMap, поэтому `mpeiapp_google_maps_api_key` больше не нужен.
> - Если локальные ключи отсутствуют, плагин `mpeix.secrets` автоматически подставляет безопасные значения-заглушки без сбоя сборки.

---

## 📂 Структура проекта

```text
mpei-next/
├── build-logic/                # Конвенции сборки и кастомные Gradle-плагины (JUnitPlatform, Compose, AGP)
├── gradle/                     # Каталоги версий (libs.versions.toml)
├── modules/
│   ├── app/                    # Точка входа, Application, навигация, SplashScreen
│   ├── feature/
│   │   ├── schedule/           # Расписание (API, impl, Compose-экран, Glance-виджет, парсер ts.mpei.ru, detector)
│   │   ├── map/                # Карта кампуса на OpenStreetMap (чистый MVI, osmdroid)
│   │   ├── app/settings/       # Настройки приложения (Compose + UI Kit)
│   │   ├── bars/               # Интеграция с ЛК БАРС (TOTP, Telegram, VK, Email, persistent 2FA session)
│   │   ├── dashboard/          # Дашборд студента
│   │   └── notes/              # Личные заметки к парам
│   ├── ui/
│   │   ├── theme/              # Дизайн-токены, палитра MpeixTheme, типографика, OLED-тема
│   │   ├── kit/                # Компоненты UI Kit (списки, свитчи, топ-бары, диалоги, кнопки)
│   │   └── icons/              # Векторная иконографика
│   └── lib/                    # Сеть (безопасное логирование), навигация, утилиты, ELM Compose bridge
└── README.md
```

---

## 👥 Авторы и разработчики

- **Расул Рустамов** ([@ghostoftheeleven](https://github.com/ghostoftheeleven)) — Разработчик «МЭИ.Хаб», студент МЭИ
- **Johnny Wickky** ([@Johnny-Wickky](https://github.com/Johnny-Wickky)) — Соавтор, студент МЭИ, ИБ- и ИИ-энтузиаст
- **Tony Kolomeytsev** ([@tonykolomeytsev](https://github.com/tonykolomeytsev)) — Создатель оригинального MpeiX

---

## 🤝 Участие в разработке

Приветствуются любые предложения и Pull Request'ы:
1. Форкните репозиторий.
2. Создайте ветку под вашу фичу (`git checkout -b feature/amazing-feature`).
3. Закоммитьте изменения (`git commit -m 'Add amazing feature'`).
4. Запушьте ветку (`git push origin feature/amazing-feature`).
5. Откройте **Pull Request**.

---

## 📄 Лицензия

Проект распространяется под лицензией **MIT**. Подробности в файле [LICENSE](./LICENSE).
