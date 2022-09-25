-- 17/09/2022
---------------------------------------------------------------------
-- permissions
INSERT INTO permissions (permission_name, is_default, is_active, created_at, updated_at)
VALUES
('ADMIN', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('READ', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('WRITE', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- groups
INSERT INTO groups (group_name, is_active, created_at, updated_at)
VALUES
('ADMINISTRATORS', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USERS', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- users
-- admin hash pass default == 'my_password'
INSERT INTO users ("name", "password", email, is_active, created_at, updated_at, last_login_at)
VALUES
('Admin', '$2a$10$xYqp1JQLdit0NKVG416A2e4hLbyuXwkARSc120yp/7lFfHxqXZPs2', 'admin@maurohahn.me', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL),
('User', '$2a$10$xYqp1JQLdit0NKVG416A2e4hLbyuXwkARSc120yp/7lFfHxqXZPs2', 'user@maurohahn.me', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

-- permissions x groups
INSERT INTO permissions_groups (permission_id, group_id, created_at)
VALUES
(1, 1, CURRENT_TIMESTAMP),
(2, 1, CURRENT_TIMESTAMP),
(3, 1, CURRENT_TIMESTAMP);

-- users x groups
INSERT INTO users_groups (user_id, group_id, created_at)
VALUES(1, 1, CURRENT_TIMESTAMP);


