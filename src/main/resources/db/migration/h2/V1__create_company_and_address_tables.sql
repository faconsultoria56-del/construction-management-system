CREATE SCHEMA IF NOT EXISTS management;
SET search_path TO management;

-- 1. LOCALIZAÇÃO (Sem dependências)
CREATE TABLE IF NOT EXISTS country (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    code CHAR(2) UNIQUE
);

CREATE TABLE IF NOT EXISTS state (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    abbreviation CHAR(2) NOT NULL,
    fk_country INT REFERENCES management.country(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS city (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    fk_state INT REFERENCES management.state(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(20),
    complement VARCHAR(100),
    neighborhood VARCHAR(100),
    fk_city INT REFERENCES management.city(id) ON DELETE SET NULL,
    fk_state INT REFERENCES management.state(id) ON DELETE SET NULL,
    zip_code VARCHAR(10),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. INFRAESTRUTURA BASE
CREATE TABLE IF NOT EXISTS document_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS plan_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) DEFAULT 0.0,
    max_users INT DEFAULT 5,
    max_projects INT DEFAULT 10,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-- 3. ENTIDADES PRINCIPAIS
CREATE TABLE IF NOT EXISTS company (
    id SERIAL PRIMARY KEY,
    document VARCHAR(20) NOT NULL UNIQUE,
    fk_document_type INT NOT NULL REFERENCES management.document_type(id),
    registered_name VARCHAR(255) NOT NULL,
    trade_name VARCHAR(255),
    registration_status_description VARCHAR(100),
    email VARCHAR(150),
    primary_phone VARCHAR(20),
    fk_plan_type INT NOT NULL REFERENCES management.plan_type(id),
    fk_address INT REFERENCES management.address(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS person (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    phone VARCHAR(20),
    fk_address INT REFERENCES management.address(id) ON DELETE SET NULL,
    kyc_status VARCHAR(30) DEFAULT 'pending',
    crea_number VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. TABELAS DE LIGAÇÃO E SEGURANÇA (Dependem de Company e Person)
CREATE TABLE IF NOT EXISTS person_document (
    id SERIAL PRIMARY KEY,
    fk_person INT NOT NULL REFERENCES management.person(id) ON DELETE CASCADE,
    fk_document_type INT NOT NULL REFERENCES management.document_type(id),
    document_value VARCHAR(50) NOT NULL,
    issued_at DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(fk_person, fk_document_type)
);

CREATE TABLE IF NOT EXISTS user_account (
    id SERIAL PRIMARY KEY,
    fk_person INT REFERENCES management.person(id) ON DELETE CASCADE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS company_member (
    id SERIAL PRIMARY KEY,
    fk_company INT NOT NULL REFERENCES management.company(id) ON DELETE CASCADE,
    fk_person INT NOT NULL REFERENCES management.person(id) ON DELETE CASCADE,
    fk_role INT NOT NULL REFERENCES management.role(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(fk_company, fk_person)
);

CREATE TABLE IF NOT EXISTS person_role (
    id SERIAL PRIMARY KEY,
    fk_person INT NOT NULL REFERENCES management.person(id) ON DELETE CASCADE,
    fk_role INT NOT NULL REFERENCES management.role(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(fk_person, fk_role)
);

-- 5. INSERTS (Sempre ao final para garantir que as FKs existam)
INSERT INTO country (name, code) VALUES ('Brasil', 'BR') ON CONFLICT DO NOTHING;
INSERT INTO document_type (code) VALUES ('CPF'), ('CNPJ'), ('CIN'), ('PASSAPORTE') ON CONFLICT DO NOTHING;
INSERT INTO role (name) VALUES ('Owner'), ('Manager'), ('Worker'), ('Partner') ON CONFLICT DO NOTHING;
INSERT INTO plan_type (code, name, price) VALUES ('FREE', 'Plano Gratuito', 0.0) ON CONFLICT DO NOTHING;