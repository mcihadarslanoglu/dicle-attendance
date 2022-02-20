CREATE TABLE `Student` (
	`student_id` INT NOT NULL,
	`first_name` varchar(45) NOT NULL,
	`last_name` varchar(45) NOT NULL,
	PRIMARY KEY (`student_id`)
);

CREATE TABLE `Attendance` (
	`attendance_id` INT NOT NULL AUTO_INCREMENT,
	`lesson_id` INT NOT NULL,
	`student_id` INT NOT NULL,
	`present` BOOLEAN NOT NULL,
	PRIMARY KEY (`attendance_id`)
);

CREATE TABLE `Teacher` (
	`teacher_id` INT NOT NULL AUTO_INCREMENT,
	`first_name` varchar(45) NOT NULL,
	`last_name` varchar(45) NOT NULL,
	PRIMARY KEY (`teacher_id`)
);

CREATE TABLE `Lesson` (
	`lesson_id` INT NOT NULL AUTO_INCREMENT,
	`teacher_id` INT NOT NULL,
	PRIMARY KEY (`lesson_id`)
);

CREATE TABLE `Teacher_has_lesson` (
	`lesson_id` INT NOT NULL,
	`teacher_id` INT NOT NULL
);

CREATE TABLE `Student_has_lesson` (
	`student_id` INT NOT NULL,
	`lesson_id` INT NOT NULL
);

ALTER TABLE `Attendance` ADD CONSTRAINT `Attendance_fk0` FOREIGN KEY (`lesson_id`) REFERENCES `Lesson`(`lesson_id`);

ALTER TABLE `Attendance` ADD CONSTRAINT `Attendance_fk1` FOREIGN KEY (`student_id`) REFERENCES `Student`(`student_id`);

ALTER TABLE `Lesson` ADD CONSTRAINT `Lesson_fk0` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher`(`teacher_id`);

ALTER TABLE `Teacher_has_lesson` ADD CONSTRAINT `Teacher_has_lesson_fk0` FOREIGN KEY (`lesson_id`) REFERENCES `Lesson`(`lesson_id`);

ALTER TABLE `Teacher_has_lesson` ADD CONSTRAINT `Teacher_has_lesson_fk1` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher`(`teacher_id`);

ALTER TABLE `Student_has_lesson` ADD CONSTRAINT `Student_has_lesson_fk0` FOREIGN KEY (`student_id`) REFERENCES `Student`(`student_id`);

ALTER TABLE `Student_has_lesson` ADD CONSTRAINT `Student_has_lesson_fk1` FOREIGN KEY (`lesson_id`) REFERENCES `Lesson`(`lesson_id`);







