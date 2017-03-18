CREATE TABLE ItemInfo (
	id serial NOT NULL,
	type varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	identifier varchar(255) NOT NULL,
	CONSTRAINT ItemInfo_pk PRIMARY KEY (id),
	CONSTRAINT ItemInfo_unique UNIQUE(name, identifier)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Item (
	id serial NOT NULL,
	iteminfo_id integer NOT NULL,
	location varchar(255) NOT NULL,
	date TIMESTAMP NOT NULL,
	expiration TIMESTAMP,
	deleted BOOLEAN NOT NULL DEFAULT 'false',
	CONSTRAINT Item_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE NutritionalInfo (
	iteminfo_id integer NOT NULL,
	energy FLOAT NOT NULL,
	carbohydrate FLOAT NOT NULL,
	fat FLOAT NOT NULL,
	protein FLOAT NOT NULL
) WITH (
  OIDS=FALSE
);



CREATE TABLE ItemInfoTag (
	id serial NOT NULL,
	iteminfo_id integer NOT NULL,
	key varchar(255) NOT NULL,
	value varchar(255) NOT NULL,
	CONSTRAINT ItemInfoTag_pk PRIMARY KEY (id),
	CONSTRAINT ItemInfoTag_unique UNIQUE(iteminfo_id, key, value)
) WITH (
  OIDS=FALSE
);



CREATE TABLE ListItem (
	iteminfo_id integer NOT NULL,
	shoppinglist_id integer NOT NULL,
	amount integer NOT NULL
) WITH (
  OIDS=FALSE
);



CREATE TABLE ShoppingList (
	id serial NOT NULL,
	name varchar(255) NOT NULL,
	date TIMESTAMP NOT NULL,
	deleted BOOLEAN NOT NULL DEFAULT 'false',
	CONSTRAINT ShoppingList_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE ItemSpecificTag (
	id serial NOT NULL,
	item_id integer NOT NULL,
	key varchar(255) NOT NULL,
	value varchar(255) NOT NULL,
	CONSTRAINT ItemSpecificTag_pk PRIMARY KEY (id),
	CONSTRAINT ItemSpecificTag_unique UNIQUE(item_id, key, value)
) WITH (
  OIDS=FALSE
);



CREATE TABLE Loan (
	users_id varchar(255) NOT NULL,
	item_id integer NOT NULL,
	date TIMESTAMP NOT NULL,
	returned TIMESTAMP,
	isreturned BOOLEAN NOT NULL DEFAULT 'false',
	deleted BOOLEAN NOT NULL DEFAULT 'false'
) WITH (
  OIDS=FALSE
);



CREATE TABLE Users (
	id varchar(255) NOT NULL,
	name varchar(255) NOT NULL UNIQUE,
	email varchar(255) UNIQUE,
	password varchar(255) NOT NULL,
	apikey varchar(255) UNIQUE,
	date TIMESTAMP NOT NULL,
	deleted BOOLEAN NOT NULL DEFAULT 'false',
	CONSTRAINT Users_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);



CREATE TABLE SessionControl (
	sessionid varchar(255) NOT NULL,
	users_id varchar(255) NOT NULL,
	date TIMESTAMP NOT NULL,
	CONSTRAINT SessionControl_pk PRIMARY KEY (sessionid)
) WITH (
  OIDS=FALSE
);



CREATE TABLE BugReport (
	users_id varchar(255) NOT NULL,
	subject varchar(255) NOT NULL,
	description TEXT NOT NULL,
	date TIMESTAMP NOT NULL
) WITH (
  OIDS=FALSE
);


CREATE TABLE Meal (
	id serial NOT NULL,
	name varchar(255) NOT NULL,
	mass DECIMAL NOT NULL,
	date TIMESTAMP NOT NULL,
	deleted BOOLEAN NOT NULL DEFAULT 'false',
	users_id varchar(255) NOT NULL,
	CONSTRAINT Meal_pk PRIMARY KEY (id)
) WITH (
  OIDS=FALSE
);

CREATE TABLE MealComponent (
	meal_id integer NOT NULL,
	iteminfo_id integer NOT NULL,
	fraction FLOAT NOT NULL
) WITH (
  OIDS=FALSE
);



CREATE TABLE AccessControl (
	item_id integer NOT NULL,
	users_id varchar(255) NOT NULL,
	level integer NOT NULL,
	CONSTRAINT AccessControl_constraint UNIQUE(item_id, users_id)
) WITH (
  OIDS=FALSE
);




ALTER TABLE Item ADD CONSTRAINT Item_fk0 FOREIGN KEY (iteminfo_id) REFERENCES ItemInfo(id);

ALTER TABLE NutritionalInfo ADD CONSTRAINT NutritionalInfo_fk0 FOREIGN KEY (iteminfo_id) REFERENCES ItemInfo(id);

ALTER TABLE ItemInfoTag ADD CONSTRAINT ItemInfoTag_fk0 FOREIGN KEY (iteminfo_id) REFERENCES ItemInfo(id);

ALTER TABLE ListItem ADD CONSTRAINT ListItem_fk0 FOREIGN KEY (iteminfo_id) REFERENCES ItemInfo(id);

ALTER TABLE ListItem ADD CONSTRAINT ListItem_fk1 FOREIGN KEY (shoppinglist_id) REFERENCES ShoppingList(id);


ALTER TABLE ItemSpecificTag ADD CONSTRAINT ItemSpecificTag_fk0 FOREIGN KEY (item_id) REFERENCES Item(id);

ALTER TABLE Loan ADD CONSTRAINT Loan_fk0 FOREIGN KEY (users_id) REFERENCES Users(id);

ALTER TABLE Loan ADD CONSTRAINT Loan_fk1 FOREIGN KEY (item_id) REFERENCES Item(id);


ALTER TABLE SessionControl ADD CONSTRAINT SessionControl_fk0 FOREIGN KEY (users_id) REFERENCES Users(id);

ALTER TABLE BugReport ADD CONSTRAINT BugReport_fk0 FOREIGN KEY (users_id) REFERENCES Users(id);

ALTER TABLE Meal ADD CONSTRAINT Meal_fk0 FOREIGN KEY (users_id) REFERENCES Users(id);

ALTER TABLE MealComponent ADD CONSTRAINT MealComponent_fk0 FOREIGN KEY (meal_id) REFERENCES Meal(id);

ALTER TABLE MealComponent ADD CONSTRAINT MealComponent_fk1 FOREIGN KEY (iteminfo_id) REFERENCES ItemInfo(id);

ALTER TABLE AccessControl ADD CONSTRAINT AccessControl_fk0 FOREIGN KEY (item_id) REFERENCES Item(id);

ALTER TABLE AccessControl ADD CONSTRAINT AccessControl_fk1 FOREIGN KEY (users_id) REFERENCES Users(id);

