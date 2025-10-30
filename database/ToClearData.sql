--To clear All the Tables in the Database

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate tables in order (child tables first)
TRUNCATE TABLE Guest_Phone;
TRUNCATE TABLE Guest;
TRUNCATE TABLE Reservation;
TRUNCATE TABLE Staff_Phone;
TRUNCATE TABLE Staff;
TRUNCATE TABLE Department;
TRUNCATE TABLE Room;
TRUNCATE TABLE RoomType;
TRUNCATE TABLE Hotel_Contact;
TRUNCATE TABLE Hotel;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
