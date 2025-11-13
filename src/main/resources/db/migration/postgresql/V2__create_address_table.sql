CREATE TABLE IF NOT EXISTS management.address (
    id SERIAL PRIMARY KEY,
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
    ADD COLUMN IF NOT EXISTS fk_address INT REFERENCES management.address(id);
