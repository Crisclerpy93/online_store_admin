/*
	Create Database
*/
CREATE DATABASE IF NOT EXISTS `projectdb`;
USE `projectdb`;

/*
	Delete tables old tables
*/

DROP TABLE IF EXISTS Whislist, Cart_products, Carts, Products, Category, Users;

/*
	Create new tables
*/

CREATE TABLE `Users` ( -- Table to store the users
	`Mail` varchar(80) NOT NULL DEFAULT 'DFUSER',
	`Name` TEXT NOT NULL,
	`Surname` TEXT NOT NULL,
	`Phone` varchar(12),
	`Address` TEXT NOT NULL,
	`passHash` varchar(64) NOT NULL,
	`isSeller` BINARY NOT NULL,
	`Image` longblob,
	PRIMARY KEY (`Mail`)
);

CREATE TABLE `Products` ( -- Table to store the products
	`Id` INT NOT NULL AUTO_INCREMENT,
	`Name` varchar(100) NOT NULL,
	`Price` FLOAT NOT NULL,
	`shortDesc` varchar(200),
	`longDesc` longtext,
	`Image` longblob,
	`Stock` INT NOT NULL,
	`Seller` varchar(80) NOT NULL DEFAULT 'super',
	`Category` int NOT NULL,
	PRIMARY KEY (`Id`)
);

CREATE TABLE `Category` ( -- Category system, each one knows its name, id and parent id
	`ParentID` int,
	`catID` int NOT NULL AUTO_INCREMENT,
	`Name` varchar(50) NOT NULL,
	PRIMARY KEY (`catID`)
);

CREATE TABLE `Whislist` ( -- NxN reference table, stores the relation between users and products
	`Id` int NOT NULL AUTO_INCREMENT,
	`Product` int NOT NULL,
	`User` varchar(80) NOT NULL,
	PRIMARY KEY (`Id`)
);

CREATE TABLE `Cart_products` ( -- NxN reference table, stores the relation between carts and products
	`Id` bigint NOT NULL AUTO_INCREMENT,
	`Product` int(80) NOT NULL,
	`order` int NOT NULL,
	`Quantity` int NOT NULL,
	PRIMARY KEY (`Id`)
);

CREATE TABLE `Carts` ( -- Represents a cart, that might be completed or not
	`orderID` int NOT NULL AUTO_INCREMENT,
	`User` varchar(80) NOT NULL,
	`creation_date` DATE NOT NULL,
	`Order` BINARY NOT NULL,
	`order_date` DATE,
	`total_order` FLOAT NOT NULL,
	PRIMARY KEY (`orderID`)
);

/*
	Adding relations between tables
*/

-- Adding relation between products and users, using seller as FK 
ALTER TABLE `Products` ADD CONSTRAINT `Product_fk0` FOREIGN KEY (`Seller`) REFERENCES `Users`(`Mail`) ON UPDATE CASCADE ON DELETE CASCADE;

-- Adding realtion between a product and a category
ALTER TABLE `Products` ADD CONSTRAINT `Product_fk1` FOREIGN KEY (`Category`) REFERENCES `Category`(`catID`) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding NxN relation constraints to table storing N:N relation between products and users (Whishlist)
ALTER TABLE `Whislist` ADD CONSTRAINT `Whislist_fk0` FOREIGN KEY (`Product`) REFERENCES `Products`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Whislist` ADD CONSTRAINT `Whislist_fk1` FOREIGN KEY (`User`) REFERENCES `Users`(`Mail`) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding NxN relation constraints to table storing NxN relation between carts and products (Cart_products)
ALTER TABLE `Cart_products` ADD CONSTRAINT `Cart_products_fk0` FOREIGN KEY (`Product`) REFERENCES `Products`(`Id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `Cart_products` ADD CONSTRAINT `Cart_products_fk1` FOREIGN KEY (`order`) REFERENCES `Carts`(`orderID`) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding relation between carts and users
ALTER TABLE `Carts` ADD CONSTRAINT `Cart_fk0` FOREIGN KEY (`User`) REFERENCES `Users`(`Mail`) ON DELETE CASCADE ON UPDATE CASCADE;

/*
	Insert some test data
*/

-- Categories
-- Super categories
INSERT INTO `Category` VALUES (NULL, 1, 'Electronics');
INSERT INTO `Category` VALUES (NULL, 2, 'Clothes');
INSERT INTO `Category` VALUES (NULL, 3, 'Books');

-- Electonics sub categories
INSERT INTO `Category` VALUES (1, 4, 'Speakers');
INSERT INTO `Category` VALUES (1, 5, 'Computers');
INSERT INTO `Category` VALUES (4, 10, 'Wired');
INSERT INTO `Category` VALUES (4, 11, 'Wireless');
INSERT INTO `Category` VALUES (5, 12, 'PC');
INSERT INTO `Category` VALUES (5, 13, 'Apple');

-- Clothes sub categories
INSERT INTO `Category` VALUES (2, 6, 'Sport');
INSERT INTO `Category` VALUES (2, 7, 'Casual');
INSERT INTO `Category` VALUES (6, 14, 'Trousers');
INSERT INTO `Category` VALUES (6, 15, 'Trainers');
INSERT INTO `Category` VALUES (7, 16, 'T-Shirts');
INSERT INTO `Category` VALUES (7, 17, 'Swimsuit');

