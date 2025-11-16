ALTER TABLE management.company
    ADD COLUMN IF NOT EXISTS registered_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS trade_name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS registration_status_description VARCHAR(100),
    ADD COLUMN IF NOT EXISTS primary_phone VARCHAR(20);

CREATE TABLE IF NOT EXISTS management.company_partner (
    id SERIAL PRIMARY KEY,
    fk_company INT NOT NULL REFERENCES management.company(id) ON DELETE CASCADE,
    fk_person INT NOT NULL REFERENCES management.person(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE management.person
    ADD COLUMN IF NOT EXISTS fk_company INT REFERENCES management.company(id);

ALTER TABLE management.company
    DROP COLUMN IF EXISTS business_start_date;
