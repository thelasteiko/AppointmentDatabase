#creates the database and uses it
CREATE DATABASE IF NOT EXISTS tutor_db;
USE tutor_db;

#Student holds information about individual students.
#They may be active/inactive or enrolled.
CREATE TABLE tutor_db.student (
  StudentID INT NOT NULL COMMENT '',
  FirstName VARCHAR(45) NULL COMMENT '',
  LastName VARCHAR(45) NULL COMMENT '',
  StudentStatus VARCHAR(45) NULL COMMENT '',
  PRIMARY KEY (StudentID)  COMMENT ''
);

#Class Section lists the sections available for
#each class.
CREATE TABLE tutor_db.class_section (
  EnrollmentCode INT NULL COMMENT '',
  ClassNumber INT NOT NULL,
  Section VARCHAR(45) NULL COMMENT '',
  PRIMARY KEY (EnrollmentCode)  COMMENT ''
);

#Class Name lists the name associated with
#each class.
CREATE TABLE tutor_db.class_name (
    ClassNumber INT NOT NULL,
    ClassName VARCHAR(45) NULL COMMENT '',
    PRIMARY KEY (ClassNumber)
);

#Enrolled shows which students are
#enrolled and what class they are
#enrolled in.
CREATE TABLE tutor_db.enrolled (
    StudentID INT NOT NULL,
    EnrollmentCode INT NOT NULL,
    CONSTRAINT u_enroll UNIQUE (StudentID, EnrollmentCode)
);

#Visit is for the tutoring appointments
#made by students.
CREATE TABLE tutor_db.visit (
  StudentID INT NOT NULL COMMENT '',
  ClassNumber INT NOT NULL COMMENT '',
  StartDate DATE NULL COMMENT '',
  StartTime INT NULL COMMENT '',
  Duration INT NULL COMMENT '',
  CONSTRAINT pk_visit PRIMARY KEY (StudentID, StartDate, StartTime)
);

