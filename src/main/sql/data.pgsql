INSERT INTO users VALUES ('admin@iitrpr.ac.in', 'ADMIN', 'admin', 'admin') ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('acadoffice@iitrpr.ac.in', 'ACADEMICS OFFICE', 'acadoffice', 'acad_office') ON CONFLICT DO NOTHING;

INSERT INTO department VALUES ('CS', 'Computer Science and Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('MC', 'Mathematics and Computing') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('AI', 'Artificial Intelligence') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('CE', 'Civil Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('CH', 'Chemical Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('EE', 'Electrical Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('ME', 'Mechanical Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('BM', 'Biomedical Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('MM', 'Metallurgical and Materials Engineering') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('MA', 'Mathematics') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('PH', 'Physics') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('CH', 'Chemistry') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('HS', 'Humanities and Social Sciences') ON CONFLICT DO NOTHING;
INSERT INTO department VALUES ('GE', 'General Engineering') ON CONFLICT DO NOTHING;