<div align="center">

<img src=".github/media/logo.png" width="120" height="120" alt="Логотип МЭИ.Хаб" />

# «МЭИ.Хаб» · MPEI.Hub

### Умная студенческая экосистема на дофамине ⚡

**Расписание офлайн · БАРС с 2FA · Офлайн-карта Лефортово · Виджет Glance · OLED Dark**

*Всё для комфортной учёбы в НИУ «МЭИ» — от первой пары до защиты диплома.*<br>
*Тактильный Jetpack Compose, zero-tracker приватность, чистый офлайн и независимость от Google Play Services.*

<br/>

[![CI](https://img.shields.io/github/actions/workflow/status/ghostoftheeleven/mpei-next/android.yml?branch=main&label=CI&logo=github&color=2ea44f)](https://github.com/ghostoftheeleven/mpei-next/actions)
![Android](https://img.shields.io/badge/Android-8.0+_(API_26–36)-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2_·_KMP-7F52FF?logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Compose-Material_3-4285F4?logo=jetpackcompose&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-KMP_WIP-000000?logo=apple&logoColor=white)
<br/>
![Privacy](https://img.shields.io/badge/privacy-152--ФЗ_·_Zero_Trackers-6f42c1)
![Third-party SDK](https://img.shields.io/badge/third--party_SDK-0-008080)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](./LICENSE)
[![Release](https://img.shields.io/github/v/release/ghostoftheeleven/mpei-next?label=Release&color=orange)](https://github.com/ghostoftheeleven/mpei-next/releases)

<br/>

[📥 Скачать релизный APK](https://github.com/ghostoftheeleven/mpei-next/releases/latest) • [📖 Документация](docs/) • [🗺️ Карта кампуса](#-навигация-и-кампус-лефортово) • [🔐 БАРС и 2FA](#-личный-кабинет-барс) • [🛠️ Сборка](#-сборка-и-запуск-проекта)

</div>

---

> [!NOTE]
> **«МЭИ.Хаб»** — независимое открытое развитие легендарного приложения **MpeiX** (автор — Антон Коломейцев). Проект создан студентами для студентов и преподавателей НИУ «МЭИ», ориентирован на бескомпромиссную скорость, эстетику и полную приватность данных.

---

## 🚀 Возможности

### 📅 Расписание занятий без компромиссов
* **Прямая интеграция с API МЭИ:** расписание загружается напрямую с официального портала (`ts.mpei.ru/api`) без сторонних прокси-серверов.
* **Режим преподавателя:** интерактивный список студенческих групп на паре с мгновенным переходом в расписание группы в один клик.
* **Два режима отображения:** понедельный просмотр с вертикальной колонкой дней и панорамный обзор всей недели (*Weekly Timeline Grid*).
* **Умные фильтры и чипы:** мгновенный отбор по типам занятий (лекции, практики, лабы, курсовые) и быстрые чипы дисциплин текущей недели.
* **Offline-first архитектура:** все данные кэшируются в локальной базе Room и моментально доступны даже в аудиториях с глухим приёмом сети.
* **Фоновые уведомления и напоминания:** периодическая проверка изменений расписания через WorkManager с алертами о замене пар, сменах аудиторий, а также напоминания за 24 и 5 часов до лабораторных и практик.

### 🔐 Личный кабинет БАРС
* **Полноценная поддержка 2FA:** бесшовный вход через TOTP-аутентификаторы (Google Authenticator, Яндекс Ключ), мессенджер МАКС, Telegram, VK и E-mail.
* **Устойчивая сессия:** сохранение состояния авторизации с ограничением времени жизни сессии (TTL 5 минут) — форма входа не сбрасывается при переключении между приложениями.
* **Детализация успеваемости:** оценки, контрольные недели, сводный семестровый рейтинг и раскрывающиеся карточки контрольных мероприятий (КМ) по дисциплинам.

### 🗺️ Навигация и кампус Лефортово
* **100% офлайн-карта на OpenStreetMap:** корпуса, общежития, столовые, спортивный комплекс, поликлиника и кафедры на базе библиотеки `osmdroid`.
* **Zero API Keys & Zero Play Services:** карта работает без привязки к сервисам Google и не требует сторонних ключей API.
* **Изолированный жизненный цикл:** разделение доменных маркеров и слоя отображения с гарантированным освобождением памяти при сворачивании экрана.
* **Схема и гибрид:** быстрое переключение слоёв карты под яркое солнце или работу в сумерках.

### 🎨 Дизайн, адаптивность и системная интеграция
* **Jetpack Compose + Material 3:** современный тактильный UI с кастомным набором компонентов UI Kit.
* **Тройная тема оформления:** выверенная Светлая, мягкая Тёмная и глубокая **OLED True Black** для максимальной экономии батареи на AMOLED-экранах.
* **Виджет рабочего стола (Jetpack Glance):** наглядное расписание на домашнем экране с переключателем дней и автообновлением при изменении расписания.
* **Премиальная заставка:** векторная анимация SplashScreen API (Android 12+) и фирменная Compose-анимация светового кольца эмблемы МЭИ.Хаб.
* **Складные устройства и планшеты:** плавная адаптация к Foldables (Samsung Galaxy Z Fold/Flip, Google Pixel Fold) без перезапуска Activity, оптимизированная разметка `layout-sw600dp`, Split-Screen и Samsung DeX.
* **Двуязычный интерфейс:** полная локализация на русский и английский языки с возможностью мгновенного переключения в настройках.

### 🛡️ Параноидальная приватность (Zero-Tracker Policy)
* **0 сторонних SDK и трекеров:** в приложении нет Яндекс.Метрики, Firebase Crashlytics/Analytics, рекламных библиотек или сторонних шпионских модулей.
* **Соответствие 152-ФЗ:** все токены, сессионные куки и заметки хранятся локально в защищённом хранилище устройства.
* **Работа на свободных прошивках:** 100% работоспособность на GrapheneOS, CalyxOS, LineageOS, AOSP и устройствах Huawei (HarmonyOS/EMUI).

---

## 🍏 Мультиплатформенность: iOS & Kotlin Multiplatform

Проект активно движется к полной мультиплатформенности:
* **lib_bars_parser & lib_bars_client:** парсер и сетевой клиент личного кабинета БАРС переведены на чистый Kotlin Multiplatform (KMP).
* **iOS App Shell:** в репозитории подготовлен каркас приложения для iOS, собираемый через GitHub Actions с выпуском `.ipa`.
* Подробности архитектуры и пошаговый план миграции:
  * [План миграции на Compose Multiplatform (Android + iOS)](./docs/COMPOSE_MULTIPLATFORM_MIGRATION.md)
  * [Инструкция по установке iOS .ipa на iPhone из Windows](./docs/IOS.md)

---

## 🏗️ Архитектура и стек технологий

Проект построен по принципам многомодульной **Clean Architecture** с однонаправленным потоком данных:

| Слой / Подсистема | Технологический стек |
|---|---|
| **Язык и платформа** | Kotlin 2.2.10 / 2.2.21, Java 17/21, AGP 8.12.3, Gradle Composite Builds (`build-logic`) |
| **Пользовательский интерфейс** | Jetpack Compose, Material 3, кастомная дизайн-система `MpeixTheme` + UI Kit |
| **Архитектура UI (MVI)** | TEA / ELM на базе Vivid Money Elmslie + `lib_elm_compose` |
| **Виджеты домашнего экрана** | Jetpack Glance (`glance-appwidget`, `glance-material3`) |
| **Картография и геоданные** | OpenStreetMap (`osmdroid 6.1.20`) |
| **Сетевой стек** | Retrofit 3, OkHttp 5, Ktor Client (KMP), Kotlinx Serialization, TLS 1.2/1.3 |
| **Локальные данные и кэш** | Room Database 2.8 (KSP), MMKV, SharedPreferences |
| **Внедрение зависимостей** | Koin 4.1 (`koin-android`, `koin-compose`) |
| **Фоновые задачи** | AndroidX WorkManager 2.10 |
| **Тестирование** | JUnit 5 Platform, Kotest (StringSpec, BehaviorSpec), UI Automator Smoke Tests |

Подробный разбор архитектурных слоёв, правил связывания модулей `api` / `impl` и инвариантов доступен в [ARCHITECTURE.md](./ARCHITECTURE.md).

---

## 🛠️ Сборка и запуск проекта

Проект не требует платных API-ключей и собирается локально стандартными средствами Gradle.

### Необходимое окружение
- **JDK 17** или **JDK 21** (рекомендуется [Eclipse Adoptium Temurin 21](https://adoptium.net/))
- **Android SDK** (API 34/35/36)
- **Android Studio Ladybug / Meerkat** или актуальная версия

### Сборка из терминала

```bash
# Клонирование репозитория
git clone https://github.com/ghostoftheeleven/mpei-next.git
cd mpei-next

# Сборка debug APK
./gradlew assembleDebug

# Запуск юнит-тестов (расписание, 2FA БАРС, мапперы)
./gradlew :feature_schedule_impl:testDebugUnitTest :feature_bars_impl:testDebugUnitTest

# Сборка release APK
./gradlew assembleRelease --no-configuration-cache
```

> [!TIP]
> **Цифровая подпись релизов:**
> Ключ подписи в публичном репозитории **не хранится**. Для подписанной релизной сборки задайте переменные окружения или параметры в `~/.gradle/gradle.properties`:
> `SIGNING_KEYSTORE_PATH`, `SIGNING_STORE_PASSWORD`, `SIGNING_KEY_ALIAS`, `SIGNING_KEY_PASSWORD`.
> Подробнее о публикации релизов, формате AAB/APK и секретах CI — в [docs/RELEASE.md](./docs/RELEASE.md).

### 💡 Рекомендации для рабочих станций с 8 ГБ ОЗУ (Windows)
1. **Лимиты памяти демона:** в файле `gradle.properties` установлены безопасные лимиты кучи JVM (`-Xmx2048m`), предотвращающие OutOfMemoryError при одновременной работе Android Studio и Gradle-демона.
2. **Пути с кириллицей и пробелами:** если каталог проекта расположен в пути с кириллицей (например, `C:\Users\<Имя>\Documents\Проекты`), рекомендуется создать символическое NTFS-соединение (junction) в короткий путь:
   ```cmd
   mklink /J C:\mpei-next "C:\Users\<Username>\Documents\Проекты\mpei-next"
   ```
   и открывать проект из каталога `C:\mpei-next`.

---

## 📂 Структура репозитория

```text
mpei-next/
├── build-logic/                # Конвенции сборки и Gradle-плагины проекта
├── gradle/                     # Version Catalog (libs.versions.toml) и Gradle Wrapper
├── docs/                       # Релизы (RELEASE.md), дорожная карта iOS (IOS.md), архив
├── third_party/                # Сторонние артефакты (локальный Maven, вендоренный lib_bars)
├── statics/                    # Данные маркеров карты и конфиги БАРС, обновляемые через GitHub
├── modules/
│   ├── app/                    # Точка входа, Application, навигационный граф, SplashScreen
│   ├── feature/
│   │   ├── schedule/           # Модуль расписания (ts.mpei.ru, Compose UI, Glance, WorkManager)
│   │   ├── bars/               # Интеграция с ЛК БАРС (TOTP, MAX, Telegram, VK, Email 2FA)
│   │   ├── map/                # Офлайн-карта кампуса Лефортово на OpenStreetMap
│   │   ├── dashboard/          # Главный экран и виджеты студента
│   │   ├── notes/              # Личные заметки к занятиям
│   │   ├── search/             # Глобальный поиск по аудиториям, группам и преподавателям
│   │   ├── app/settings/       # Настройки (темы, язык, уведомления)
│   │   └── onboarding/         # Первоначальная настройка и выбор расписания
│   ├── ui/
│   │   ├── theme/              # Дизайн-токены, типографика, цветовые палитры (OLED Black)
│   │   ├── kit/                # UI Kit (списки, кнопки, диалоги, переключатели)
│   │   ├── icons/              # Векторная проектная графика MpeixIcons
│   │   └── splash/             # Премиальная Compose-анимация заставки
│   └── lib/                    # Сетевой стек, Room DB, ELM/MVI мост, утилиты
└── README.md
```

---

## 👥 Авторы и разработчики

* **Расул Рустамов** ([@ghostoftheeleven](https://github.com/ghostoftheeleven)) — Архитектура, разработка «МЭИ.Хаб», студент МЭИ
* **Johnny Wickky** ([@Johnny-Wickky](https://github.com/Johnny-Wickky)) — Соавтор, студент МЭИ, информационная безопасность
* **Антон Коломейцев** ([@tonykolomeytsev](https://github.com/tonykolomeytsev)) — Создатель оригинального приложения MpeiX

---

## 🤝 Участие в разработке

Мы открыты для сообщества студентов и разработчиков! Ваши Pull Request'ы и идеи приветствуются:
1. Сделайте **Fork** репозитория.
2. Создайте функциональную ветку под вашу задачу (`git checkout -b feature/awesome-feature`).
3. Закоммитьте изменения в конвенции Conventional Commits (`git commit -m 'feat: add awesome feature'`).
4. Отправьте ветку в ваш форк (`git push origin feature/awesome-feature`).
5. Откройте **Pull Request** в ветку `main`.

---

## 📄 Лицензия

Проект распространяется под свободной лицензией **[MIT](./LICENSE)**.

*Дисклеймер: «МЭИ.Хаб» является независимой студенческой разработкой и не является официальным приложением ректората НИУ «МЭИ». Все товарные знаки и названия принадлежат их законным правообладателям.*
