
-- password = "password" (dit comment is een security lek, zet dit nooit in je code.
-- Als je hier je plaintext password niet meer weet, moet je een nieuw password encrypted)
INSERT INTO users (username, password, email, enabled) VALUES ('participant', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK','participant@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'admin@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('organizer', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'organizer@test.nl', TRUE);

INSERT INTO roles (username, role) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO roles (username, role) VALUES ('organizer', 'ROLE_ORGANIZER');
INSERT INTO roles (username, role) VALUES ('participant', 'ROLE_PARTICIPANT');


-- Add event
INSERT INTO events (id, name, organizer_username, location, start_date, end_date, capacity, price)
VALUES ('c4205e65-fee0-4c4f-9470-88edfc8280e6','Annual Tech Conference', 'admin', 'Netherlands', '2025-06-15', '2025-06-15', '22', 300);
--
-- -- Add Feedback
INSERT INTO feedbacks (comment, rating, username, feedback_date, id)
VALUES ('Great event! Learned a lot about new technologies.', 5, 'admin', '2025-06-15', 'c4205e65-fee0-4c4f-9470-88edfc8280e2');

-- -- Add Ticket
INSERT INTO tickets (id, price, purchase_date, ticket_type, ticket_code)
VALUES ('c4205e65-fee0-4c4f-9470-88edfc8280e5', 10, '2025-06-15', 'FREE', 'c4205e65-fee0-4c4f-9470-88edfc82822');
