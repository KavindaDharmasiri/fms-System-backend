-- Fix missing columns in fms_element table
ALTER TABLE fms_element ADD COLUMN IF NOT EXISTS payment_network VARCHAR(50) AFTER operator;
ALTER TABLE fms_element ADD COLUMN IF NOT EXISTS validation_message TEXT AFTER validation;

-- Fix missing columns in transaction_history table
ALTER TABLE transaction_history ADD COLUMN IF NOT EXISTS block_reason VARCHAR(255);
ALTER TABLE transaction_history ADD COLUMN IF NOT EXISTS fraud_percentage DECIMAL(5,2);

-- Fix final_rule column size in fms_rule table
ALTER TABLE fms_rule MODIFY COLUMN final_rule TEXT;

-- Create audit_log table if not exists
CREATE TABLE IF NOT EXISTS audit_log (
    audit_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100),
    entity_id VARCHAR(100),
    entity_type VARCHAR(100),
    error_message TEXT,
    ip_address VARCHAR(50),
    new_values TEXT,
    old_values TEXT,
    request_id VARCHAR(100),
    session_id VARCHAR(100),
    status VARCHAR(50),
    timestamp DATETIME,
    user_agent VARCHAR(255),
    user_id VARCHAR(100)
);

-- Create geo_cache table
CREATE TABLE IF NOT EXISTS geo_cache (
    location VARCHAR(255) PRIMARY KEY,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
