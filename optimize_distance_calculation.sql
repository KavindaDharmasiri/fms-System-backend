-- =====================================================
-- FMS Distance Calculation Optimization
-- Run this script to optimize transaction queries
-- =====================================================

-- 1. Add index on created_at for time-based filtering
CREATE INDEX IF NOT EXISTS idx_transaction_created_at 
ON transaction_history(created_at DESC);

-- 2. Add generated column for PAN prefix (MySQL 5.7+)
-- This allows efficient indexing of JSON data
ALTER TABLE transaction_history 
ADD COLUMN IF NOT EXISTS pan_prefix VARCHAR(12) 
GENERATED ALWAYS AS (
    SUBSTRING(JSON_UNQUOTE(JSON_EXTRACT(tran_packet, '$.pan')), 1, 12)
) STORED;

-- 3. Add composite index on pan_prefix and created_at
CREATE INDEX IF NOT EXISTS idx_transaction_pan_created 
ON transaction_history(pan_prefix, created_at DESC);

-- 4. Add index on transaction_history_id for faster lookups
CREATE INDEX IF NOT EXISTS idx_transaction_history_id 
ON transaction_history(transaction_history_id DESC);

-- 5. Optimize geo_cache table
CREATE INDEX IF NOT EXISTS idx_geo_cache_location 
ON geo_cache(location);

-- 6. Add index for location-based queries (if lat/lon columns exist)
-- CREATE INDEX IF NOT EXISTS idx_geo_cache_coords 
-- ON geo_cache(latitude, longitude);

-- =====================================================
-- Verify indexes were created
-- =====================================================
SHOW INDEX FROM transaction_history;
SHOW INDEX FROM geo_cache;

-- =====================================================
-- Performance Testing Query
-- Test the optimized query performance
-- =====================================================
EXPLAIN SELECT * FROM transaction_history t 
WHERE t.pan_prefix = '412370999900' 
AND t.created_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
ORDER BY t.created_at DESC 
LIMIT 10;

-- =====================================================
-- Cleanup old data (optional - run periodically)
-- =====================================================
-- DELETE FROM transaction_history 
-- WHERE created_at < DATE_SUB(NOW(), INTERVAL 90 DAY);

-- DELETE FROM geo_cache 
-- WHERE updated_at < DATE_SUB(NOW(), INTERVAL 30 DAY);
