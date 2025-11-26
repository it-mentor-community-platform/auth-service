# Auth Service

Микросервис аутентификации и авторизации пользователей Telegram Mini App.

## 🧠 Контекст

Сервис отвечает за аутентификацию пользователей на основе данных Telegram Mini App и выдачу JWT токенов для последующего доступа к API других микросервисов.

### Полезные ссылки:
- [Telegram Mini App init data](https://docs.telegram-mini-apps.com/platform/init-data)
- [Telegram Mini App launch parameters](https://docs.telegram-mini-apps.com/platform/launch-parameters)
- [JWT — структура, подпись и claims](https://auth0.com/docs/secure/tokens/json-web-tokens)

## ⚙️ Технологический стек

- **Java 21**
- **Spring Boot 3**
- **Spring Data JDBC**
- **Liquibase**
- **PostgreSQL**
- **JJWT** — для генерации и валидации JWT токенов

## 🔄 Взаимодействие

### Входящие:
- REST эндпоинты

### Схема БД:
- Таблица `Users`
- Таблица `Roles`
- Таблица `User_Roles`
  - Композитный индекс **(user_id, role_id)** — `UNIQUE`
- Список ролей определяется в бизнес-аналитике авторизации

## 🔐 REST API

### Авторизация через Telegram
`POST /api/auth/by-telegram`

## Инструкция по тестированию 

Для тестирования функционала в сервисе вам потребуется использовать токен бота, который можно получить в Secrets в гитхабе или написать лиду. После этого, воспользоваться тестовым ботом, который предлагает скопировать текущую `initData`, по этой ссылке - [telegram-bot](https://t.me/testtesttest111122233_bot/getdata).

Время жизни initData - 6 минут, eсли не закрывать бота. Такое значение выставлено в настройках application-ide.yml.

После этого использовать initData, которую вы скопировали, в качестве тела запроса, и не закрывать бота, пока тестирование не будет завершено.

При локальном тестировании на docker, использовать - http://localhost:8081/api/auth/by-telegram

**Request body:**
```text/plain
user=%7B%22id%22%3...
```
