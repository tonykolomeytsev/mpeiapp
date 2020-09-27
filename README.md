# MpeiX - приложение МЭИ здорового человека

![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/tonykolomeytsev/mpeiapp?label=version) 
![](https://github.com/tonykolomeytsev/mpeiapp/workflows/Android%20build/badge.svg?branch=master)

Расписание пар, личный кабинет БАРС и крутая карта для студентов НИУ МЭИ. Самое функциональное, легковесное и стабильное МЭИшное приложение.

**Фичи приложения:**
+ Личный кабинет БАРС (мы работаем над тем чтобы вернуть его)
+ Просмотр расписаний
+ **ТЁМНАЯ ТЕМА**
+ Дашборд с самой актуальной информацией
+ Карта корпусов, общежитий, мест общепита и кучи других мест
+ Добавление заметок заданий к парам
+ Самый маленький вес приложения по сравнению с конкурентами
+ Открiтый исходнiй код 
+ Меми.🤗💪😸😃

Пулл реквесты приветствуются.

[![](https://github.com/tonykolomeytsev/mpeiapp/raw/master/screenshots/3.png)](https://play.google.com/store/apps/details?id=kekmech.ru.mpeiapp)

### Скриншоты

![screenshots](https://github.com/tonykolomeytsev/mpeiapp/raw/master/screenshots/0.png)

### Требования приложения

+ Android 5 Lollipop и выше (Api V21+)
+ Доступ к интернету

### Стек
 
+ Многомодульная, упрощенная **Clean Architecture** с активным использованием **Koin**.
+ ~~Kotlin Coroutines~~ RxJava
+ ~~Android Arch~~ 
  - ~~Navigation Component~~ Самописная навигация
  - ~~LiveData~~ RxJava
  - ~~Room~~ Vanilla SQLite
+ Firebase 
  - ~~Firestore~~ Собственный бэкенд
  - Crashlytics
  - Analytics
  - Remote Config
+ ~~Для скрапинга веб-сайтов используется Jsoup~~ Ничего больше не скрапится, есть бэкенд который делает всю грязную работу.
+ Для подгрузки изображений используется Picasso

### Архитектура

Разделение на common, domain, feature модули.
- common - могут зависеть от других common-модулей
- domain - могут зависеть от других common-модулей и domain-модулей
- feature - могут зависеть от common-модулей и domain-модулей, но ни в коем случае не зависят от feature-модулей.

Сделано это для ускорения билда. Также повсюду выпилен kapt. Везде, где можно, используются обычные kotlin-модули вместо android-модулей. В android-модулях подключено минимум зависимостей, минимум плагинов. Используются обыкновенные groovy gradle скрипты вместо buildSrc и KTS.

#### Почему избавились от Kotlin Coroutines?

Пришлось мигрировать на RxJava, потому что она ~~помогает выявить таких же дидов как я~~ всем понятна, существует давно и все ее подводные камни известны. В корутинах многого еще не хватает (либо автор слишком тупой чтобы понять их гениальность).

#### Почему избавились от Android Arch Nav Component?

Потому что сколько-нибудь большое количество экранов превращало визуальный граф приложения в кашу/вермишель (что вам больше нравится). Фрагменты реплейсятся, вместо того чтобы добавляться в стек. Текущая реализация навигации от гугла рассчитана для одномодульных приложений с легкой версткой экранов, потому что любой переход между фрагментами пересоздавал view этих фрагментов. Зачем? И я не знаю.

#### Почему избавились от LiveData?

По той же причине, по которой избавились от корутин.

#### Почему избавились от Room?

Room - хорошая библиотека если у вас одномодульное приложение. К сожалению, оставаясь типичным ORM, он требует кодогенерации (использования плагина kapt).

Во всех модулях, в которых объявляются сущности для Room, используются аннотации Room и, собственно, kapt. Это очень негативно влияет на время сборки проекта. Плюс, изначально БД была спроектирована не правильно, отчего стало невозможным тестировать миграции.

Теперь в приложении универсальная запускалка SQL скриптов: `AppDatabase#fetch`. Потому что проще писать голые скрипты, чем пользоваться Room.

#### Почему избавились от Firestore?

1. Лимит на количество запросов. В сентябре 2020 года он был достигнут.
1. У приложения появился свой собственный бэкенд.

#### Что там со скрапингом?

Все данные приложение теперь получает с бэкенда, в скрапинге больше нет необходимости, кроме случаев обхода блокировок.