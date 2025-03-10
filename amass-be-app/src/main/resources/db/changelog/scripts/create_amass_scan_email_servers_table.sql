CREATE TABLE IF NOT EXISTS amass_scan_email_servers
(
    amass_scan_id BIGINT       NOT NULL,
    email_server  VARCHAR(255) NOT NULL,
    PRIMARY KEY (amass_scan_id, email_server),
    FOREIGN KEY (amass_scan_id) REFERENCES amass_scan (id) ON DELETE CASCADE
);