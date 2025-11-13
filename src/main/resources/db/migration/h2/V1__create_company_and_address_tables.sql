CREATE TABLE IF NOT EXISTS management.state (
    id INT PRIMARY KEY AUTO_INCREMENT,
    uf VARCHAR(2) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS management.city (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    fk_state INT NOT NULL,
    FOREIGN KEY (fk_state) REFERENCES management.state(id)
);

CREATE TABLE IF NOT EXISTS management.address (
    id INT PRIMARY KEY AUTO_INCREMENT,
    street VARCHAR(255),
    number VARCHAR(255),
    neighborhood VARCHAR(255),
    zip_code VARCHAR(255),
    complement VARCHAR(255),
    fk_city INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_city) REFERENCES management.city(id)
);

CREATE TABLE IF NOT EXISTS management.company (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_address INT,
    registered_name VARCHAR(255),
    trade_name VARCHAR(255),
    registration_status_description VARCHAR(100),
    primary_phone VARCHAR(20),
    FOREIGN KEY (fk_address) REFERENCES management.address(id)
);

CREATE TABLE IF NOT EXISTS management.person (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_company INT,
    full_name VARCHAR(255),
    document VARCHAR(255),
    email VARCHAR(255),
    birth_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_company) REFERENCES management.company(id)
);

CREATE TABLE IF NOT EXISTS management.company_partner (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_company INT NOT NULL,
    fk_person INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_company) REFERENCES management.company(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_person) REFERENCES management.person(id) ON DELETE CASCADE
);
