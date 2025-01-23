
-- password = "password" (dit comment is een security lek, zet dit nooit in je code.
-- Als je hier je plaintext password niet meer weet, moet je een nieuw password encrypted)
INSERT INTO users (username, password, email, enabled) VALUES ('participant', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK','participant@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'admin@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('organizer', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'organizer@test.nl', TRUE);

INSERT INTO roles (username, role) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO roles (username, role) VALUES ('organizer', 'ROLE_ORGANIZER');
INSERT INTO roles (username, role) VALUES ('participant', 'ROLE_PARTICIPANT');


-- Add event
INSERT INTO events (name, location, description, start_date, end_date, max_participants, status)
VALUES ('Annual Tech Conference', 'Amsterdam, Netherlands', 'A conference focusing on the latest in tech innovation.', '2025-04-01', '2025-04-03', 300, 'UPCOMING');


-- Add Feedback
INSERT INTO feedback (comment, rating, username, event_id)
VALUES ('Great event! Learned a lot about new technologies.', 5, 'participant', 1);
