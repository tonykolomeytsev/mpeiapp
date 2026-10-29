# third_party

Библиотеки, лежащие в репозитории в виде готовых артефактов (локальный Maven-репозиторий `third_party/maven`,
подключён в `settings.gradle` как `VendoredLibs`). Благодаря этому сборка не зависит от чужих аккаунтов и токенов.

## com.kekmech:lib_bars:0.4.1

Клиент личного кабинета БАРС (вход, двухфакторная аутентификация, список студентов, оценки).
Используется модулем `feature_bars_impl`.

| | |
|---|---|
| Автор | Антон Коломейцев ([@tonykolomeytsev](https://github.com/tonykolomeytsev)), автор оригинального MpeiX |
| Источник | GitHub Packages: https://github.com/tonykolomeytsev/mpeiapp/packages/2895096 |
| Файлы | `lib_bars-0.4.1.aar`, `.pom`, `.module` — без изменений, как опубликованы |
| SHA-256 `.aar` | `76a40465cb3a8723ebf4db65599869e7e37532648aefbfd90cfdac455c38fbe1` |
| Лицензия | в артефакте файла лицензии нет; репозиторий-источник распространяется под MIT (c) 2021 Anton Kolomeytsev |

Особенности: Android-библиотека (AAR) поверх OkHttp и jsoup; внутренние классы обфусцированы R8, а строковые
константы зашифрованы LSParanoid, поэтому по самому артефакту не видно, какие адреса он запрашивает. Для iOS/KMP
её придётся заменить собственной реализацией (см. issue про iOS).

Хэш можно проверить так: `sha256sum third_party/maven/com/kekmech/lib_bars/0.4.1/lib_bars-0.4.1.aar`.
