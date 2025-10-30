-- 🏨 Create Database
DROP DATABASE IF EXISTS hotel_management;
CREATE DATABASE hotel_management;
USE hotel_management;

-- 🏨 Hotel Table
CREATE TABLE Hotel (
    Hotel_id INT AUTO_INCREMENT PRIMARY KEY,
    HotelName VARCHAR(100) NOT NULL,
    TotalRooms INT,
    Location VARCHAR(150)
);

-- ☎️ Hotel Contact Table
CREATE TABLE Hotel_Contact (
    Contact_id INT AUTO_INCREMENT PRIMARY KEY,
    Hotel_id INT,
    Contact VARCHAR(20),
    FOREIGN KEY (Hotel_id) REFERENCES Hotel(Hotel_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 🏠 Room Type Table
CREATE TABLE RoomType (
    RoomType_id INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(50),
    Rent DECIMAL(10,2)
);

-- 🚪 Room Table
CREATE TABLE Room (
    Hotel_id INT,
    Room_num INT,
    RoomType_id INT,
    Status ENUM('Available','Booked','CheckedOut') DEFAULT 'Available',
    PRIMARY KEY (Hotel_id, Room_num),
    FOREIGN KEY (Hotel_id) REFERENCES Hotel(Hotel_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (RoomType_id) REFERENCES RoomType(RoomType_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 👤 Guest Table
CREATE TABLE Guest (
    Guest_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    Age INT,
    Gender VARCHAR(10),
Password VARCHAR(100)
);

-- 📞 Guest Phone Table
CREATE TABLE Guest_Phone (
    Phone_id INT AUTO_INCREMENT PRIMARY KEY,
    Guest_id INT,
    Phone VARCHAR(20),
    FOREIGN KEY (Guest_id) REFERENCES Guest(Guest_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 🛏️ Reservation Table (NO STATUS HERE)
CREATE TABLE Reservation (
    Reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    Guest_id INT,
    Hotel_id INT,
    Room_num INT,
    Check_in DATE,
    Check_out DATE,
    FOREIGN KEY (Guest_id) REFERENCES Guest(Guest_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (Hotel_id, Room_num) REFERENCES Room(Hotel_id, Room_num)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 🏢 Department Table
CREATE TABLE Department (
    Dept_id INT AUTO_INCREMENT PRIMARY KEY,
    Dept_name VARCHAR(100),
    Hotel_id INT,
    FOREIGN KEY (Hotel_id) REFERENCES Hotel(Hotel_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 👷‍♂️ Staff Table
CREATE TABLE Staff (
    Staff_id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100),
    Gender VARCHAR(10),
    Salary DECIMAL(10,2),
    Dept_id INT,
    Hotel_id INT,
    FOREIGN KEY (Dept_id) REFERENCES Department(Dept_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (Hotel_id) REFERENCES Hotel(Hotel_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 📱 Staff Phone Table
CREATE TABLE Staff_Phone (
    Phone_id INT AUTO_INCREMENT PRIMARY KEY,
    Staff_id INT,
    Phone VARCHAR(20),
    FOREIGN KEY (Staff_id) REFERENCES Staff(Staff_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- 🧠 Triggers to Keep Room & Reservation Status in Sync
DELIMITER $$

-- When a reservation is made → Room becomes Booked
CREATE TRIGGER trg_after_reservation_insert
AFTER INSERT ON Reservation
FOR EACH ROW
BEGIN
    UPDATE Room
    SET Status = 'Booked'
    WHERE Hotel_id = NEW.Hotel_id AND Room_num = NEW.Room_num;
END$$

-- When a reservation is canceled (deleted) → Room becomes Available
CREATE TRIGGER trg_after_reservation_delete
AFTER DELETE ON Reservation
FOR EACH ROW
BEGIN
    UPDATE Room
    SET Status = 'Available'
    WHERE Hotel_id = OLD.Hotel_id AND Room_num = OLD.Room_num;
END$$

DELIMITER ;
