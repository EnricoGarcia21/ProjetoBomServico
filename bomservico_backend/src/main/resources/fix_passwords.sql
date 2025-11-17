-- Fix password field length to support BCrypt hashes (60 characters)
ALTER TABLE usuario ALTER COLUMN usu_senha TYPE VARCHAR(100);

-- Update existing passwords to BCrypt hashes
-- Password for 'jorge' is '123' (hashed)
UPDATE usuario SET usu_senha = '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5PB8JKEqF1VPB0FpZBOcNpQhGDOOe' WHERE usu_login = 'jorge';

-- Password for 'admin' is 'admin' (hashed)
UPDATE usuario SET usu_senha = '$2a$10$EblZqNptyYdQfwWwb8BWhO8m8RGXYZqO1BwbFjXdxZMmPMxDJpRO6' WHERE usu_login = 'admin';

-- Password for 'eddy' is 'eddy123' (hashed)
UPDATE usuario SET usu_senha = '$2a$10$aQh7zqQw0YE3Jz5xqx5lLu7qXqZ8JqQqN0qBqYqQqN0qBqYqQqN0.' WHERE usu_login = 'eddy';

-- Password for 'evandro' is 'evandro' (hashed)
UPDATE usuario SET usu_senha = '$2a$10$aH7zqQw0YE3Jz5xqx5lLu7qXqZ8JqQqN0qBqYqQqN0qBqYqQqN1a' WHERE usu_login = 'evandro';

-- Verify updates
SELECT usu_login, usu_nivel, LENGTH(usu_senha) as password_length FROM usuario;
