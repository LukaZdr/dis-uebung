/* drop everything at the begining so the script
 * can run from begining to end every time */
begin;
drop schema public cascade;
create schema public;

/* Aufgabe 1 */

show transaction_isolation;
/* read committed */

create table sheet3 (
	id serial primary key,
	name varchar(50)
);

insert into sheet3 (id, name)
values (1, 'Luka'), (2, 'Jana'), (3, 'Herbert'), (4, 'Pannelore'), (5, 'Ingrid'), (6, 'Sonntrud'), (7, 'Baltasar');
commit;

begin;
select name from sheet3 where id = 2;
select relation::regclass, mode, granted from pg_locks where relation::regclass = 'sheet3'::regclass;
commit;

/* Read Commit: AccessShareLock */
/* Serializable: AccessShareLock, SIReadLock */

/* Aufgabe 2b */

begin;
select * from sheet3 where id > 2;
commit;

/* Aufgabe 2c */
begin;
update sheet3
set name = 'Norbert'
where id = 3;
commit;

/* Aufgabe 2e */
begin;
create table sheet3second (
	id serial primary key,
	name varchar(50)
);
commit;

/* run the code below and afterwards the code from 2e in
 * transaction2 (has to happen within in 10 seconds) 
 * transaction level: read committed for both transactions*/
begin;
create table sheet3second (
	id serial primary key,
	name varchar(50)
);
insert into sheet3second (id, name)
values (1, 'Luka');
commit;

begin;
update sheet3 set name ='Foo' where id=1;
select pg_sleep(10);
update sheet3second set name ='Bar' where id=1;
commit;