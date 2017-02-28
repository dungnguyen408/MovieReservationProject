DROP DATABASE IF EXISTS MovieReservation;
CREATE DATABASE MovieReservation;
USE MovieReservation;
   
CREATE TABLE Movie (
	mid INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    duration INT(5) NOT NULL,
    PRIMARY KEY (mid)
   );
   
CREATE TABLE Admin (
	aid INT NOT NULL AUTO_INCREMENT,
    uname VARCHAR(50) NOT NULL,
    fname char(50) NOT NULL,
    lname char(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    pw VARCHAR(10) NOT NULL,
    PRIMARY KEY (aid)
   );
   
CREATE TABLE RegUser(
	uid INT NOT NULL AUTO_INCREMENT,
    uname VARCHAR(50) NOT NULL,
    fname char(50)  NOT NULL,
    lname char(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    pw VARCHAR(10) NOT NULL,
    PRIMARY KEY (uid)
   );
   
CREATE TABLE TheaterRoom(
	trid INT NOT NULL AUTO_INCREMENT,
    seats INT NOT NULL,
    PRIMARY KEY (trid)
    );

CREATE TABLE Showtimes (
	mid INT NOT NULL AUTO_INCREMENT,
    trid INT NOT NULL,
    showtime TIME NOT NULL,
    date DATE DEFAULT '0001-01-01' NOT NULL,
    seats INT DEFAULT 0,
    updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mid) REFERENCES Movie(mid) ON DELETE CASCADE,
    FOREIGN KEY (trid) REFERENCES TheaterRoom(trid) ON DELETE CASCADE
  
   );

   
CREATE TABLE Reservation (
	rid INT NOT NULL AUTO_INCREMENT,
    uid INT NOT NULL,
    mid INT NOT NULL,
    PRIMARY KEY(rid),
    FOREIGN KEY (uid) REFERENCES RegUser(uid) ON DELETE CASCADE,
    FOREIGN KEY (mid) REFERENCES Movie(mid) ON DELETE CASCADE
	);
   
CREATE TABLE Archive(
  	mid INT NOT NULL ,
    trid INT NOT NULL,
    showtime TIME NOT NULL,
    date DATE NOT NULL,
    seats INT NOT NULL,
    updatedAt DATETIME NOT NULL
  );
  
DELIMITER $$
CREATE PROCEDURE archiveShowtime(
  cutoff DateTime
)
BEGIN
  INSERT INTO Archive
  SELECT * FROM Showtimes
  WHERE Showtimes.updatedAt <= cutoff;
  DELETE FROM Showtimes
  WHERE Showtimes.updatedAt <= cutoff;
END;
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER StampTime
  AFTER UPDATE ON Showtimes
  FOR EACH ROW
  BEGIN
    UPDATE Showtimes
    SET Showtimes.updatedAT = CURRENT_TIMESTAMP;
  END;
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER IncSeats
	AFTER INSERT ON Reservation
	FOR EACH ROW
	BEGIN
		UPDATE Showtimes
		SET Showtimes.seats = Showtimes.seats + 1
		WHERE Showtimes.mid = new.mid;
	END;
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER DecSeats
	AFTER DELETE ON Reservation
	FOR EACH ROW
	BEGIN
		UPDATE Showtimes
		SET Showtimes.seats = Showtimes.seats - 1
		WHERE Showtimes.mid = old.mid;
	END;
$$
DELIMITER ;

INSERT INTO TheaterRoom(seats) VALUES (3), (30), (60), (90), (120); 

INSERT INTO Movie (title, duration) VALUES 
('Dr Strange','90'),('Fantastic Beast', '90'), ('Mulan', '60'), ('Lion King','70'),
('Harry Potter', '110'), ('Star Wars', '90'), ('Warcraft', '70'), ('Pokemon', '60'), ('Digimon', '60');

INSERT INTO Showtimes (showtime, trid, date) VALUES 
('12:00:00', 1, '2016-11-16'), ('13:20:00', 2, '2016-11-16'),('13:00:00', 3, '2016-11-16'),('12:00:00', 4,'2016-11-16')
,('15:30:00', 2,'2016-11-30'),('20:00:00', 1,'2016-12-10'),('10:30:00', 1,'2016-12-01'),('9:00:00', 2,'2016-12-02');


INSERT INTO RegUser(uname, fname, lname,email, pw) VALUES
 ('BobSmith408', 'Bob','Smith','BobSmith408@yahoo.com','1234'), ('himynameisalan', 'Alan','Dang','alan@yahoo.com','1234'),
 ('hellosir', 'Hello','Sir','hellosir@yahoo.com','1234'), ('dude', 'This','Dude','thisdude@yahoo.com','1234'),
 ('MichelleYu', 'Michelle','Yu','MichelleYu@yahoo.com','1234'), ('hellotoyoutoo', 'Amy','Tran','hellotoyoutoo@yahoo.com','1234')
;
 
INSERT INTO Admin (uname, fname, lname,email, pw) VALUES 
('AlanFuulol', 'Alan','Fuu','AlanFuulol@yahoo.com','1111'), ('himynameisdung', 'Dung','Nguyen','himynameisdung@yahoo.com','1234'),
('Philly', 'Phil','Ly','Philly@yahoo.com','1234'), ('dangmang', 'Dan','Houa','dangmang@yahoo.com','1234'), 
('whatthe', 'Christianne','Bond','whatthe@yahoo.com','1234'), ('joee', 'Vinh','Joe','joee@yahoo.com','1234');

