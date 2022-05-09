/* Aufgabe 2b */
begin;
insert into sheet3 (id, name)
values (10, 'Melchior');
select * from sheet3;
commit;

/* Aufgabe 2c */
begin;
update sheet3
set name = 'Panni'
where id = 4;

update sheet3
set name = 'Rolbert'
where id = 3;
commit;

/* Aufgabe 2e
 * run this after running 2e from connection1*/

begin;
update sheet3second set name ='Foo' where id=1;
select pg_sleep(10);
update sheet3 set name ='Bar' where id=1;
commit;


