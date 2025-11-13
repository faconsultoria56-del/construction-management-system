CREATE TABLE IF NOT EXISTS management.state (
    id SERIAL PRIMARY KEY,
    uf VARCHAR(2) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS management.city (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    fk_state INT NOT NULL REFERENCES management.state(id)
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_attribute WHERE attrelid = 'management.address'::regclass AND attname = 'fk_city') THEN
        ALTER TABLE management.address ADD COLUMN fk_city INT;
    END IF;
END
$$;

DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_attribute WHERE attrelid = 'management.address'::regclass AND attname = 'city') THEN
        ALTER TABLE management.address DROP COLUMN city;
    END IF;
END
$$;

DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_attribute WHERE attrelid = 'management.address'::regclass AND attname = 'state') THEN
        ALTER TABLE management.address DROP COLUMN state;
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_constraint WHERE conname = 'fk_address_city' AND conrelid = 'management.address'::regclass) THEN
        ALTER TABLE management.address ADD CONSTRAINT fk_address_city FOREIGN KEY (fk_city) REFERENCES management.city(id);
    END IF;
END
$$;
