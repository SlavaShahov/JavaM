--liquibase formatted sql

-- changeset yourname:001_create_tables

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR NOT NULL,
    author_id INTEGER,
    post_id INTEGER,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- rollback drop table comments;