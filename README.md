# Lost & Found: desktop app for lost and found items

A Java desktop application where users declare lost or found items and the app suggests possible matches between them. An admin dashboard reviews items and matches.

## Features

- **Accounts and roles:** registration and login, normal users and administrators
- **Items:** declare a lost or found item with a description, location and photo
- **Automatic matching:** a match score between lost and found items based on text similarity of their descriptions and locations (Levenshtein distance)
- **Admin dashboard:** review items, matches and users

## Architecture

MVC with a DAO layer:

```
view/        Swing screens (login, register, user dashboard, admin dashboard)
controller/  Screen logic
service/     Business rules and Levenshtein matching
dao/         Database access (JDBC)
model/       User, NormalUser, Admin, Item, Match
config/      MySQL connection
```

## Tech stack

Java · Swing · JDBC · MySQL

## Run it

1. Create a MySQL database and update the credentials in `src/com/lostandfound/config/DBConnection.java`.
2. Add the MySQL JDBC driver to the classpath.
3. Run `src/com/lostandfound/App.java`.
