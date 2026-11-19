-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname    VARCHAR(255) NOT NULL,
    lastname     VARCHAR(255),
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone        VARCHAR(20)  NOT NULL UNIQUE,
    password     VARCHAR(250) NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);

-- Таблица ролей
CREATE TABLE IF NOT EXISTS roles (
    id     BIGSERIAL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL UNIQUE
);

-- Связующая таблица
CREATE TABLE IF NOT EXISTS user_role (
    user_id  UUID   NOT NULL,
    role_id  BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Начальные роли
INSERT INTO roles (name) VALUES
('guest'),
('producer'),
('subscriber'),
('administrator')
ON CONFLICT (name) DO NOTHING;