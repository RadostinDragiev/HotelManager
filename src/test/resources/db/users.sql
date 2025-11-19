DELETE FROM users_roles;
DELETE FROM users;

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
        'testUser',
        '$2a$10$zBBuWHA6t9iqhDa07hzovubRvlf0LobOvX9a3JLi.cBOyOQUrbm1u',
        'testUser@example.com',
        'Test',
        'User',
        'TEST_USER',
        1,
        NULL,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'ADMINISTRATOR'
WHERE u.username = 'testUser';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'MANAGER'
WHERE u.username = 'testUser';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'RECEPTIONIST'
WHERE u.username = 'testUser';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'HOUSEKEEPING'
WHERE u.username = 'testUser';

INSERT INTO users_roles (user_id, role_id)
SELECT u.uuid, r.uuid
FROM users u
         JOIN roles r ON r.name = 'USER'
WHERE u.username = 'testUser';