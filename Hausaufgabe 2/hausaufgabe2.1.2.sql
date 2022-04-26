/* drop everything at the begining so the script
 * can run from begining to end every time */
drop schema public cascade;
create schema public;

/* Records */

create table contracts(
	contract_number serial primary key,
	contract_date timestamp not null,
	place varchar(50) not null
);

create table purchase_contracts(
	id serial primary key,
	installment_number int not null,
	interest_rate real not null,
	contract_number int not null
);

/* makes purchase_contracts a subclass of contracts by referenconmg */
alter table purchase_contracts 
	add constraint fk_purchase_contracts_contracts foreign key (contract_number) references contracts (contract_number) on delete cascade;

create table tenancy_contracts(
	id serial primary key,
	start_date timestamp not null,
	duration varchar(50) not null,
	additional_costs real not null,
	contract_number int not null
);

/* makes tenancy_contracts a subclass of contracts by referenconmg */
alter table tenancy_contracts 
	add constraint fk_tenancy_contracts_contracts foreign key (contract_number) references contracts (contract_number) on delete cascade;

create table persons(
	id serial primary key,
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	address varchar(50) not null
);

create table estate_agents(
	id serial primary key,
	name varchar(50) not null,
	address varchar(50) not null,
	login varchar(50) not null,
	password varchar(50) not null
);

create table estates(
	id serial primary key,
	city varchar(50) not null,
	postal_code int not null,
	street varchar(50) not null,
	street_number int not null,
	square_area int not null,
	agent_id int not null
);

alter table estates 
	add constraint fk_estate_estate_agents foreign key (agent_id) references estate_agents (id) on delete set null;

create table apartments(
	id serial primary key,
	floor int not null,
	rent real not null,
	rooms int not null,
	balcony int,
	built_in_kitchen bool not null,
	estate_id int not null
);

alter table apartments
	add constraint fk_apartments_estates foreign key (estate_id) references estates (id) on delete cascade;

create table houses(
	id serial primary key,
	floors int not null,
	price real not null,
	garden bool,
	estate_id int not null
);

alter table houses
	add constraint fk_houses_estates foreign key (estate_id) references estates (id) on delete cascade;

/* Relations */

create table rents(
	id serial primary key,
	tenancy_contracts_id int not null,
	apartment_id int not null,
	person_id int not null
);

alter table rents
	add constraint fk_rents_tenancy_contract foreign key (tenancy_contracts_id) references tenancy_contracts (id);

alter table rents
	add constraint fk_rents_apartment foreign key (apartment_id) references apartments (id) on delete cascade;

alter table rents
	add constraint fk_rents_person foreign key (person_id) references persons (id);

create table sells(
	id serial primary key,
	purchase_contract_id int not null,
	person_id int not null,
	house_id int not null
);

alter table sells
	add constraint fk_sells_purchase_contract foreign key (purchase_contract_id) references purchase_contracts (id);

alter table sells
	add constraint fk_sells_house foreign key (house_id) references houses (id) on delete cascade;

alter table sells
	add constraint fk_sells_person foreign key (person_id) references persons (id);

/* Initializing data */

--purchase contract
insert into contracts(contract_number, contract_date, place)
values (42, '2015-12-17', 'Hamburg');

insert into purchase_contracts(id, installment_number, interest_rate, contract_number)
values (16 ,3, 0.05, 42);

-- tenancy_contract
insert into contracts(contract_number, contract_date, place)
values (43, '2016-04-02', 'Berlin');

insert into tenancy_contracts(id, start_date, duration, additional_costs, contract_number)
values (12, '2014-05-11', '2 years 4 months', '400.000', 43);

insert into persons(id, first_name, last_name, address)
values (68, 'Janucha', 'Banucha', 'Superstreet 123');

insert into estate_agents(id, name, address, login, password)
values (70, 'Janucha', 'Superstreet 123', 'user_user', 'secret_password');

-- arpartment
insert into estates(id, city, postal_code, street, street_number, square_area, agent_id)
values (54, 'Hamburg', 223344, 'Evenbetterstreet', 321, 200, 70);

insert into apartments(id, floor, rent, rooms, balcony, built_in_kitchen, estate_id)
values (1, 3, '1020', 5, 2, true, 54);

insert into rents(id, tenancy_contracts_id, apartment_id, person_id)
values (2, 12, 1, 68);

-- house
insert into estates(id, city, postal_code, street, street_number, square_area, agent_id)
values (55, 'Berlin', 22222, 'Thebeststreet', 420, 250, 70);

insert into houses(id, floors, price, garden, estate_id)
values (1, 2, '530.000', false, 55);

insert into sells(id, purchase_contract_id, house_id, person_id)
values (1, 16, 1, 68);