-- Books sub categories
INSERT INTO `Category` VALUES (3, 8, 'Novel');
INSERT INTO `Category` VALUES (3, 9, 'Education');
INSERT INTO `Category` VALUES (8, 18, 'Mistery');
INSERT INTO `Category` VALUES (8, 19, 'Action');
INSERT INTO `Category` VALUES (9, 20, 'Computer Science');
INSERT INTO `Category` VALUES (9, 21, 'Biology');

-- Users
INSERT INTO `Users` VALUES('DFUSER', 'Default', 'User', NULL, 'DFUSER', '362ac1f11d1ae433d55261f53089ba1475af2ad93bc5fbd6e98763be11ff7f7e', true, NULL); -- Pass: defaultpwd
INSERT INTO `Users` VALUES('seller@sellers.com', 'Pedro', 'Salinas', '699699699', 'Los Molinos, 12\n15400 Pueblito (Cuenca)', '362ac1f11d1ae433d55261f53089ba1475af2ad93bc5fbd6e98763be11ff7f7e', true, NULL); -- Pass: defaultpwd
INSERT INTO `Users` VALUES('user@users.com', 'Juan', 'Lopez', '122344566', 'Los Molinos, 12\n15400 Pueblito (Cuenca)', '362ac1f11d1ae433d55261f53089ba1475af2ad93bc5fbd6e98763be11ff7f7e', false, NULL); -- Pass: defaultpwd

-- Products, id to null, auto increment is enabled
INSERT INTO `Products` VALUES(NULL, 'AirPods UltraSuper', 28.95, 'Cheap, wireless, fancy Headphones', 'Desinged by Apple in California. Assambled in China. 2 Year Warranty.', NULL,5000, 'DFUSER', 11);
INSERT INTO `Products` VALUES(NULL, 'JBL Headphones', 18.95, 'Cheap, with micro, fancy Headphones', 'Sportive headphones, Best audio quality.\n2 Year Warranty.', NULL,4567, 'seller@sellers.com', 10);
INSERT INTO `Products` VALUES(NULL, 'MacBook Pro 13\'\'', 3228.95, 'Best Apple Laptop, perfect for bussiness and education.', 'Desinged by Apple in California. Assambled in China. 2 Year Warranty. FinalCut Pro 2 included',NULL,350, 'seller@sellers.com', 13);
INSERT INTO `Products` VALUES(NULL, 'DELL XPS 15', 2238.95, 'Best Developer Laptop 2019', 'Comes with ubuntu installed. 1,2kgs. Slim, 4K Screen', NULL,450, 'DFUSER', 12);
INSERT INTO `Products` VALUES(NULL, 'Adidas XBT', 58.95, 'Our best trousers', 'Available in S,M,L,XL and XXL sizes.',NULL,567, 'seller@sellers.com', 14);
INSERT INTO `Products` VALUES(NULL, 'Nike Air', 88.95, 'Air Zoom Pegasus 33 Zapatilla Deportiva', 'El rendimiento legendario y la tecnología Premium se combinan para crear clásicos de Nike con amortiguación: las zapatillas para correr Zoom Pegasus 33 para mujer.', NULL,5000, 'seller@sellers.com', 15);
INSERT INTO `Products` VALUES(NULL, 'Murder on the Orient Express: A Hercule Poirot Mystery', 8.95, 'Famous book by Agatha Christie', 'Murder on the Orient Express, the most famous Hercule Poirot mystery, which has the brilliant detective hunting for a killer aboard one of the world’s most luxurious passenger trains.', NULL,3333, 'seller@sellers.com', 18);
INSERT INTO `Products` VALUES(NULL, 'Killer Queen', 58.95, 'Book by: Richad Castle', 'Inspired on march 1895 crimes in London.', NULL,112, 'seller@sellers.com', 19);
INSERT INTO `Products` VALUES(NULL, 'C++ A Modern Language', 33.95, 'IEEE, By Stroustrup Bjarne', 'Basic C++', NULL,5637, 'seller@sellers.com', 20);
INSERT INTO `Products` VALUES(NULL, 'El Origen de las Especies', 18.95, 'Charles Darwin.', 'Desde las increíbles variaciones de las especies hasta la dura lucha por la supervivencia, desde la bacteria más minúscula hasta el gran Árbol de la Vida', NULL,5607, 'seller@sellers.com', 21);

-- Whishlist, id to null, auto increment enabled
INSERT INTO `Whislist` VALUES(NULL, 1, 'user@users.com');
INSERT INTO `Whislist` VALUES(NULL, 2, 'user@users.com');
INSERT INTO `Whislist` VALUES(NULL, 3, 'user@users.com');
INSERT INTO `Whislist` VALUES(NULL, 1, 'seller@sellers.com');
INSERT INTO `Whislist` VALUES(NULL, 5, 'seller@sellers.com');
INSERT INTO `Whislist` VALUES(NULL, 6, 'user@users.com');

-- Creating two carts
INSERT INTO `Carts` VALUES(NULL, 'user@users.com','2018-7-04', false, NULL, 76.85);
INSERT INTO `Carts` VALUES(NULL, 'user@users.com', '2018-1-04', true,'2018-1-12', 9744.75);

-- Inserting Products
INSERT INTO `Cart_products` VALUES(NULL, 1,1, 2);
INSERT INTO `Cart_products` VALUES(NULL, 2,1, 1);
INSERT INTO `Cart_products` VALUES(NULL, 1,2, 2);
INSERT INTO `Cart_products` VALUES(NULL, 3,2, 3);