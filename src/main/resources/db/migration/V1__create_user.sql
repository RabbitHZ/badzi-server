CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    provider    VARCHAR(20)  NOT NULL,
    provider_id VARCHAR(100) NOT NULL,
    username    VARCHAR(100),
    email       TEXT,
    email_hash  VARCHAR(64)  NOT NULL,
    name        TEXT,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_users_provider UNIQUE (provider, provider_id),
    CONSTRAINT uq_users_email_hash UNIQUE (email_hash)
);

CREATE INDEX idx_users_email_hash ON users (email_hash);
