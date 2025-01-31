
-- password = "password" (dit comment is een security lek, zet dit nooit in je code.
-- Als je hier je plaintext password niet meer weet, moet je een nieuw password encrypted)
INSERT INTO users (username, password, email, enabled) VALUES ('participant', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK','participant@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('admin', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'admin@test.nl', TRUE);
INSERT INTO users (username, password, email, enabled) VALUES ('organizer', '$2a$12$IzA1Ja1LH4PSMoro9PeITO1etDlknPjSX1nLusgt1vi9c1uaEXdEK', 'organizer@test.nl', TRUE);

INSERT INTO roles (username, role) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO roles (username, role) VALUES ('organizer', 'ROLE_ORGANIZER');
INSERT INTO roles (username, role) VALUES ('participant', 'ROLE_PARTICIPANT');


-- Add event
INSERT INTO events (id, name, organizer_id, location, start_time, end_time, capacity, price)
VALUES ('c4205e65-fee0-4c4f-9470-88edfc8280e6','Annual Tech Conference', 'admin', 'Netherlands', '2025-06-15T09:00:00', '2025-06-15T18:00:00', '22', 300);
--
-- -- Add Feedback
-- INSERT INTO feedback (comment, rating, username, event_id)
-- VALUES ('Great event! Learned a lot about new technologies.', 5, 'participant', 1);
--

-- -- Add Ticket
INSERT INTO tickets (id, price, purchase_date, ticket_type, ticket_code)
VALUES ('c4205e65-fee0-4c4f-9470-88edfc8280e5', 10, '2025-06-15T09:00:00', 'FREE', 'c4205e65-fee0-4c4f-9470-88edfc82822');
