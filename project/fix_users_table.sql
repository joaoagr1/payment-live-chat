-- Fix users table to allow null email and username
-- Run this in your PostgreSQL database

-- Option 1: If you have no important data, drop and recreate
-- DROP TABLE IF EXISTS users CASCADE;

-- Option 2: Alter existing table (RECOMMENDED)
ALTER TABLE users ALTER COLUMN email DROP NOT NULL;
ALTER TABLE users ALTER COLUMN username DROP NOT NULL;

-- Optional: Clean up any test users with null values
-- DELETE FROM users WHERE email IS NULL OR username IS NULL;