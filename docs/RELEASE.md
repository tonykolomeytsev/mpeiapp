# Выпуск релизов «МЭИ.Хаб»

Этот документ описывает, как подписывается и публикуется приложение. Он обновляется вместе с процессом выпуска;
открытые вопросы по публикации в сторах ведутся в issue [#8](https://github.com/ghostoftheeleven/mpei-next/issues/8).

## Идентификаторы

| Что | Значение |
|---|---|
| `applicationId` (релиз) | `ru.mpei.hub` |
| `applicationId` (debug, qa) | `ru.mpei.hub.dev` |
| `namespace` в коде | `kekmech.ru.mpeiapp` — **не менять** (иначе переедет весь код и манифест) |
| Firebase-проект | `mpei-hub` (Crashlytics, Analytics, Remote Config) |

`ru.mpei.hub` — новое приложение для пользователей: версии v0.5.x (`kekmech.ru.mpeiapp`) на него не обновляются
и могут стоять на устройстве рядом.

## Ключ подписи

Все релизы подписываются одним ключом. Файл ключа **не хранится в репозитории** (`*.jks`, `*.keystore`, `keystore/` в `.gitignore`).

| Параметр | Значение |
|---|---|
| Файл | `mpei_hub_release.jks` (PKCS#12) |
| Alias | `mpei_hub` |
| Алгоритм | RSA 4096 |
| SHA-256 сертификата | `9a101fb9d9e2cea0f0e0e1b7d1600c00fca3f006addc99561c33fa7ef115c8f2` |
| Подписаны этим ключом | v0.5.0 и v0.5.1 (пакет `kekmech.ru.mpeiapp`), далее `ru.mpei.hub` |

Проверка отпечатка файла ключа (в PowerShell):

```powershell
& "C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe" -list -v -keystore mpei_hub_release.jks -alias mpei_hub
```

В выводе строка `SHA256:` должна совпасть с таблицей выше (двоеточия и регистр не важны).

### Правила хранения

- Файл и пароль — в менеджере паролей и в одной офлайн-копии (флешка в надёжном месте). **Потеря ключа = невозможность обновить приложение** у пользователей APK/RuStore.
- Пароль длинный и случайный. Не отправлять файл и пароль в чаты, почту, трекер и AI-сессии: в чат можно отправлять только отпечатки (SHA-1/SHA-256), они не секретные.
- Если есть подозрение на утечку ключа или пароля — заменить ключ до первой публикации в сторах (после публикации замена очень дорога).

## Секреты GitHub Actions

Репозиторий → Settings → Secrets and variables → Actions → New repository secret:

| Секрет | Значение |
|---|---|
| `SIGNING_KEYSTORE_BASE64` | файл `.jks` в Base64 |
| `SIGNING_STORE_PASSWORD` | пароль хранилища |
| `SIGNING_KEY_ALIAS` | `mpei_hub` |
| `SIGNING_KEY_PASSWORD` | пароль ключа (для PKCS#12 совпадает с паролем хранилища) |

Файл в Base64 (результат попадает в буфер обмена):

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("mpei_hub_release.jks")) | Set-Clipboard
```

Без этих секретов workflow подписывает одноразовым ключом: такую сборку можно собрать и проверить, но нельзя публиковать.

## Выпуск через CI (`Release APK`)

Workflow `.github/workflows/release-apk.yaml`:

1. Юнит-тесты (`testDebugUnitTest`).
2. Подпись: из секретов или одноразовым ключом.
3. Сборка `:app:assembleRelease` и `:app:bundleRelease`.
4. Проверка подписи APK (`apksigner`) и AAB (`jarsigner`); при настоящем ключе — сверка SHA-256 сертификата с таблицей выше.
5. Артефакты: `release-apk` (APK) и `release-aab` (AAB для Google Play и RuStore).
6. Смоук-тест на эмуляторе (светлая и тёмная темы, обход вкладок).
7. Публикация GitHub Release (только для тега `vX.Y.Z` или ручного запуска с `publish = true` и тегом).

Запуск:

- **Проверка без публикации:** Actions → Release APK → Run workflow → `publish = false`.
- **Релиз:** поднять `versionName`/`versionCode` в `modules/app/build.gradle`, обновить `.github/release-notes.md`, создать тег `vX.Y.Z`.

## Публикация заранее подписанного APK (`Publish signed release`)

Если APK подписан вне CI (ключ только у владельца): закоммитить APK во **временную ветку**, запустить workflow
`Publish signed release` на этой ветке (Use workflow from), указать тег и путь к APK, затем удалить ветку.
APK не держим в `main`: бинарники остаются в истории git навсегда. Workflow сверяет SHA-256 сертификата APK с ключом из таблицы.

## Сторы

- **Google Play:** загружается AAB (`release-aab`). Решение про Play App Signing (загрузить свой ключ как ключ подписи приложения или принять, что подпись Play отличается от GitHub-APK) принимается до первой загрузки; см. #8. Новым личным аккаунтам разработчика перед продакшеном нужно закрытое тестирование (по правилам Google: 12 тестеров на протяжении 14 дней — проверить актуальные условия в консоли).
- **RuStore:** APK или AAB, подпись своим ключом.
- **App Store:** iOS-версии пока нет, см. #5.

После получения релизного ключа в Firebase (Project settings → приложение `ru.mpei.hub` → Add fingerprint) добавить SHA-1 и SHA-256 ключа и ограничить API-ключ по пакету и SHA-1 (Google Cloud Console → APIs & Services → Credentials).

## Локальная подписанная сборка

Задать переменные окружения (или `~/.gradle/gradle.properties`) и запустить `./gradlew :app:assembleRelease :app:bundleRelease`:

```text
SIGNING_KEYSTORE_PATH   путь к .jks (абсолютный или от корня проекта)
SIGNING_STORE_PASSWORD  пароль хранилища
SIGNING_KEY_ALIAS       mpei_hub
SIGNING_KEY_PASSWORD    пароль ключа (по умолчанию равен паролю хранилища)
```

Без них `release` подписывается debug-сертификатом — такой APK публиковать нельзя.
