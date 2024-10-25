CREATE TABLE IF NOT EXISTS users (
    id integer PRIMARY KEY,
    name text,
    birthday date
);

CREATE TABLE IF NOT EXISTS emails (
    user_id integer PRIMARY KEY REFERENCES users ON DELETE CASCADE,
    email text NOT NULL
);

CREATE TABLE IF NOT EXISTS logins (
    user_id integer PRIMARY KEY REFERENCES users ON DELETE CASCADE,
    login text NOT NULL
);

CREATE TABLE IF NOT EXISTS friendships (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    fst_user_id integer REFERENCES users ON DELETE CASCADE,
    snd_user_id integer REFERENCES users ON DELETE CASCADE,
    accepted_by_second boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS ratings (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name text NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    id integer PRIMARY KEY,
    name text NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id integer PRIMARY KEY,
    name text NOT NULL,
    description text NOT NULL,
    release_date date NOT NULL,
    duration integer NOT NULL,
    rating_id integer REFERENCES ratings ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_genres (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id integer REFERENCES films ON DELETE CASCADE,
    genre_id integer REFERENCES genres ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id integer REFERENCES users ON DELETE CASCADE,
    film_id integer REFERENCES films ON DELETE CASCADE
);