--liquibase formatted sql

-- changeset yourname:001_create_tables
CREATE TABLE category (
                          id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE pet (
                     id BIGINT PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     category_id BIGINT,
                     status VARCHAR(50),
                     FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE SET NULL
);

CREATE TABLE tag (
                     id BIGINT PRIMARY KEY,
                     name VARCHAR(255) NOT NULL
);

CREATE TABLE pet_tags (
                          pet_id BIGINT,
                          tag_id BIGINT,
                          PRIMARY KEY (pet_id, tag_id),
                          FOREIGN KEY (pet_id) REFERENCES pet(id) ON DELETE CASCADE,
                          FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);

-- rollback drop table pet_tags, tag, pet, category;