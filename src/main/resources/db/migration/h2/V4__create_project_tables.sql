CREATE TABLE IF NOT EXISTS management.project (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    fk_company INT,
    fk_owner_person INT,
    fk_address INT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (fk_company) REFERENCES management.company(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_owner_person) REFERENCES management.person(id),
    FOREIGN KEY (fk_address) REFERENCES management.address(id),
    CONSTRAINT project_owner_or_company CHECK (
        (fk_company IS NOT NULL AND fk_owner_person IS NULL)
        OR
        (fk_company IS NULL AND fk_owner_person IS NOT NULL)
    )
);

CREATE TABLE IF NOT EXISTS management.project_member (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_project INT,
    fk_person INT,
    role VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (fk_project) REFERENCES management.project(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_person) REFERENCES management.person(id) ON DELETE CASCADE,
    UNIQUE (fk_project, fk_person)
);
