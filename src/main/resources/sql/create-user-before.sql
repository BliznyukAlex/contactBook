--delete from user_roles;
--delete from user;

insert into user(id, user_name, password) values
(1, 'user', '$2a$10$XlpTkvfWciT8TqP8x7s7ruex0Pgm.37OnIaibgGfG1ueuAQqvK.oC'),
(2, 'admin', '$2a$10$XlpTkvfWciT8TqP8x7s7ruex0Pgm.37OnIaibgGfG1ueuAQqvK.oC');

insert into user_roles(user_id, roles_id) values
(1, 1),
(2, 2);
