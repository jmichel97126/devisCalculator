INSERT INTO customer(firstname, lastname, signupdate) values ('Juergen','Hoeller', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Mark','Fisher', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Rod','Johnson', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('David','Syer', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Gunnar','Hillert', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Dave','McCrory', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Josh','Long', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Patrick','Chanezon', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Andy','Piper', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Eric','Bottard', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Chris','Richardson', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Raja','Rao', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Rajdeep','Dua', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Monica','Wilkinson', NOW() );
INSERT INTO customer(firstname, lastname, signupdate) values ('Mark','Pollack', NOW() );

INSERT INTO parameter(type, name, code) values ('TOS', 'Simple','simple');
INSERT INTO parameter(type, name, code) values ('TOS', 'Sold in batches','batches');
INSERT INTO parameter(type, name, code) values ('TOS', 'Sold by weight','weight');

INSERT INTO PRODUCT(name, price, updateDate, idType, promotion, promotionRate) values ('nom', 1.0, NOW(), 1, false, null);
INSERT INTO PRODUCT(name, price, updateDate, idType, promotion, promotionRate) values ('nom2', 1.0, NOW(), 1, true, 10);

	
