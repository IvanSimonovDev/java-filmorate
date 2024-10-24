-- Initializing table ratings

MERGE INTO ratings
KEY (id, name)
VALUES (1, 'G');

MERGE INTO ratings
KEY (id, name)
VALUES (2, 'PG');

MERGE INTO ratings
KEY (id, name)
VALUES (3, 'PG_13');

MERGE INTO ratings
KEY (id, name)
VALUES (4, 'R');

MERGE INTO ratings
KEY (id, name)
VALUES (5, 'NC_17');

-- Initializing table genres

MERGE INTO genres
KEY (id, name)
VALUES (1, 'COMEDY');

MERGE INTO genres
KEY (id, name)
VALUES (2, 'DRAMA');

MERGE INTO genres
KEY (id, name)
VALUES (3, 'CARTOON');

MERGE INTO genres
KEY (id, name)
VALUES (4, 'THRILLER');

MERGE INTO genres
KEY (id, name)
VALUES (5, 'DOCUMENTARY');

MERGE INTO genres
KEY (id, name)
VALUES (6, 'ACTION_FILM');
