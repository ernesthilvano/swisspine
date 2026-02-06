-- Flyway Migration: Initial Schema
-- Version: V1
-- Description: Create all tables for External Connections and Planner system

-- ============================================
-- Master Data Tables (Lookup Tables)
-- ============================================

CREATE TABLE source_names (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_source_names_name ON source_names(name);

CREATE TABLE run_names (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_run_names_name ON run_names(name);

CREATE TABLE report_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_report_types_name ON report_types(name);

CREATE TABLE report_names (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    report_type_id BIGINT REFERENCES report_types(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_report_names_name ON report_names(name);
CREATE INDEX idx_report_names_type ON report_names(report_type_id);

CREATE TABLE funds (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_funds_name ON funds(name);

CREATE TABLE fund_aliases (
    id BIGSERIAL PRIMARY KEY,
    fund_id BIGINT NOT NULL REFERENCES funds(id) ON DELETE CASCADE,
    alias_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_fund_alias UNIQUE(fund_id, alias_name)
);

CREATE INDEX idx_fund_aliases_fund ON fund_aliases(fund_id);
CREATE INDEX idx_fund_aliases_name ON fund_aliases(alias_name);

-- ============================================
-- External Connections Table
-- ============================================

CREATE TABLE external_connections (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    base_url VARCHAR(500) NOT NULL,
    authentication_method VARCHAR(50) NOT NULL,
    key_field VARCHAR(255) NOT NULL,
    value_field VARCHAR(500),  -- Sensitive field, masked in UI after initial entry
    value_field_set BOOLEAN NOT NULL DEFAULT FALSE,  -- Track if value has been set
    authentication_place VARCHAR(20) CHECK (authentication_place IN ('Header', 'QueryParameters')),
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0  -- Optimistic locking
);

-- Indexes for performance
CREATE INDEX idx_ext_conn_name ON external_connections(name);
CREATE INDEX idx_ext_conn_default ON external_connections(is_default) WHERE is_default = TRUE;

-- Ensure only one default connection exists
CREATE UNIQUE INDEX uk_ext_conn_one_default ON external_connections(is_default) WHERE is_default = TRUE;

-- ============================================
-- Planner Tables
-- ============================================

CREATE TABLE planners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    planner_type VARCHAR(100),
    external_system_config_id BIGINT REFERENCES external_connections(id) ON DELETE SET NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Draft',
    owner VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0  -- Optimistic locking
);

CREATE INDEX idx_planner_name ON planners(name);
CREATE INDEX idx_planner_owner ON planners(owner);
CREATE INDEX idx_planner_status ON planners(status);
CREATE INDEX idx_planner_created ON planners(created_at DESC);

-- ============================================
-- Planner Relationships
-- ============================================

CREATE TABLE planner_funds (
    id BIGSERIAL PRIMARY KEY,
    planner_id BIGINT NOT NULL REFERENCES planners(id) ON DELETE CASCADE,
    fund_id BIGINT NOT NULL REFERENCES funds(id) ON DELETE RESTRICT,
    fund_alias_id BIGINT REFERENCES fund_aliases(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uk_planner_fund UNIQUE(planner_id, fund_id)
);

CREATE INDEX idx_planner_funds_planner ON planner_funds(planner_id);
CREATE INDEX idx_planner_funds_fund ON planner_funds(fund_id);

-- ============================================
-- Planner Sources, Runs, and Reports
-- ============================================

CREATE TABLE planner_sources (
    id BIGSERIAL PRIMARY KEY,
    planner_id BIGINT NOT NULL REFERENCES planners(id) ON DELETE CASCADE,
    source_name_id BIGINT REFERENCES source_names(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_planner_sources_planner ON planner_sources(planner_id);
CREATE INDEX idx_planner_sources_order ON planner_sources(planner_id, display_order);

CREATE TABLE planner_runs (
    id BIGSERIAL PRIMARY KEY,
    planner_source_id BIGINT NOT NULL REFERENCES planner_sources(id) ON DELETE CASCADE,
    run_name_id BIGINT REFERENCES run_names(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_planner_runs_source ON planner_runs(planner_source_id);
CREATE INDEX idx_planner_runs_order ON planner_runs(planner_source_id, display_order);

CREATE TABLE planner_reports (
    id BIGSERIAL PRIMARY KEY,
    planner_source_id BIGINT NOT NULL REFERENCES planner_sources(id) ON DELETE CASCADE,
    report_type_id BIGINT REFERENCES report_types(id) ON DELETE RESTRICT,
    report_name_id BIGINT REFERENCES report_names(id) ON DELETE RESTRICT,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_planner_reports_source ON planner_reports(planner_source_id);
CREATE INDEX idx_planner_reports_order ON planner_reports(planner_source_id, display_order);

-- ============================================
-- Trigger Functions for Updated Timestamps
-- ============================================

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply triggers to all tables with updated_at
CREATE TRIGGER update_source_names_updated_at BEFORE UPDATE ON source_names
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_run_names_updated_at BEFORE UPDATE ON run_names
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_report_types_updated_at BEFORE UPDATE ON report_types
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_report_names_updated_at BEFORE UPDATE ON report_names
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_funds_updated_at BEFORE UPDATE ON funds
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_fund_aliases_updated_at BEFORE UPDATE ON fund_aliases
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_ext_conn_updated_at BEFORE UPDATE ON external_connections
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_planners_updated_at BEFORE UPDATE ON planners
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_planner_sources_updated_at BEFORE UPDATE ON planner_sources
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_planner_runs_updated_at BEFORE UPDATE ON planner_runs
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_planner_reports_updated_at BEFORE UPDATE ON planner_reports
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- Comments for Documentation
-- ============================================

COMMENT ON TABLE external_connections IS 'External system connection configurations with authentication details';
COMMENT ON COLUMN external_connections.value_field IS 'Sensitive authentication value - masked in UI after initial entry';
COMMENT ON COLUMN external_connections.value_field_set IS 'Flag to prevent modification of value_field after initial set';
COMMENT ON COLUMN external_connections.is_default IS 'Only one connection can be default at a time';

COMMENT ON TABLE planners IS 'Main planner configurations for data processing workflows';
COMMENT ON TABLE planner_funds IS 'Many-to-many relationship between planners and funds with optional aliases';
COMMENT ON TABLE planner_sources IS 'Data sources associated with planners';
COMMENT ON TABLE planner_runs IS 'Run configurations for planner sources';
COMMENT ON TABLE planner_reports IS 'Report configurations for planner sources';