#These alterations set the foreign keys of each table.
ALTER TABLE tutor_db.visit 
  ADD CONSTRAINT fk_visitclass FOREIGN KEY (ClassNumber)
	REFERENCES class_name(ClassNumber)
    ON DELETE NO ACTION
	ON UPDATE CASCADE,
  ADD CONSTRAINT fk_visitstudent FOREIGN KEY (StudentID)
	REFERENCES student(StudentID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE
;

ALTER TABLE tutor_db.class_section
  ADD CONSTRAINT fk_sectionclass FOREIGN KEY (ClassNumber)
    REFERENCES class_name(ClassNumber)
    ON DELETE NO ACTION
    ON UPDATE CASCADE
;

ALTER TABLE tutor_db.enrolled
  ADD CONSTRAINT fk_enrclass FOREIGN KEY (EnrollmentCode)
	REFERENCES class_section(EnrollmentCode)
    ON DELETE NO ACTION
    ON UPDATE CASCADE,
  ADD CONSTRAINT fk_enrstudent FOREIGN KEY (StudentID)
	REFERENCES student(StudentID)
    ON DELETE NO ACTION
    ON UPDATE CASCADE
;

#Sample classes and sections.
INSERT INTO class_name VALUES (240, 'Calculus I');
	INSERT INTO class_section VALUES (19534, 240, 'A');
	INSERT INTO class_section VALUES (19535, 240, 'B');
INSERT INTO class_name VALUES (142, 'Introduction to Java Programming I');
	INSERT INTO class_section VALUES (2073, 142, 'A');
	INSERT INTO class_section VALUES (2074, 142, 'B');
INSERT INTO class_name VALUES (101, 'English Literature I');
	INSERT INTO class_section VALUES (8234, 101, 'A');
	INSERT INTO class_section VALUES (8235, 101, 'B');
	INSERT INTO class_section VALUES (8236, 101, 'C');
	INSERT INTO class_section VALUES (8237, 101, 'D');
INSERT INTO class_name VALUES (143, 'Introduction to Java Programming II');
	INSERT INTO class_section VALUES (2075, 143, 'A');
INSERT INTO class_name VALUES (102, 'English Literature II');
	INSERT INTO class_section VALUES (8238, 102, 'A');
	INSERT INTO class_section VALUES (8239, 102, 'B');
INSERT INTO class_name VALUES (135, 'Native American History');
	INSERT INTO class_section VALUES (38641, 135, 'A');
	INSERT INTO class_section VALUES (38642, 135, 'B');
INSERT INTO class_name VALUES (138, 'Social Welfare during the Great Depression');
	INSERT INTO class_section VALUES (39461, 138, 'A');
INSERT INTO class_name VALUES (250, 'Animal Behaviour and Welfare');
	INSERT INTO class_section VALUES (28553, 250, 'A');
INSERT INTO class_name VALUES (563, 'Major Depression in the Population');
	INSERT INTO class_section VALUES (28725, 563, 'A');
INSERT INTO class_name VALUES (170, 'Qualitative Research Methods');
	INSERT INTO class_section VALUES (28123, 170, 'A');
	INSERT INTO class_section VALUES (28124, 170, 'B');

#Sample students and enrollments
INSERT INTO student VALUES (1349009, "Arty", "Metagroth", 'Enrolled');
	INSERT INTO enrolled VALUES (1349009, 2073);
    INSERT INTO enrolled VALUES (1349009, 8236);
    INSERT INTO enrolled VALUES (1349009, 38641);
INSERT INTO student VALUES (5903236, "Samantha", "Coin", 'Active');
	INSERT INTO enrolled VALUES (5903236, 2075);
    INSERT INTO enrolled VALUES (5903236, 8237);
    INSERT INTO enrolled VALUES (5903236, 28725);
INSERT INTO student VALUES (9873965, "Hon", "Solo", 'Active');
INSERT INTO student VALUES (3828921, "Jeremy", "Davidson", 'Active');
INSERT INTO student VALUES (6389887, "Saria", "Torsha", 'Enrolled');
	INSERT INTO enrolled VALUES (6389887, 8235);
INSERT INTO student VALUES (7269376, "Sorak", "Ipeshi", 'Active');
INSERT INTO student VALUES (8482998, "Jake", "Silver", 'Inactive');
	INSERT INTO enrolled VALUES (8482998, 39461);
INSERT INTO student VALUES (4472977, "Melinda", "Robertson", 'Enrolled');
	INSERT INTO enrolled VALUES (4472977, 8239);
    INSERT INTO enrolled VALUES (4472977, 38641);
INSERT INTO student VALUES (1789342, "Ebeki", "Jordan", 'Enrolled');
	INSERT INTO enrolled VALUES (1789342, 2074);
	INSERT INTO enrolled VALUES (1789342, 8234);

#Sample visits
INSERT INTO visit VALUES (1349009, 101, '2015/04/15', 800, 30);
INSERT INTO visit VALUES (1349009, 101, '2015/10/06', 1500, 30);
INSERT INTO visit VALUES (1789342, 142, '2015/02/5', 1300, 30);
INSERT INTO visit VALUES (4472977, 102, '2015/04/15', 1000, 15);
INSERT INTO visit VALUES (5903236, 143, '2015/03/06', 1700, 10);
INSERT INTO visit VALUES (4472977, 102, '2015/05/30', 1300, 60);
INSERT INTO visit VALUES (5903236, 563, '2015/03/25', 830, 35);
INSERT INTO visit VALUES (8482998, 138, '2015/11/20', 1430, 15);
INSERT INTO visit VALUES (8482998, 138, '2015/12/17', 1645, 20);
INSERT INTO visit VALUES (8482998, 138, '2015/07/01', 745, 90);

#Views used in the application
CREATE VIEW enr_name AS
SELECT EnrollmentCode, class_name.ClassNumber, ClassName
FROM class_section INNER JOIN class_name
ON class_section.ClassNumber = class_name.ClassNumber;

CREATE VIEW enr_id AS
SELECT StudentID, ClassNumber, ClassName
FROM enrolled INNER JOIN enr_name
ON enrolled.EnrollmentCode = enr_name.EnrollmentCode;

CREATE VIEW enr_student AS #this view can be used in-lieu of a more complicated query
SELECT student.StudentID, FirstName, LastName, enr_id.ClassNumber, enr_id.ClassName
FROM student INNER JOIN enr_id
ON student.StudentID = enr_id.StudentID;

#Triggers for appointment validation
#prevent students from making appt if they aren't enrolled
DELIMITER //
CREATE TRIGGER visit_check
BEFORE INSERT ON visit
FOR EACH ROW
BEGIN
    SET @msg = 'Student is not enrolled in that class.';
    IF (new.StudentID NOT IN (
		SELECT enr_student.StudentID
        FROM enr_student
        WHERE enr_student.ClassNumber = new.ClassNumber)
	) THEN
		SIGNAL SQLSTATE '02000' SET MESSAGE_TEXT = @msg;
	END IF;
END //
DELIMITER ;