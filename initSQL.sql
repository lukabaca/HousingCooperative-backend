CREATE DATABASE test CHARACTER SET utf8 COLLATE utf8_general_ci;
use  test;

insert into role values(1, 'ROLE_USER', 'Lokator'),
 (2,'ROLE_ADMIN', 'Administrator'), 
 (3, 'ROLE_MANAGER', 'Zarządca');
 insert into housingcooperative values (1, 'Teofilów');
 insert into measurementcost values (1, 2, 3, 4, 5);
 
 select * from appUser;
select * from activationtoken;

