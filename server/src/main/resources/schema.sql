DROP TABLE IF EXISTS requests, comments,bookings,items, users;

CREATE TABLE IF NOT EXISTS users
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    100
) NOT NULL,
    email VARCHAR
(
    100
) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE
(
    email
)
    );
CREATE TABLE IF NOT EXISTS requests
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    description
    VARCHAR
(
    512
) NOT NULL,
    requestor_id BIGINT,
    created TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY
(
    requestor_id
) REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS items
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    512
) NOT NULL,
    available BOOLEAN,
    owner_id BIGINT NOT NULL,
    request_id BIGINT,
    FOREIGN KEY
(
    request_id
) REFERENCES requests
(
    id
),
    CONSTRAINT fk_items_owner_id FOREIGN KEY
(
    owner_id
) REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    start_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    end_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    item_id
    BIGINT
    NOT
    NULL,
    booker_id
    BIGINT
    NOT
    NULL,
    status
    VARCHAR
(
    64
),
    CONSTRAINT fk_bookings_item_id FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
),
    CONSTRAINT fk_bookings_booker_id FOREIGN KEY
(
    booker_id
) REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    PRIMARY
    KEY,
    text
    VARCHAR
(
    2048
) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT fk_comments_item_id FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
),
    CONSTRAINT fk_comments_author_id FOREIGN KEY
(
    author_id
) REFERENCES users
(
    id
)
    );