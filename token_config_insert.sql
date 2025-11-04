-- Insert token configuration for ACCESS_TOKEN
INSERT INTO token_configuration (
    token_type, 
    secret_key, 
    expiration_time_in_seconds, 
    status, 
    version, 
    created_at, 
    updated_at, 
    created_by, 
    updated_by
) VALUES (
    'ACCESS_TOKEN',
    'mySecretKey123456789012345678901234567890',
    3600000,
    'ACTIVE',
    1,
    NOW(),
    NOW(),
    'SYSTEM',
    'SYSTEM'
);