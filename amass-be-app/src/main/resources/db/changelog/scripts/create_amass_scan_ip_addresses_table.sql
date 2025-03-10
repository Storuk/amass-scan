CREATE TABLE IF NOT EXISTS amass_scan_ip_addresses
(
    amass_scan_id BIGINT       NOT NULL,
    ip_address    VARCHAR(255) NOT NULL,
    PRIMARY KEY (amass_scan_id, ip_address),
    FOREIGN KEY (amass_scan_id) REFERENCES amass_scan (id) ON DELETE CASCADE
);