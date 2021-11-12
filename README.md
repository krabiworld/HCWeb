# HC WebSite
A simple site with authorization, registration, account manager and admin panel.

## Tech
- [Spring](https://spring.io/) - Application framework and inversion of control container for the Java platform.
- [PostgreSQL](https://www.postgresql.org/) - Free and open-source relational database management system emphasizing extensibility and SQL compliance.
- [Bootstrap](https://getbootstrap.com/) - Free and open-source CSS framework directed at responsive, mobile-first front-end web development.
- [Lombok](https://projectlombok.org/) - Java annotation library which helps to reduce boilerplate code.
- [Thymeleaf](https://www.thymeleaf.org/) - A modern server-side Java template engine for both web and standalone environments. Allows HTML to be correctly displayed in browsers and as static prototypes.

## Installation
1. Create in `src\main\resources` file `application.properties` with this content:
```properties
spring.datasource.url = jdbc:postgresql://URL/DB_NAME?serverTimezone=Europe/Moscow
spring.datasource.username = USERNAME
spring.datasource.password = PASSWORD
```
2. Create in your database this tables: `users` and `confirmation_tokens`:
```sql
create table users
(
    id       integer generated always as identity primary key,
    username text                         not null,
    password text                         not null,
    email    text                         not null,
    enabled  boolean default true         not null,
    role     text    default 'USER'::text not null
);
create table confirmation_tokens
(
    id    integer not null,
    token text    not null,
    type  text
);
```