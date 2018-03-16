-- INSERT INTO SYS_USER (id,username,password) VALUES (1,'zyf','123');
-- INSERT INTO SYS_USER (id,username,password) VALUES (2,'change','456');
--
-- INSERT INTO SYS_ROLE (id,name) VALUES (1,'ROLE_ADMIN');
-- INSERT INTO SYS_ROLE (id,name) VALUES (2,'ROLE_USER');

-- zyf是admin
-- INSERT INTO SYS_USER_ROLES(SYS_USER_ID,ROLES_ID) VALUES (1,1);
-- change是user
-- INSERT INTO SYS_USER_ROLES(SYS_USER_ID,ROLES_ID) VALUES (2,2);