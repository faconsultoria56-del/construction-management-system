CREATE TABLE management.state (
    id SERIAL PRIMARY KEY,
    uf VARCHAR(2) UNIQUE NOT NULL
);

CREATE TABLE management.city (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    fk_state INT NOT NULL REFERENCES management.state(id)
);

ALTER TABLE management.address
    ADD COLUMN fk_city INT,
    DROP COLUMN city,
    DROP COLUMN state;

ALTER TABLE management.address
    ADD CONSTRAINT fk_address_city FOREIGN KEY (fk_city) REFERENCES management.city(id);
