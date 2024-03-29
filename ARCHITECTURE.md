# Архитектура

Этот документ описывает высокоуровневую архитектуру приложения MpeiX. Если вы хотите лучше понимать,
как устроен код MpeiX, вы пришли по адресу 😃

После прочтения у вас должно появиться понимание следующих вещей:

- где находятся определенные части кода и за что они отвечают,
- где размещать код для новых фичей,
- как устроено взаимодействие приложения с внешним миром.

## С высоты птичьего полета

![](https://github.com/tonykolomeytsev/mpeiapp/raw/master/.github/media/mpeix-birds-view.png)

Рассуждая о приложении на самом высоком уровне, можно сказать что оно просто предоставляет 
пользователям удобный способ просмотра данных, которые приходят с бекенда MpeiX.

Если рассуждать более конкретно, то приложение MpeiX предоставляет пользователям доступ к 
расписаниям учебных занятий, поиску по преподавателям, учебным группам и другим ресурсам 
университета путем обращения к бекенду MpeiX, Firebase и статическим файлам проекта на GitHub. 
Большая часть данных кэшируется на устройствах пользователей чтобы иметь к ним доступ в случае 
перебоев с интернетом. В приложении нет сложных алгоритмов обработки данных, основная работа с 
"предметной областью" и с данными происходит на бекенде.

Обращение к ресурсам университета происходит через бекенд по следующим причинам:

- чтобы обеспечить работу приложения даже тогда, когда сайт университета недоступен,
- чтобы сделать приложение независимым от API университетских ресурсов, в случае смены API обновить 
  бекенд всегда проще, чем выпустить обновление в Google Play.

Код приложения выстроен в соответствии с Clean Architecture, с небольшими упрощениями.

## Путеводитель по коду

Эта секция коротко описывает основные директории в структуре проекта.

### `build-logic`

Здесь находится вложенный проект с кастомными плагинами Gradle, созданными для удобства разработки
и избавления от boilerplate-кода в `build.gradle.kts` скриптах модулей приложения.

В проекте приложения используется композитный билд - сначала собираются плагины из `/build-logic`, 
потом они используются для сборки самого приложения. Подробнее про композитные билды:

- https://habr.com/ru/companies/badoo/articles/514094/

### `gradle`

В этой директории находится файл `libs.versions.toml` - каталог версий Gradle. 
В `libs.versions.toml` описаны все зависимости проекта. Подробнее про version catalog:

- https://developer.android.com/build/migrate-to-catalogs
- https://proandroiddev.com/gradle-version-catalogs-for-an-awesome-dependency-management-f2ba700ff894

Как и в большинстве проектов android, здесь лежит `wrapper/gradle-wrapper.properties`.

### `statics`

Здесь расположены ресурсы которые могут быть загружены приложением со странички GitHub.

### `modules/app`

Главный модуль приложения, как и в почти любом android проекте. Этот модуль зависит от всех более 
низкоуровневых модулей и является точкой входа в приложение. Здесь лежит код MainActivity и 
наследника Application.

Модуль `:app` зависит от всех других модулей.

### `modules/feature`

В этой директории находятся все `feature`-модули, разбитые на `*_api` и `*_impl` составляющие. 
Каждый `feature_*_api` модуль является интерфейсом, через который могут взаимодействовать между собой 
`feature_*_impl` модули. Каждый `feature_*_impl` модуль при помощи пакетов разделен на слои по
правилам Clean Arch. В `*_impl` модулях также находятся DI модули.

Если нужно создать новый экран, относящийся к какой-то существующей фиче, он создается в уже 
существующем `feature_*_impl`-модуле, рядом с другими экранами.

Если нужно создать новый экран для новой фичи, это значит нужно завести новые `feature_*_(api|impl)`-модули.

<p align="center">
  <img src="https://github.com/tonykolomeytsev/mpeiapp/raw/master/.github/media/mpeix-feature-api-impl-structure.png">
</p>

**Архитектурный инвариант:** `*_impl` модули НЕ МОГУТ зависеть от других `*_impl` модулей. Связь между
`*_impl` модулями реализуется через зависимость от интерфейсов `*_api` модулей и механизмы DI.

### `modules/lib`

В этой директории хранятся "библиотечные" `lib_*`-модули. В таких модулях находится обособленный функционал,
не являющийся частью бизнес-логики приложения, который можно переносить от проекта к проекту без значимых изменений.
Отдельный `lib_*` модуль создается для какого-либо функционала, если требуется переиспользовать его в других модулях.

**Архитектурный инвариант:** Библиотечные модули НЕ МОГУТ зависеть от `feature_*_(api|impl)` модулей.
Библиотечные модули могут иметь внешние зависимости, зависимости от `ext_*` модулей и других библиотечных модулей.

Примеры библиотечных модулей в проекте:
- `lib_elm` - обертки для использования ELM в presentation слое.
- `lib_feature_toggles` - функционал фиче-тогглов на базе Firebase Remote Config.
- `lib_persistent_cache` - библиотека с персистентным кэшом для использования в репозиториях.

### `modules/ext`

В этой директории хранятся `ext_*`-модули или т.н. "модули-расширения". В таких модулях находится 
функционал для расширения внешних библиотек или конструкций языка. Эти модули должны быть максимально легковесными.

**Архитектурный инвариант:** Модули-расширения НЕ МОГУТ зависеть ни от каких других модулей проекта,
их зависимости исключительно внешние.

Примеры модулей-расширений в проекте:
- `ext_kotlin` - расширения и недостающие функции для стандратной библиотеки Kotlin.
- `ext_shared_preferences` - расширения для удобной работы с SharedPreferences.
- `ext_koin` - дополнительные фичи для библиотеки Koin.

### `modules/res`

В этой директории хранятся модули, в которых находятся ресурсы типа иконок и строк. Они вынесены в отдельные модули,
потому что это позволяет удобнее использовать сторонние инструменты менеджмента ресурсов, плюс позволяет 
сократить время инкрементального билда.

**Архитектурный инвариант:** Ресурсные модули НЕ МОГУТ иметь никаких зависимостей. Также они НЕ МОГУТ
иметь никакого исходного кода.

## Вещи, которые стоит знать

### Контуры бекенда

Приложение поддерживает работу с `mock`, `staging` и `production` окружениями. 
Переключаться между ними можно из дебаг меню в статус баре.

### Вес приложения

В проекте используется минимальное количество зависимостей и библиотек. 
И в основном выбраны те библиотеки, которые добавляют меньше всего веса приложению. 

Меньший вес приложения, чем у конкурентов, при бОльшем количестве фичей - это фишка всего проекта.

### Скорость билда

Некоторые вещи в приложении сделаны определенным образом только потому, что это позволяет сократить
время билда. Так, например, были выпилены все зависимости, требующие использования kapt. 

На сегодняшний день все больше библиотек добавляют подержку KSP. Так что в будущем будет происходить возврат к тем библиотекам, которые его раньше не поддерживали.
