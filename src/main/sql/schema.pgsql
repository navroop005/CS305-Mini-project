CREATE TABLE IF NOT EXISTS users (
    email VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    role VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS department (
    code CHAR(2) PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    email VARCHAR PRIMARY KEY,
    entry_number CHAR(11) NOT NULL,
    batch INTEGER NOT NULL,
    dept CHAR(2) NOT NULL,
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE,
    FOREIGN KEY (dept) REFERENCES department(code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS instructors (
    email VARCHAR PRIMARY KEY NOT NULL,
    dept CHAR(2) NOT NULL,
    FOREIGN KEY (email) REFERENCES users(email) ON DELETE CASCADE,
    FOREIGN KEY (dept) REFERENCES department(code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_catalogue (
    code CHAR(5) NOT NULL,
    name VARCHAR NOT NULL,
    dept CHAR(2) NOT NULL,
    credits NUMERIC(3) NOT NULL,
    prerequisites CHAR(5)[], 
    PRIMARY KEY (code, dept),
    FOREIGN KEY (dept) REFERENCES department(code) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_offering (
    code CHAR(5) NOT NULL,
    dept CHAR(2) NOT NULL,
    session CHAR(7) NOT NULL,
    instructor VARCHAR NOT NULL,
    min_cg NUMERIC(3),
    batch INTEGER NOT NULL, 
    branch CHAR(2) NOT NULL, 
    course_type CHAR(2) NOT NULL,
    PRIMARY KEY (code, session, dept, batch, branch, course_type),
    FOREIGN KEY (code, dept) REFERENCES course_catalogue(code, dept) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_enrollments (
    code CHAR(5) NOT NULL,
    student_id VARCHAR NOT NULL,
    dept CHAR(2) NOT NULL,
    session CHAR(7) NOT NULL,
    batch INTEGER NOT NULL, 
    branch CHAR(2) NOT NULL,
    course_type CHAR(2) NOT NULL,
    grade VARCHAR(2),
    PRIMARY KEY (code, session, dept, student_id),
    FOREIGN KEY (code, dept, session, batch, branch, course_type) REFERENCES course_offering(code, dept, session, batch, branch, course_type) ON DELETE CASCADE
);
