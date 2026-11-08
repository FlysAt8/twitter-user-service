# Федоров А.Д.
# User Service
Сервис для управления пользователями, ролями и аутентификацией

## Запуск
Для хранения данных используется PostgreSQL, запущенный в Docker:
```bash
docker run -d --name user-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=user_db -p 5433:5432 -v user_pgdata:/var/lib/postgresql/data --restart unless-stopped postgres:16
```

Основной класс `UserserviceApplication` запускает сервис

Приложение автоматически создаст таблицы в базе данных (с помощью `schema.sql`)

Доступ к сервису - `http://localhost:9000`

## Эндпоинты
### Пользователи
* `POST /users` — Регистрация нового пользователя (автоматически выдаётся роль guest).
* `GET /users/{id}` — Получить профиль пользователя (включая роли).
* `GET /users?ids=...` — Получить список пользователей по их ID (батч-запрос для других сервисов).
* `PUT /users/{id}` — Обновить профиль.
* `DELETE /users/{id}` — Удалить пользователя.
### Аутентификация
* `POST /auth/login` — Логин по email и паролю (возвращает JWT-токен).
### Роли
* `GET /roles` — Получить список всех доступных ролей.
* `POST /roles/assign/{userId}/{roleId}` — Выдать роль пользователю.

## Отличия от схемы
1. Нет таблицы `sessions`, в пользу JWT.
2. Для ролей использован тип данных `Long` вместо `UUID`, так как их немного.
3. Нет метода `getUserRoles`, вместо этого `GET /users/{id}`, возвращает пользователей сразу с ролями
4. Для связующей таблицы `user_role` используется `DaoHelper` с `JdbcTemplate`, так как `Spring Data JDBC` не поддерживает составные первичные ключи.