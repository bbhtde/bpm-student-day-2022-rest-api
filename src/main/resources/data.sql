INSERT INTO courses (course_id, course_number, title, description, start, pricing, max_seats) VALUES (next value for course_seq, 'BPM-001', 'BPM 101: Your step into business process management', 'This course is great!', CURRENT_TIMESTAMP + 0.002083, 'PRICE_LEVEL_3', 15);

INSERT INTO users (user_id, email, first_name, last_name, iban) VALUES (next value for user_seq, 'max@mustermann.de', 'Max', 'Mustermann', 'DE12500105170648489890');

INSERT INTO courses_users(course_id, user_id) VALUES (current value for course_seq, current value for user_seq);

INSERT INTO courses (course_id, course_number, title, description, start, pricing, max_seats) VALUES (next value for course_seq, 'PA-001', 'PA 101: Your first steps into process automation', 'This course is great!', CURRENT_TIMESTAMP + 2, 'FREE', 10);

INSERT INTO users (user_id, email, first_name, last_name, iban) VALUES (next value for user_seq, 'hacker@demo.qa', 'Hacker', 'Demo', 'QA99996666444422221111');
