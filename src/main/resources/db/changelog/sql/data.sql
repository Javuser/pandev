insert into pandev (name, parent_id)
values ('Phone', null),
       ('Apple', (select id from pandev where name = 'Phone')),
       ('Samsung', (select id from pandev where name = 'Phone'));


insert into pandev (name, parent_id)
VALUES ('Samsung S25', (select id from pandev where name = 'Samsung')),
        ('Ipad', (select id from pandev where name = 'Apple')),
        ('Samsung S20', (select id from pandev where name = 'Samsung')),
       ('Iphone', (select id from pandev where name = 'Apple'));

insert into pandev (name, parent_id)
VALUES ('14', (select id from pandev where name = 'Iphone'));

insert into pandev (name, parent_id) values
('14 pro', (select id from pandev where name = '14')),
('14 pro max', (select id from pandev where name = '14'));


delete from pandev where parent_id = (select  id from pandev where name = 'Huawei')