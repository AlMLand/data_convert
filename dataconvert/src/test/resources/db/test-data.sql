insert into user(id, version, username, password, start, userRole, aktiv)
values(2, 0, 'testUserName', '12345', '2020-12-12', 'DEVELOPER', 'false');

insert into employee(id, version, firstName, lastName, birthDate, jobStartInTheCompany, companyAffiliation, webSite, user_id)
values(1, 0, 'Test1_First_Name', 'Test1_Last_Name', '1985-06-05', '2018-10-10', 0, 'http://test1.com/', null);