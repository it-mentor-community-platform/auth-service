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

**Request body:**
```json
{
  "initDataRaw": "..."
}

# Build and run Docker image

docker build -t auth-service:local-stack .
docker run -p 8080:8080 --env SPRING_PROFILES_ACTIVE=local-stack auth-service:local-stack