-- ============================================================
-- FITNESS APP - SCRIPT DE CREACION DE TABLAS
-- Base de datos: fitness_progress
-- ============================================================

BEGIN;

-- -----------------------------------------------------------
-- 1. users
-- -----------------------------------------------------------
CREATE TABLE users (
    id            BIGSERIAL       PRIMARY KEY,
    email         VARCHAR(255)    NOT NULL,
    password_hash VARCHAR(255)    NOT NULL,
    name          VARCHAR(100),
    created_at    TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_users_email ON users (email);


-- -----------------------------------------------------------
-- 2. exercise_categories
-- -----------------------------------------------------------
CREATE TABLE exercise_categories (
    id         BIGSERIAL       PRIMARY KEY,
    user_id    BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name       VARCHAR(50)     NOT NULL,
    created_at TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_exercise_categories_user_name
    ON exercise_categories (user_id, name);


-- -----------------------------------------------------------
-- 3. daily_records
-- -----------------------------------------------------------
CREATE TABLE daily_records (
    id                    BIGSERIAL       PRIMARY KEY,
    user_id               BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    record_date           DATE            NOT NULL,
    weight_kg             DECIMAL(5,2)    CHECK (weight_kg > 0),
    body_fat_pct          DECIMAL(4,2)    CHECK (body_fat_pct >= 0 AND body_fat_pct <= 100),
    kcal_consumed         INTEGER         CHECK (kcal_consumed >= 0),
    kcal_expended         INTEGER         CHECK (kcal_expended >= 0),
    exercise_category_id  BIGINT          REFERENCES exercise_categories (id) ON DELETE SET NULL,
    exercise_duration_min INTEGER         CHECK (exercise_duration_min >= 0),
    notes                 TEXT,
    created_at            TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_daily_records_user_date
    ON daily_records (user_id, record_date);

CREATE INDEX idx_daily_records_user_date_desc
    ON daily_records (user_id, record_date DESC);


-- -----------------------------------------------------------
-- 4. body_measurements
-- -----------------------------------------------------------
CREATE TABLE body_measurements (
    id               BIGSERIAL       PRIMARY KEY,
    user_id          BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    measurement_date DATE            NOT NULL,
    chest_cm         DECIMAL(5,2),
    waist_cm         DECIMAL(5,2),
    hips_cm          DECIMAL(5,2),
    arm_cm           DECIMAL(5,2),
    thigh_cm         DECIMAL(5,2),
    neck_cm          DECIMAL(5,2),
    created_at       TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_body_measurements_user_date
    ON body_measurements (user_id, measurement_date);


-- -----------------------------------------------------------
-- 5. user_configs
-- -----------------------------------------------------------
CREATE TABLE user_configs (
    id                BIGSERIAL       PRIMARY KEY,
    user_id           BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    api_key_encrypted TEXT,
    provider          VARCHAR(50)     NOT NULL,
    base_url          VARCHAR(255)    NOT NULL,
    default_model     VARCHAR(100),
    created_at        TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_user_configs_user_id ON user_configs (user_id);


-- -----------------------------------------------------------
-- 6. chat_messages
-- -----------------------------------------------------------
CREATE TABLE chat_messages (
    id         BIGSERIAL       PRIMARY KEY,
    user_id    BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role       VARCHAR(20)     NOT NULL CHECK (role IN ('user', 'assistant', 'system', 'tool')),
    content    TEXT            NOT NULL,
    created_at TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_chat_messages_user_time
    ON chat_messages (user_id, created_at);


-- -----------------------------------------------------------
-- 7. refresh_tokens
-- -----------------------------------------------------------
CREATE TABLE refresh_tokens (
    id         BIGSERIAL       PRIMARY KEY,
    user_id    BIGINT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token      VARCHAR(512)    NOT NULL,
    expires_at TIMESTAMPTZ     NOT NULL,
    created_at TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_refresh_tokens_token ON refresh_tokens (token);


COMMIT;
