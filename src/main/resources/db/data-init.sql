INSERT INTO status (label) VALUES
('Confirmé'),
('En attente'),
('Annulé');

INSERT INTO app_user (uid, firstname, lastname, email, roleId) VALUES
('xau0If5kFubYSQBOG5y6I6vpMK73', 'hugo', 'barbaste', 'hugobast33@gmail.com', 2);

INSERT INTO ad (hourprice, state, description, latitude, longitude, name, userId) VALUES
(2.0, true, 'coucou', 2.01285895, -2.095629599, 'test', 'xau0If5kFubYSQBOG5y6I6vpMK73'),
(2.0, true, 'coucou2', 2.01285895, -2.095629599, 'test2', 'xau0If5kFubYSQBOG5y6I6vpMK73'),
(2.0, true, 'coucou3', 2.01285895, -2.095629599, 'test3', 'xau0If5kFubYSQBOG5y6I6vpMK73');

INSERT INTO reservation (ad, status, beginDate, endDate, userId) VALUES
(1, 1, '2022-06-12', '2022-06-16', 'xau0If5kFubYSQBOG5y6I6vpMK73'),
(1, 1, '2022-06-12', '2022-06-16', 'xau0If5kFubYSQBOG5y6I6vpMK73'),
(1, 1, '2022-06-12', '2022-06-16', 'xau0If5kFubYSQBOG5y6I6vpMK73');

INSERT INTO feedback (ad, rating, description, date, userId) VALUES
(1, 5, 'coucou', '2023-10-04', 'xau0If5kFubYSQBOG5y6I6vpMK73'),
(1, 5, 'coucou2', '2023-10-04', 'xau0If5kFubYSQBOG5y6I6vpMK73'),
(1, 5, 'coucou3', '2023-10-04', 'xau0If5kFubYSQBOG5y6I6vpMK73');