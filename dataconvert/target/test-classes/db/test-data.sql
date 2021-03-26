insert into employee_user(ID, VERSION, USERNAME, PASSWORD, START, USERROLE, AKTIV)
values(1, 0, 'testUserName_1', '12345', '2020-12-12', 'DEVELOPER', 'false');

insert into employee_user(ID, VERSION, USERNAME, PASSWORD, START, USERROLE, AKTIV)
values(2, 0, 'testUserName_2', '12345', '2020-12-12', 'DEVELOPER', 'false');

insert into employee(ID, VERSION, FIRSTNAME, LASTNAME, BIRTHDATE, JOBSTARTINTHECOMPANY, COMPANYAFFILIATION,DESCRIPTION, PHOTO, WEBSITE, USER_ID)
values(1, 0, 'Test1_First_Name', 'Test1_Last_Name', '1985-06-05', '2032-10-10', 0, 'Test1_description', null, 'http://test1.com/', 1);