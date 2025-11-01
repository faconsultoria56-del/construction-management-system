CREATE TABLE IF NOT EXISTS management.address (
    id INT PRIMARY KEY,
    street VARCHAR(255),
    number VARCHAR(255),
    neighborhood VARCHAR(255),
    zip_code VARCHAR(255),
    complement VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS management.company (
    id INT PRIMARY KEY,
    fk_address INT,
    FOREIGN KEY (fk_address) REFERENCES management.address(id)
);

CREATE TABLE IF NOT EXISTS management.person (
    id INT PRIMARY KEY
);

ALTER TABLE management.company ADD COLUMN registered_name VARCHAR(255);
ALTER TABLE management.company ADD COLUMN trade_name VARCHAR(255);
ALTER TABLE management.company ADD COLUMN registration_status_description VARCHAR(100);
ALTER TABLE management.company ADD COLUMN primary_phone VARCHAR(20);

CREATE TABLE IF NOT EXISTS management.company_partner (
    id INT PRIMARY KEY,
    fk_company INT NOT NULL,
    fk_person INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_company) REFERENCES management.company(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_person) REFERENCES management.person(id) ON DELETE CASCADE
);

ALTER TABLE management.person ADD COLUMN fk_company INT;

ALTER TABLE management.person ADD CONSTRAINT fk_company_person FOREIGN KEY (fk_company) REFERENCES management.company(id);
