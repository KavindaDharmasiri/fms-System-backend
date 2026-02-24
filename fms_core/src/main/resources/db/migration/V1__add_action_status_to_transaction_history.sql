-- Add action_status column to store the reaction template action (BLOCK, ALLOW, SEND_MESSAGE, etc.)
ALTER TABLE transaction_history ADD COLUMN action_status VARCHAR(50) DEFAULT 'ALLOW';
ALTER TABLE transaction_history ADD COLUMN reaction_template_name VARCHAR(255);
ALTER TABLE transaction_history ADD COLUMN sms_enabled VARCHAR(10);
ALTER TABLE transaction_history ADD COLUMN email_enabled VARCHAR(10);
ALTER TABLE transaction_history ADD COLUMN frm_enabled VARCHAR(10);
ALTER TABLE transaction_history ADD COLUMN manual_review_status VARCHAR(50);
ALTER TABLE transaction_history ADD COLUMN manual_review_reason TEXT;
ALTER TABLE transaction_history ADD COLUMN reviewed_by VARCHAR(100);
ALTER TABLE transaction_history ADD COLUMN reviewed_at TIMESTAMP;

-- Update existing records based on current status
UPDATE transaction_history 
SET action_status = CASE 
    WHEN status = 'HIGH' THEN 'BLOCKED'
    WHEN status = 'MID' THEN 'REVIEW'
    ELSE 'ALLOWED'
END;
