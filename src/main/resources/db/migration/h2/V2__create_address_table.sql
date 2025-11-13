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

ALTER TABLE management.company
    ADD COLUMN IF NOT EXISTS fk_address INT;

ALTER TABLE management.company
    ADD CONSTRAINT IF NOT EXISTS fk_address_company FOREIGN KEY (fk_address) REFERENCES management.address(id);
