CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE CONSTRAINT user_name_length CHECK (LENGTH(name)>=3) NOT NULL,
    password VARCHAR(100) CONSTRAINT user_password_length CHECK (LENGTH(password)>=3) NOT NULL,
    role VARCHAR(20) NOT NULL,
    CHECK (role IN ('ROLE_USER', 'ROLE_ADMIN')),
    view_type VARCHAR(5) DEFAULT 'LIST' NOT NULL,
    CHECK (view_type IN ('LIST', 'TABLE'))
);

CREATE TABLE notes (
    note_id VARCHAR(40) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) CONSTRAINT note_title_length CHECK (LENGTH(title)>=5),
    content VARCHAR(10000) CONSTRAINT note_content_length CHECK (LENGTH(content)>=5),
    privacy VARCHAR(7) DEFAULT 'PRIVATE' CHECK (privacy IN ('PRIVATE', 'PUBLIC')) NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);