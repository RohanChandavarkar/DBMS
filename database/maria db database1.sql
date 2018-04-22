-- create statements
-- location(costfactor) table creation
create table location(
city varchar(20), 
costfactor float(2) not null, 
constraint location_pk primary key(city));

-- hotel(hotel_id,name,phone_no,address,city) creation
create table hotel(
hotel_id int(20) auto_increment,
name varchar(50) not null,
phone_no bigint(10) not null,
address varchar(50) not null,
city varchar(20) not null,
constraint hotel_pk primary key(hotel_id),
constraint hotel_location_fk foreign key(city) references location(city)on delete cascade
);

-- staff(staff_id, name, phone_no, title, dob, hotel_id) creation
create table staff(
staff_id int(50) auto_increment,
name varchar(50) not null,
phone_no bigint(10),
title varchar(50) not null,
dob date,
hotel_id int(20) not null,
constraint staff_pk primary key(staff_id),
constraint staff_hotel_fk foreign key(hotel_id) references hotel(hotel_id) on delete cascade
);


-- catering staff creation
create table catering_staff(
staff_id int(50),
constraint catering_staff_pk primary key(staff_id),
constraint catering_staff_fk foreign key(staff_id) references staff(staff_id)on delete cascade
);

-- service staff creation
create table service_staff(
staff_id int(50),
constraint room_service_pk primary key(staff_id),
constraint room_staff_fk foreign key(staff_id) references staff(staff_id)on delete cascade
);

-- occupancy creation
create table occupancy(
max_limit int(2),
constraint occupancy_pk primary key(max_limit)
);

-- category creation
create table category(
category_name varchar(50),
constraint category_pk primary key(category_name)
);

-- cost creation
create table cost(
max_limit int(2),
category_name varchar(20),
costfactor float(2) not null,
constraint cost_pk primary key(max_limit,category_name),
constraint cost_occupancy_fk foreign key(max_limit) references occupancy(max_limit)on delete cascade,
constraint cost_category_fk foreign key(category_name) references category(category_name)on delete cascade
);

-- room creation
create table room(
room_no int(10),
hotel_id int(20),
availability bit not null,
max_limit int(2) not null,
category_name varchar(50) not null,
rate float(2) not null,
constraint room_pk primary key(room_no, hotel_id),
constraint room_hotel_fk foreign key(hotel_id) references hotel(hotel_id)on delete cascade,
constraint room_occupancy_fk foreign key(max_limit) references cost(max_limit)on delete cascade,
constraint room_category_fk foreign key(category_name) references cost(category_name)on delete cascade
);

-- presidential creation
create table presidential(
room_no int(10),
hotel_id int(20),
catering_staff_id int(20),
service_staff_id int(20),
constraint presidential_pk primary key(hotel_id,room_no),
constraint presidential_room_fk foreign key(room_no,hotel_id) references room(room_no, hotel_id)on delete cascade,
constraint presidential_catering_fk foreign key(catering_staff_id) references catering_staff(staff_id),
constraint presidential_service_fk foreign key(service_staff_id) references service_staff(staff_id)
);

-- card creation
create table card(
card_no bigint(16),
validity date not null,
name varchar(20) not null,
max_limit float(10,2) not null,
balance float(10,2) not null,
cvv int(3) not null,
constraint card_pk primary key(card_no)
);

-- create services
create table services(
service_name varchar(20),
charges float(2) not null,
period varchar(20) not null,
constraint services_pk primary key(service_name)
);

-- offers creation
create table offers(
category_name varchar(20),
service_name varchar(20),
constraint offers_pk primary key(category_name,service_name),
constraint offers_category_fk foreign key(category_name) references category(category_name) on delete cascade,
constraint offers_service_fk foreign key(service_name) references services(service_name) on delete cascade
);

-- create customer
create table customer(
customer_id int(20) auto_increment,
name varchar(50) not null,
dob date,
phone_number bigint(10) not null,
email varchar(30),
ssn int(9),
card_no bigint(16),
room_no int(10) not null,
hotel_id int(20) not null,
guest_count int(20) not null,
check_in time not null,
check_out time,
start_date date not null,
end_date date,
constraint customer_pk primary key(customer_id),
constraint customer_room_fk foreign key(room_no,hotel_id) references room(room_no,hotel_id)on delete cascade,
constraint customer_card_fk foreign key(card_no) references card(card_no)on delete set null
);

-- create bill

create table bill(
bill_id int(20) auto_increment,
customer_id int(20) not null,
bill_date date,
rate float(2),
amount float(2),
discount int(3),
total float(2),
payment_type varchar(20),
bill_address varchar(100),
constraint bill_pk primary key(bill_id),
constraint bill_customer_fk foreign key(customer_id) references customer(customer_id)on delete cascade
);

-- create uses
create table uses(
service_name varchar(20),
customer_id int,
times int,
amount float(2) not null,
constraint usagelog_pk primary key(service_name,customer_id),
constraint usage_service_fk foreign key(service_name) references services(service_name)on delete cascade,
constraint usage_customer_id foreign key(customer_id) references customer(customer_id)on delete cascade
);

-- create serves
create table serves(
staff_id int(20),
customer_id int(20),
constraint serves_pk primary key(staff_id,customer_id),
constraint serves_staff_fk foreign key(staff_id) references staff(staff_id)on delete cascade,
constraint serves_customer_fk foreign key(customer_id) references customer(customer_id)on delete cascade
);

-- trigger before insert into staff 

delimiter //
create trigger before_insert_staff
before insert on staff
for each row
begin 
if new.title='manager' then
if(exists(select * from staff where title='manager' and hotel_id=new.hotel_id)) then
signal sqlstate '45000' set message_text= 'A manager for this hotel is already present. To add a new manager delete the old manager';
end if;
end if;
end;

