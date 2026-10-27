INSERT INTO users (id, username, password, roles, created_at)
SELECT id,
       'demo-user-' || LPAD(id::text, 2, '0'),
       '',
       'ROLE_USER',
       CURRENT_TIMESTAMP
FROM generate_series(1, 5) AS ids(id);

INSERT INTO projects (id, name, owner_id, created_at)
SELECT id,
       'Sample project ' || LPAD(id::text, 2, '0'),
       ((id - 1) / 5) + 1,
       CURRENT_TIMESTAMP
FROM generate_series(1, 25) AS ids(id);

INSERT INTO issues (id, title, description, project_id, reporter_id, created_at)
SELECT id,
       'Sample issue ' || LPAD(id::text, 2, '0'),
       CASE id % 5
           WHEN 0
               THEN 'The application returns an unexpected result when this workflow is repeated. Please investigate and confirm the expected behavior.'
           WHEN 1
               THEN 'Users cannot complete the selected action. Review the validation flow and provide a clear error message.'
           WHEN 2
               THEN 'Important details are hidden on smaller screens. Ensure the content remains readable across screen sizes.'
           WHEN 3
               THEN 'The endpoint response differs from the documented behavior. Reproduce with the sample request and correct it.'
           ELSE 'The page slows down after several updates. Check for redundant queries and verify performance after the fix.'
           END,
       ((id - 1) / 5) + 1,
       1,
       CURRENT_TIMESTAMP
FROM generate_series(1, 125) AS ids(id);

SELECT setval('users_seq', (SELECT MAX(id) FROM users));
SELECT setval('projects_seq', (SELECT MAX(id) FROM projects));
SELECT setval('issues_seq', (SELECT MAX(id) FROM issues));

INSERT INTO public.users
VALUES (CURRENT_TIMESTAMP, nextval('users_seq'),
        '$2a$10$Suh0JMLAUk5E5AHPcmzYmumbPzzTKbTKCwrBm/XtnEM9ZbLqe8ln6',
        'ROLE_USER',
        'user');
