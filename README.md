# java-filmorate
Template repository for Filmorate project.

# Filmorate project DB schema

![Filmorate project DB schema](./ER-diagram.png)

## Query for film with id = {id}

```SQL
SELECT f.id,
       f.name,
       f.description,
       f.release_date,
       f.duration,
       r.name,
       g.name
FROM films AS f
LEFT JOIN ratings AS r ON f.rating_id = r.id
LEFT JOIN films_genres AS fg ON f.id = fg.film_id 
LEFT JOIN genres AS g ON fg.genre_id = g.id 
WHERE id = {id};
```

## Query for all films

```SQL
SELECT f.id,
       f.name,
       f.description,
       f.release_date,
       f.duration,
       r.name,
       g.name
FROM films AS f
LEFT JOIN ratings AS r ON f.rating_id = r.id
LEFT JOIN films_genres AS fg ON f.id = fg.film_id 
LEFT JOIN genres AS g ON fg.genre_id = g.id;
```

## Query for user with id = {id}

```SQL
SELECT *
FROM users 
JOIN emails ON users.id = emails.user_id
JOIN logins ON users.id = logins.user_id
WHERE id = {id};
```

## Query for all users

```SQL
SELECT *
FROM users 
JOIN emails ON users.id = emails.user_id
JOIN logins ON users.id = logins.user_id;
```


