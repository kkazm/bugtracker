INSERT INTO users (id, username, password, roles, created_at)
SELECT id,
       'demo-user-' || LPAD(id::text, 2, '0'),
       '',
       'ROLE_USER',
       CURRENT_TIMESTAMP
FROM generate_series(1, 50) AS ids(id);

INSERT INTO projects (id, name, owner_id)
SELECT id,
       'Sample project ' || LPAD(id::text, 2, '0'),
       id
FROM generate_series(1, 50) AS ids(id);

INSERT INTO issues (id, name, project_id)
SELECT id,
       'Sample issue ' || LPAD(id::text, 2, '0'),
       id
FROM generate_series(1, 50) AS ids(id);

SELECT setval('users_seq', (SELECT MAX(id) FROM users));
SELECT setval('projects_seq', (SELECT MAX(id) FROM projects));
SELECT setval('issues_seq', (SELECT MAX(id) FROM issues));
