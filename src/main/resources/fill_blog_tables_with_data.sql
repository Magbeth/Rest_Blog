drop extension if exists pgcrypto;
create extension pgcrypto;

insert into blog_user (username, first_name, last_name, password, email, created_at, enabled)
values ('admin', 'name2', 'lastname', crypt('000000', gen_salt('bf', 10)), 'admin@email.com', current_timestamp, true);

insert into articles (created_at, full_text, status, title, author_id)
values (current_timestamp, 'full text article', 'PUBLIC', 'Test PUBLIC article', (Select id from blog_user where username='admin'));

insert into articles (created_at, full_text, status, title, author_id)
values (current_timestamp, 'full text article', 'DRAFT', 'Test DRAFT article', (Select id from blog_user where username='admin'));

insert into comments (message, created_at, author_id, article_id)
values ('comment message 1', current_timestamp, (Select id from blog_user where username='admin'), (select id from articles where title='Test PUBLIC article') );

insert into comments (message, created_at, author_id, article_id)
values ('comments message 2', current_timestamp, (Select id from blog_user where username='admin'), (select id from articles where title='Test DRAFT article'));

insert into tags (name)
values ('tag_1');

insert into tags(name)
values ('tag_2');

insert into articles_tags(article_id, tag_id)
values ((select id from articles where title='Test PUBLIC article'), (select id from tags where name='tag_1'));

insert into articles_tags(article_id, tag_id)
values ((select id from articles where title='Test DRAFT article'), (select id from tags where name='tag_2'));

insert into articles_tags(article_id, tag_id)
values ((select id from articles where title='Test PUBLIC article'), (select id from tags where name='tag_2'));
