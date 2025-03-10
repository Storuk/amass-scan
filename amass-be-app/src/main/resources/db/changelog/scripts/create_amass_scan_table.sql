CREATE TABLE IF NOT EXISTS amass_scan
(
    id         BIGSERIAL  PRIMARY KEY,
    domain     VARCHAR(255) NOT NULL,
    start_time TIMESTAMP    NOT NULL,
    end_time   TIMESTAMP,
    status     VARCHAR(255) NOT NULL,
    version    INTEGER      NOT NULL
);