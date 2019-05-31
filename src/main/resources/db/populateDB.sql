
DELETE FROM users;

INSERT INTO users (id, name, email, password)
VALUES (1, 'User1', 'user1@yandex.ru', '{noop}password1'),
       (2, 'User2', 'user2@yandex.ru', '{noop}password2'),
       (3, 'User3', 'user3@yandex.ru', '{noop}password3');

INSERT INTO files (id, user_id, name, path, created, size, content_type)
VALUES (10, 1, '1.txt', '/parentFolder/1.txt',  '2019-05-30 10:00:00', 134, 'text/plain'),
       (11, 1, '2.txt', '/parentFolder/2.txt', '2019-05-30 10:01:00', 234, 'text/plain');