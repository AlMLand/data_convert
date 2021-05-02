insert into users(ID, VERSION, USERNAME, PASSWORD, START, AKTIV)
values(1, 0, 'testUserName_1', '12345', '2020-12-12', 'true');

insert into users(ID, VERSION, USERNAME, PASSWORD, START, AKTIV)
values(2, 0, 'testUserName_2', '12345', '2020-12-12', 'false');

insert into roles(ID, VERSION, ROLE)
values(1, 0, 'DEVELOPER');

insert into employees(ID, VERSION, FIRSTNAME, LASTNAME, BIRTHDATE, JOBSTARTINTHECOMPANY, COMPANYAFFILIATION, DESCRIPTION, PHOTO, WEBSITE, USER_ID)
values(1, 0, 'Test1_First_Name', 'Test1_Last_Name', '1985-06-05', '2032-10-10', 0, 'Test1_description', null, 'http://test1.com/', 1);

insert into users_roles(USER_FK, ROLE_FK) 
values(1, 1);