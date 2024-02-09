INSERT INTO status (label) VALUES
    ('Confirmé'),
    ('En attente'),
    ('Annulé');


INSERT INTO ad (hourprice, state, description, latitude, longitude, name, userId) VALUES
(2.0, true, 'coucou', 48.8566, 2.3522, 'test', 'fdfvreferg'),
(2.0, true, 'coucou2', 48.8666, 2.3722, 'test2', 'fdfvreferg'),
(2.0, true, 'coucou3', 48.8466, 2.3322, 'test3', 'fdfvreferg'),
(2.0, true, 'coucou4', 48.8566, 2.4889, 'test4', 'fdfvreferg'),
(2.0, true, 'coucou5', 48.8566, 2.4888, 'test5', 'fdfvreferg');

INSERT INTO reservation (ad, status, beginDate, endDate, userId) VALUES
    (1, 1, '2024-06-12', '2024-06-16', 'vbggtrgrr'),
    (1, 1, '2024-06-18', '2024-06-22', 'vbggtrgrr'),
    (1, 1, '2024-07-12', '2024-07-16', 'vbggtrgrr');

INSERT INTO feedback (ad, rating, description, date, userId) VALUES
(1, 5, 'coucou', '2023-10-04', 'fegfegegtger'),
(1, 5, 'coucou2', '2023-10-04', 'fegfegegtger'),
(1, 5, 'coucou3', '2023-10-04', 'fegfegegtger'),
(1, 4, 'coucou4', '2023-10-04', 'fegfegegtger');