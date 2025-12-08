--Rollback;
START TRANSACTION;

DROP TABLE IF EXISTS concert_opening_act;
DROP TABLE IF EXISTS concert;
DROP TABLE IF EXISTS user_yearly_stats;
DROP TABLE IF EXISTS festival;
DROP TABLE IF EXISTS tour;
DROP TABLE IF EXISTS venue;
DROP TABLE IF EXISTS artist;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
	user_id SERIAL PRIMARY KEY,
	username VARCHAR(50) UNIQUE NOT NULL,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	password_hash VARCHAR(250) NOT NULL,
	wanting_email_updates BOOLEAN DEFAULT TRUE,
	total_concerts INT DEFAULT 0,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE artist (
	artist_id SERIAL PRIMARY KEY,
	name VARCHAR(150) UNIQUE NOT NULL
);

CREATE TABLE venue (
	venue_id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	city VARCHAR(100) NOT NULL,
	state VARCHAR(50) NOT NULL
);

CREATE TABLE tour (
	tour_id SERIAL PRIMARY KEY,
	name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE festival (
	festival_id SERIAL PRIMARY KEY,
	name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE concert (
	concert_id SERIAL PRIMARY KEY,
	user_id INT NOT NULL,
	artist_id INT NOT NULL,
	venue_id INT NOT NULL,
	tour_id INT,
	festival_id INT,
	date DATE NOT NULL,
	notes TEXT,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (user_id) REFERENCES users(user_id),
	FOREIGN KEY (artist_id) REFERENCES artist(artist_id),
	FOREIGN KEY (venue_id) REFERENCES venue(venue_id),
	FOREIGN KEY (tour_id) REFERENCES tour(tour_id),
	FOREIGN KEY	(festival_id) REFERENCES festival(festival_id)
);

CREATE TABLE concert_opening_act (
	concert_id INT NOT NULL,
	artist_id INT NOT NULL,
	PRIMARY KEY (concert_id, artist_id),
	FOREIGN KEY (concert_id) REFERENCES concert(concert_id)
		ON DELETE CASCADE,
	FOREIGN KEY (artist_id) REFERENCES artist(artist_id)
);

CREATE TABLE user_yearly_stats (
	user_id INT NOT NULL,
	year INT NOT NULL,
	concert_count INT DEFAULT 0,
	PRIMARY KEY (user_id, year),
	FOREIGN KEY (user_id) REFERENCES users(user_id)
		ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION trg_concert_insert_func()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE users
    SET total_concerts = total_concerts + 1
    WHERE user_id = NEW.user_id;

    INSERT INTO user_yearly_stats (user_id, year, concert_count)
    VALUES (NEW.user_id, EXTRACT(YEAR FROM NEW.date)::INT, 1)
    ON CONFLICT (user_id, year)
    DO UPDATE SET concert_count = user_yearly_stats.concert_count + 1;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trg_concert_delete_func()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE users
    SET total_concerts = total_concerts - 1
    WHERE user_id = OLD.user_id;

    UPDATE user_yearly_stats
    SET concert_count = GREATEST(concert_count - 1, 0)
    WHERE user_id = OLD.user_id AND year = EXTRACT(YEAR FROM OLD.date)::INT;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_concert_insert
AFTER INSERT ON concert
FOR EACH ROW
EXECUTE FUNCTION trg_concert_insert_func();

CREATE TRIGGER trg_concert_delete
AFTER DELETE ON concert
FOR EACH ROW
EXECUTE FUNCTION trg_concert_delete_func();

CREATE INDEX idx_concert_user_date ON concert (user_id, date);
COMMIT;






