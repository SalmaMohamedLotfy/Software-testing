CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE
);

-- Insert initial data
INSERT INTO users (name, email)
VALUES 
    ('Salma Mohamed', 'salmamohamed3511@gmail.com'),
    ('Ahmed Ali', 'ahmed.ali@example.com');


SELECT * FROM users;
UPDATE users
SET name = 'Sarah'
WHERE name = 'Salma Mohamed';


SELECT * FROM users;
ALTER TABLE users
ADD COLUMN phone_number VARCHAR(15);

-- Update phone number for Sarah
UPDATE users
SET phone_number = '123-456-7890'
WHERE name = 'Sarah';

-- Update phone number for Ahmed Ali
UPDATE users
SET phone_number = '+44 7911 123456'
WHERE name = 'Ahmed Ali';

-- Select all data to verify phone numbers
SELECT * FROM users;


-- Delete user with name Sarah
DELETE FROM users WHERE name = 'Sarah';

-- Verify the delete operation
SELECT * FROM users;
DROP TABLE users;
