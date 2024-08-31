INSERT INTO app_user (goal_id, first_name, last_name, username, points, streak, age, email)
VALUES
    (1, 'John', 'Doe', 'johndoe', 1200, 5, 25, john@gmail.com),
    (2, 'Jane', 'Smith', 'janesmith', 1500, 7, 30, jane@gmail.com);

INSERT INTO goal_type (id, type) VALUES
(1, 'NOW_PLAYING'),
(2, 'MOST_POPULAR'),
(3, 'TOP_RATED'),
(4, 'DISCOVER');

INSERT INTO badge (id, name, genre_to_unlock, multiplier_level) VALUES
(9, 'crime beginner', 5, 1),
(10, 'horror beginner', 11, 1),
(11, 'comedy beginner', 4, 1),
(12, 'action beginner', 1, 1),
(13, 'adventure beginner', 2, 1),
(14, 'animation beginner', 3, 1),
(15, 'family beginner', 8, 1),
(16, 'history beginner', 10, 1),
(17, 'mystery beginner', 13, 1),
(18, 'fantasy beginner', 9, 1),
(19, 'western beginner', 19, 1),
(20, 'drama beginner', 7, 1),
(21, 'thriller beginner', 17, 1),
(22, 'comedy expert', 4, 2),
(23, 'western expert', 19, 2),
(24, 'science fiction beginner', 15, 1);

INSERT INTO country_of_production (name, country_code)
VALUES
    ('Canada', 'CA');
INSERT INTO country_of_production (name, country_code) VALUES ('Afghanistan', 'AF');
INSERT INTO country_of_production (name, country_code) VALUES ('Albania', 'AL');
INSERT INTO country_of_production (name, country_code) VALUES ('Algeria', 'DZ');
INSERT INTO country_of_production (name, country_code) VALUES ('Argentina', 'AR');
INSERT INTO country_of_production (name, country_code) VALUES ('Australia', 'AU');
INSERT INTO country_of_production (name, country_code) VALUES ('Austria', 'AT');
INSERT INTO country_of_production (name, country_code) VALUES ('Bangladesh', 'BD');
INSERT INTO country_of_production (name, country_code) VALUES ('Belgium', 'BE');
INSERT INTO country_of_production (name, country_code) VALUES ('Brazil', 'BR');
INSERT INTO country_of_production (name, country_code) VALUES ('China', 'CN');
INSERT INTO country_of_production (name, country_code) VALUES ('Colombia', 'CO');
INSERT INTO country_of_production (name, country_code) VALUES ('Croatia', 'HR');
INSERT INTO country_of_production (name, country_code) VALUES ('Cuba', 'CU');
INSERT INTO country_of_production (name, country_code) VALUES ('Czech Republic', 'CZ');
INSERT INTO country_of_production (name, country_code) VALUES ('Denmark', 'DK');
INSERT INTO country_of_production (name, country_code) VALUES ('Egypt', 'EG');
INSERT INTO country_of_production (name, country_code) VALUES ('Finland', 'FI');
INSERT INTO country_of_production (name, country_code) VALUES ('France', 'FR');
INSERT INTO country_of_production (name, country_code) VALUES ('Germany', 'DE');
INSERT INTO country_of_production (name, country_code) VALUES ('Greece', 'GR');
INSERT INTO country_of_production (name, country_code) VALUES ('Hong Kong', 'HK');
INSERT INTO country_of_production (name, country_code) VALUES ('India', 'IN');
INSERT INTO country_of_production (name, country_code) VALUES ('Indonesia', 'ID');
INSERT INTO country_of_production (name, country_code) VALUES ('Iran', 'IR');
INSERT INTO country_of_production (name, country_code) VALUES ('Iraq', 'IQ');
INSERT INTO country_of_production (name, country_code) VALUES ('Ireland', 'IE');
INSERT INTO country_of_production (name, country_code) VALUES ('Israel', 'IL');
INSERT INTO country_of_production (name, country_code) VALUES ('Italy', 'IT');
INSERT INTO country_of_production (name, country_code) VALUES ('Japan', 'JP');
INSERT INTO country_of_production (name, country_code) VALUES ('Kenya', 'KE');
INSERT INTO country_of_production (name, country_code) VALUES ('Malaysia', 'MY');
INSERT INTO country_of_production (name, country_code) VALUES ('Mexico', 'MX');
INSERT INTO country_of_production (name, country_code) VALUES ('Netherlands', 'NL');
INSERT INTO country_of_production (name, country_code) VALUES ('New Zealand', 'NZ');
INSERT INTO country_of_production (name, country_code) VALUES ('Nigeria', 'NG');
INSERT INTO country_of_production (name, country_code) VALUES ('Norway', 'NO');
INSERT INTO country_of_production (name, country_code) VALUES ('Pakistan', 'PK');
INSERT INTO country_of_production (name, country_code) VALUES ('Peru', 'PE');
INSERT INTO country_of_production (name, country_code) VALUES ('Philippines', 'PH');
INSERT INTO country_of_production (name, country_code) VALUES ('Poland', 'PL');
INSERT INTO country_of_production (name, country_code) VALUES ('Portugal', 'PT');
INSERT INTO country_of_production (name, country_code) VALUES ('Qatar', 'QA');
INSERT INTO country_of_production (name, country_code) VALUES ('Romania', 'RO');
INSERT INTO country_of_production (name, country_code) VALUES ('Russia', 'RU');
INSERT INTO country_of_production (name, country_code) VALUES ('Saudi Arabia', 'SA');
INSERT INTO country_of_production (name, country_code) VALUES ('Singapore', 'SG');
INSERT INTO country_of_production (name, country_code) VALUES ('South Africa', 'ZA');
INSERT INTO country_of_production (name, country_code) VALUES ('South Korea', 'KR');
INSERT INTO country_of_production (name, country_code) VALUES ('Spain', 'ES');
INSERT INTO country_of_production (name, country_code) VALUES ('Sri Lanka', 'LK');
INSERT INTO country_of_production (name, country_code) VALUES ('Sweden', 'SE');
INSERT INTO country_of_production (name, country_code) VALUES ('Switzerland', 'CH');
INSERT INTO country_of_production (name, country_code) VALUES ('Taiwan', 'TW');
INSERT INTO country_of_production (name, country_code) VALUES ('Thailand', 'TH');
INSERT INTO country_of_production (name, country_code) VALUES ('Turkey', 'TR');
INSERT INTO country_of_production (name, country_code) VALUES ('Ukraine', 'UA');
INSERT INTO country_of_production (name, country_code) VALUES ('United Arab Emirates', 'AE');
INSERT INTO country_of_production (name, country_code) VALUES ('United Kingdom', 'GB');
INSERT INTO country_of_production (name, country_code) VALUES ('United States', 'US');
INSERT INTO country_of_production (name, country_code) VALUES ('Venezuela', 'VE');
INSERT INTO country_of_production (name, country_code) VALUES ('Vietnam', 'VN');

