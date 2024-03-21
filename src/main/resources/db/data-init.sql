INSERT INTO status (label) VALUES
('Confirmé'),
('En attente'),
('Annulé');

INSERT INTO app_user (uid, firstname, lastname, email, roleId) VALUES
('8yk8My4TXZZPNkXOhAIki4y1iP83', 'hugo', 'barbaste', 'hugobast33@gmail.com', 2);

INSERT INTO ad (hourprice, state, description, latitude, longitude, name, userId) VALUES
(2.0, true, 'coucou', 2.01285895, -2.095629599, 'test', '8yk8My4TXZZPNkXOhAIki4y1iP83'),
(2.0, true, 'coucou2', 2.01285895, -2.095629599, 'test2', '8yk8My4TXZZPNkXOhAIki4y1iP83'),
(2.0, true, 'coucou3', 2.01285895, -2.095629599, 'test3', '8yk8My4TXZZPNkXOhAIki4y1iP83');

INSERT INTO reservation (ad, status, beginDate, endDate, totalPrice, userId) VALUES
(1, 1, '2022-06-12', '2022-06-16', 0.0, '8yk8My4TXZZPNkXOhAIki4y1iP83'),
(1, 1, '2022-06-12', '2022-06-16', 0.0, '8yk8My4TXZZPNkXOhAIki4y1iP83'),
(1, 1, '2022-06-12', '2022-06-16', 0.0, '8yk8My4TXZZPNkXOhAIki4y1iP83');

INSERT INTO feedback (ad, rating, description, date, userId) VALUES
(1, 5, 'coucou', '2023-10-04', '8yk8My4TXZZPNkXOhAIki4y1iP83'),
(1, 5, 'coucou2', '2023-10-04', '8yk8My4TXZZPNkXOhAIki4y1iP83'),
(1, 5, 'coucou3', '2023-10-04', '8yk8My4TXZZPNkXOhAIki4y1iP83');