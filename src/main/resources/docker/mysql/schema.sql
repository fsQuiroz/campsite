CREATE TABLE reservation
(
    id        BIGINT AUTO_INCREMENT,
    created   TIMESTAMP DEFAULT NOW() NOT NULL,
    updated   TIMESTAMP               NULL,
    deleted   TIMESTAMP               NULL,
    name      VARCHAR(255)            NOT NULL,
    email     VARCHAR(255)            NOT NULL,
    arrival   DATE                    NOT NULL,
    departure DATE                    NOT NULL,
    CONSTRAINT reservation_pk PRIMARY KEY (id)
);