INSERT INTO genre (tmdb_id, name) VALUES (28, 'Action');
INSERT INTO genre (tmdb_id, name) VALUES (12, 'Adventure');
INSERT INTO genre (tmdb_id, name) VALUES (16, 'Animation');
INSERT INTO genre (tmdb_id, name) VALUES (35, 'Comedy');
INSERT INTO genre (tmdb_id, name) VALUES (80, 'Crime');
INSERT INTO genre (tmdb_id, name) VALUES (99, 'Documentary');
INSERT INTO genre (tmdb_id, name) VALUES (18, 'Drama');
INSERT INTO genre (tmdb_id, name) VALUES (10751, 'Family');
INSERT INTO genre (tmdb_id, name) VALUES (14, 'Fantasy');
INSERT INTO genre (tmdb_id, name) VALUES (36, 'History');
INSERT INTO genre (tmdb_id, name) VALUES (27, 'Horror');
INSERT INTO genre (tmdb_id, name) VALUES (10402, 'Music');
INSERT INTO genre (tmdb_id, name) VALUES (9648, 'Mystery');
INSERT INTO genre (tmdb_id, name) VALUES (10749, 'Romance');
INSERT INTO genre (tmdb_id, name) VALUES (878, 'Science Fiction');
INSERT INTO genre (tmdb_id, name) VALUES (10770, 'TV Movie');
INSERT INTO genre (tmdb_id, name) VALUES (53, 'Thriller');
INSERT INTO genre (tmdb_id, name) VALUES (10752, 'War');
INSERT INTO genre (tmdb_id, name) VALUES (37, 'Western');

INSERT INTO course (title)
VALUES
    ('Introduction to film directing'),
    ('Introduction to Nouvelle vague');