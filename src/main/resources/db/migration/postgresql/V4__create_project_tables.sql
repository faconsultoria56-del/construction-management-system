CREATE TABLE IF NOT EXISTS management.project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    fk_company INT REFERENCES management.company(id) ON DELETE CASCADE,
    fk_owner_person INT REFERENCES management.person(id),
    fk_address INT REFERENCES management.address(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT project_owner_or_company CHECK (
        (fk_company IS NOT NULL AND fk_owner_person IS NULL)
        OR
        (fk_company IS NULL AND fk_owner_person IS NOT NULL)
    )
);

CREATE TABLE IF NOT EXISTS management.project_member (
    id SERIAL PRIMARY KEY,
    fk_project INT REFERENCES management.project(id) ON DELETE CASCADE,
    fk_person INT REFERENCES management.person(id) ON DELETE CASCADE,
    role VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (fk_project, fk_person)
);
