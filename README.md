# MpeiX - приложение МЭИ здорового человека

![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/tonykolomeytsev/mpeiapp?label=version) 
[![Build status](https://github.com/tonykolomeytsev/mpeiapp/actions/workflows/android-release.yaml/badge.svg?branch=master)](https://github.com/tonykolomeytsev/mpeiapp/actions/workflows/android-release.yaml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/tonykolomeytsev/mpeiapp/blob/master/LICENSE)

<p align="center">
  <img src="https://github.com/tonykolomeytsev/mpeiapp/raw/master/.github/media/gradient_logo.png" />
</p>

Расписание пар, заметки, личный кабинет БАРС и крутая карта для студентов и преподавателей НИУ МЭИ. Самое функциональное, легковесное и стабильное МЭИшное приложение.

**Фичи приложения:**
+ Просмотр расписаний **групп и преподавателей**
+ Карта корпусов, общежитий, мест общепита, известных мест кампуса МЭИ и кафедр.
+ Добавление **заметок** к парам.
+ Просмотр ЛК **БАРС**.
+ Дашборд с ближайшими парами, заметками, расписанием сессии и объявлениями.
+ Быстрое переключение между **избранными расписаниями**.
+ Переключение между **ТЁМНОЙ ТЕМОЙ** и светлой темой.
+ Переключение между русским и английским языком в приложении.
+ Поиск по заметкам, карте, группам и преподавателям.
+ Предпросмотр расписаний групп и преподавателей прямо с экрана поиска.
+ Кэширование расписаний и геометок на устройстве.
+ Поддержка версии для планшетов.
+ Самый маленький вес приложения по сравнению с конкурентами.
+ Открiтый исходнiй код.
+ Меми.🤗💪😸😃

Пулл реквесты приветствуются.

[![](https://github.com/tonykolomeytsev/mpeiapp/raw/master/.github/media/available_in_google_play.png)](https://play.google.com/store/apps/details?id=kekmech.ru.mpeiapp)

### Скриншоты

![screenshots 1](https://github.com/tonykolomeytsev/mpeiapp/raw/master/.github/media/promo_wide_frame_1.png)

![screenshots 2](https://github.com/tonykolomeytsev/mpeiapp/raw/master/.github/media/promo_wide_frame_2.png)

### Требования приложения

+ Android 5.0 Lollipop и выше (Api V21+)
+ Доступ к интернету

### Стек

+ Многомодульная, упрощенная **Clean Architecture** с активным использованием DI **Koin**.
+ Vivid Money [**Elmslie**](https://github.com/vivid-money/elmslie)
+ Самописная навигация
+ **Retrofit** + **RxJava** для REST Api
+ Vanilla SQLite для хранения в БД заметок и избранных расписаний
+ Firebase 
  - Crashlytics
  - Analytics
  - Remote Config
+ Для подгрузки изображений используется **Picasso** (из-за соотношения веса и скорости работы)
+ Для тестирования используется **JUnit 5** + [**Kotest**](https://github.com/kotest/kotest)
+ Композитный билд и кастомные Gradle плагины

### Архитектура

Если хотите разобраться в устройстве проекта, специально для вас создан файлик [ARCHITECTURE.md](https://github.com/tonykolomeytsev/mpeiapp/blob/master/ARCHITECTURE.md).

### Планы на будущее

1. В самое ближайшее время планируется переход с RxJava на Kotlin Coroutines, как более легковесную альтернативу. Ну и потому что корутины теперь являются новым мейнстримом.
2. Переход с Fragments на Jetpack Compose. Это позволит уменьшить вес приложения, увеличить скорость билда, да и в целом упростить поддержку кодовой базы, что положительно скажется на частоте обновлений приложения.
3. Редизайн приложения в соответствии с новыми гайдлайнами Material.
4. Полировка существующих фич и добавление нового функционала. Новые фичи сейчас на стадии рисовки в Figma. Добавляться будут только реально необходимые функции. Лишная фигня, которой пользуются раз в год и которую сложно поддерживать, в приложении появляться не будет.

### Инструкция по сборке проекта

1. Добавьте `google-services.json` от Firebase в корень модуля `app` и в `app/src/debug` (можно один и тот же). [Инструкция](https://support.google.com/firebase/answer/7015592?hl=en) по получению `google-services.json`.

1. В глобальный `gradle.properties` добавьте поле `mpeiapp_google_maps_api_key="{SECRET}"`, где `{SECRET}` - ключ от API Google Maps. [Инструкция](https://developers.google.com/maps/gmp-get-started) по получению ключа.

1. Скачайте и установите JDK 17 версии, укажите в настройках проекта java 17 по умолчанию для Gradle и компилятора Kotlin:

   `File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle`

1. Установите плагин **Kotest**, с ним будет проще гонять тесты.

После первых двух пунктов проект соберется, после двух других начнут работать тесты. В связи с выходом Gradle 7.5.0 и AGP 7.4.0 проект перешел на Java 17.

По понятным причинам в репозитории не будут опубликованы оригинальные ключи от Google Maps API и Firebase API. Используйте свои ключики.
