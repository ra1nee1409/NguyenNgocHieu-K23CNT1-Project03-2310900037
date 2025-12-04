-- Fix order status values to match Java enum (uppercase)
UPDATE orders SET status = 'PENDING' WHERE LOWER(status) = 'pending';
UPDATE orders SET status = 'CONFIRMED' WHERE LOWER(status) = 'confirmed';
UPDATE orders SET status = 'SHIPPING' WHERE LOWER(status) = 'shipping' OR LOWER(status) = 'shipped';
UPDATE orders SET status = 'DELIVERED' WHERE LOWER(status) = 'delivered';
UPDATE orders SET status = 'COMPLETED' WHERE LOWER(status) = 'completed';
UPDATE orders SET status = 'CANCELLED' WHERE LOWER(status) = 'cancelled';

-- Verify the update
SELECT id, status, created_at FROM orders ORDER BY created_at DESC;
