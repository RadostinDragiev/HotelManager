INSERT INTO roles (uuid, name, created_date_time)
VALUES (UUID(), 'ADMINISTRATOR', CURRENT_TIMESTAMP),
       (UUID(), 'MANAGER', CURRENT_TIMESTAMP),
       (UUID(), 'RECEPTIONIST', CURRENT_TIMESTAMP),
       (UUID(), 'HOUSEKEEPING', CURRENT_TIMESTAMP),
       (UUID(), 'USER', CURRENT_TIMESTAMP);

INSERT INTO users (uuid,
                   username,
                   password,
                   email,
                   first_name,
                   last_name,
                   position,
                   is_enabled,
                   created_by,
                   created_date_time,
                   last_login_date_time)
VALUES (UUID(),
        'admin',
        '$2a$10$U0iC8SQSdbJ0DTUSp02r9e/wjHOo2P/JKjVkdxnniVbkVhMsOMhIe',
        'admin@example.com',
        'System',
        'Administrator',
        'SYSTEM_ADMIN',
        1,
        NULL,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'ADMINISTRATOR'
WHERE u.username = 'admin';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'MANAGER'
WHERE u.username = 'admin';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'RECEPTIONIST'
WHERE u.username = 'admin';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'HOUSEKEEPING'
WHERE u.username = 'admin';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'USER'
WHERE u.username = 'admin';
