-- Initializing table ratings
MERGE INTO ratings
KEY (id, name)
VALUES (1, 'G');

MERGE INTO ratings
KEY (id, name)
VALUES (2, 'PG');

MERGE INTO ratings
KEY (id, name)
VALUES (3, 'PG-13');

MERGE INTO ratings
KEY (id, name)
VALUES (4, 'R');

MERGE INTO ratings
KEY (id, name)
VALUES (5, 'NC-17');

-- Initializing table genres

MERGE INTO genres
KEY (id, name)
VALUES (1, 'Комедия');

MERGE INTO genres
KEY (id, name)
VALUES (2, 'Драма');

MERGE INTO genres
KEY (id, name)
VALUES (3, 'Мультфильм');

MERGE INTO genres
KEY (id, name)
VALUES (4, 'Триллер');

MERGE INTO genres
KEY (id, name)
VALUES (5, 'Документальный');

MERGE INTO genres
KEY (id, name)
VALUES (6, 'Боевик');
