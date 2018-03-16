INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'盖伦',18,'北京');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'卢姥爷',17,'上海');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'蛇哥',19,'广州');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'PG1',16,'深圳');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'阿怡',15,'杭州');
INSERT INTO person(id,name,age,address) VALUES (hibernate_sequence.nextval,'天佑',20,'杭州');
COMMIT ;
DELETE FROM PERSON;