-- trigger after insert into staff
delimiter //
create trigger insert_staff
after insert on staff
for each row
begin 
if new.title='catering' then
insert into catering_staff values (new.staff_id);
end if;
if new.title='service' then
insert into service_staff values(new.staff_id);
end if;
end;

-- trigger before insert into room
delimiter //
create trigger insert_before_room
before insert on room
for each row
begin
set new.availability=1;
set new.rate = (select a.costfactor+b.costfactor from 
(select costfactor from location where city=(select city from hotel where hotel_id=new.hotel_id) )as a,
(select costfactor from cost where (category_name,max_limit)=(select category_name,max_limit 
from cost where category_name=new.category_name and max_limit=new.max_limit) )as b);
end;

-- trigger after insert into room
delimiter //
create trigger insert_after_room
after insert on room
for each row
begin
if new.category_name = 'presidential' then
insert into presidential(room_no,hotel_id) values(new.room_no, new.hotel_id);
end if;
end;

-- trigger after update on cost
delimiter //
create trigger update_cost
after update on cost 
for each row
begin
update room,(select room_no,hotel_id,location.costfactor+cost.costfactor as costfactor from room natural join hotel natural join location join cost using(max_limit,category_name) where cost.max_limit=new.max_limit and cost.category_name=new.category_name)as a
set rate=a.costfactor
where room.room_no=a.room_no and room.hotel_id=a.hotel_id;
end;



-- trigger after updating location
delimiter //
create trigger update_location
after update on location 
for each row
begin
update room,(select room_no,hotel_id,location.costfactor+cost.costfactor as costfactor from room natural join hotel natural join location join cost using(max_limit,category_name) where location.city=new.city)as a
set rate=a.costfactor
where room.room_no=a.room_no and room.hotel_id=a.hotel_id;
end;

-- trigger before update
delimiter //
create trigger update_room
before update on room
for each row
begin
if old.max_limit<> new.max_limit or old.category_name<>new.category_name then
set new.rate = (select a.costfactor+b.costfactor from 
(select costfactor from location where city=(select city from hotel where hotel_id=new.hotel_id) )as a,
(select costfactor from cost where (category_name,max_limit)=(select category_name,max_limit 
from cost where category_name=new.category_name and max_limit=new.max_limit) )as b);
end if;
if old.category_name='presidential' and
(old.category_name <> new.category_name) and
(exists(select * from presidential where room_no=new.room_no and hotel_id=new.hotel_id)) then
delete from presidential where room_no=new.room_no and hotel_id=new.hotel_id;
elseif (new.category_name='presidential') and
(not exists(select * from presidential where room_no=new.room_no and hotel_id=new.hotel_id))then
insert into presidential(room_no,hotel_id) values(new.room_no,new.hotel_id);
end if;
end;



-- trigger after customer_insert
delimiter //
create trigger insert_customer
after insert on customer
for each row
begin
declare price float;
select rate into price from room where room_no=new.room_no and hotel_id=new.hotel_id;
update room set availability=0 where room_no=new.room_no and hotel_id=new.hotel_id;
insert into bill (customer_id,rate) values(new.customer_id,price);
end;


delimiter //
create trigger before_insert_customer
before insert on customer
for each row
begin
if new.guest_count > (select max_limit from room where room_no=new.room_no and hotel_id=new.hotel_id) then
signal sqlstate '45000' set message_text='The guest count is more than the occupancy capacity of the room.';
end if;
end;


-- trigger before deletion of room
delimiter //
create trigger delete_room
before delete on room
for each row
begin 
if old.availability=0 then 
signal sqlstate '45000' set message_text= 'There is currently a customer in the room, try deleting after the customer checks out';
end if;
end;

-- trigger before customer delete
delimiter //
create trigger delete_customer
before delete on customer
for each row
begin 
if old.end_date is null then
signal sqlstate '45000' set message_text= 'the customer needs to checkout the room before deletion';
end if;
end;

-- update customer
delimiter //
create trigger update_customer
after update on customer
for each row
begin 
if new.end_date is not null and old.end_date is null then
update room set availability=1 where room_no=old.room_no and hotel_id=old.hotel_id;
update bill set bill_date=new.end_date where customer_id=new.customer_id;
if(exists(select * from presidential where room_no=old.room_no and hotel_id=old.hotel_id) )then
update presidential set catering_staff_id=null,service_staff_id=null where room_no=old.room_no and hotel_id=old.hotel_id;
end if;
end if;
end;

-- updation of presidential
delimiter //
create trigger update_presidential
before update on presidential
for each row
begin
if new.catering_staff_id is not null and new.catering_staff_id<>old.catering_staff_id then
if (not exists(select * from staff where staff_id=new.catering_staff_id and hotel_id=new.hotel_id)) then
signal sqlstate '45000' set message_text= 'choose the catering staff that works in the hotel the room is in';
end if;
end if;
if new.service_staff_id<> null and new.service_staff_id<>old.service_staff_id then
if (not exists(select * from staff where staff_id=new.service_staff_id and hotel_id=new.hotel_id)) then
signal sqlstate '45000' set message_text= 'choose the catering staff that works in the hotel the room is in';
end if;
end if;
end;

-- trigger before delete on staff
delimiter //
create trigger delete_staff
before delete on staff
for each row
begin
if (exists(select * from presidential where catering_staff_id=old.staff_id))
then  signal sqlstate '45000' set message_text= 'the staff is already assigned to serve a customer, delete after changing the status of staff';
elseif(exists(select * from presidential where service_staff_id=old.staff_id))
then  signal sqlstate '45000' set message_text= 'the staff is already assigned to serve a customer, delete after changing the status of staff';
end if;
end;

