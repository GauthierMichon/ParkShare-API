INSERT INTO status (label) VALUES
    ('Confirmé'),
    ('En attente'),
    ('Annulé');


INSERT INTO ad (hourprice, state, description, latitude, longitude, name, userId) VALUES
(2.0, true, 'coucou', 2.01285895, -2.095629599, 'test', 'fdfvreferg'),
(2.0, true, 'coucou2', 2.01285895, -2.095629599, 'test2', 'fdfvreferg'),
(2.0, true, 'coucou3', 2.01285895, -2.095629599, 'test3', 'fdfvreferg');

INSERT INTO reservation (ad, status, beginDate, endDate, userId) VALUES
    (1, 1, '2022-06-12', '2022-06-16', 'vbggtrgrr'),
    (1, 1, '2022-06-12', '2022-06-16', 'vbggtrgrr'),
    (1, 1, '2022-06-12', '2022-06-16', 'vbggtrgrr');

INSERT INTO feedback (ad, rating, description, date, userId) VALUES
(1, 5, 'coucou', '2023-10-04', 'fegfegegtger'),
(1, 5, 'coucou2', '2023-10-04', 'fegfegegtger'),
(1, 5, 'coucou3', '2023-10-04', 'fegfegegtger');