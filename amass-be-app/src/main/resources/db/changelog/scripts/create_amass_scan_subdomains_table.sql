CREATE TABLE IF NOT EXISTS amass_scan_subdomains
(
    amass_scan_id BIGINT       NOT NULL,
    subdomain     VARCHAR(255) NOT NULL,
    PRIMARY KEY (amass_scan_id, subdomain),
    FOREIGN KEY (amass_scan_id) REFERENCES amass_scan (id) ON DELETE CASCADE
);