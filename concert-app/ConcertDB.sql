--Rollback;
START TRANSACTION;

DROP TABLE IF EXISTS concert_opening_act CASCADE;
DROP TABLE IF EXISTS user_concert CASCADE;
DROP TABLE IF EXISTS user_yearly_stats CASCADE;
DROP TABLE IF EXISTS concert_event CASCADE;
DROP TABLE IF EXISTS festival CASCADE;
DROP TABLE IF EXISTS tour CASCADE;
DROP TABLE IF EXISTS venue CASCADE;
DROP TABLE IF EXISTS artist CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;


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

CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO roles (name)
VALUES ('USER'), ('ADMIN')
ON CONFLICT (name) DO NOTHING;

CREATE TABLE user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
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

CREATE TABLE concert_event (
	concert_event_id SERIAL PRIMARY KEY,
	artist_id INT NOT NULL,
	venue_id INT NOT NULL,
	event_date DATE NOT NULL,
	start_time TIME,
	tour_id INT,
	festival_id INT,
	created_by INT NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (artist_id) REFERENCES artist(artist_id),
	FOREIGN KEY (venue_id) REFERENCES venue(venue_id),
	FOREIGN KEY (tour_id) REFERENCES tour(tour_id),
	FOREIGN KEY	(festival_id) REFERENCES festival(festival_id),
	FOREIGN KEY (created_by) REFERENCES users(user_id)
);

CREATE TABLE user_concert (
    user_id INT NOT NULL,
    concert_event_id INT NOT NULL,
    notes TEXT,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, concert_event_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (concert_event_id) REFERENCES
        concert_event(concert_event_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uq_event_with_time
ON concert_event (artist_id, venue_id, event_date, start_time)
WHERE start_time IS NOT NULL;

CREATE UNIQUE INDEX uq_event_without_time
ON concert_event (artist_id, venue_id, event_date)
WHERE start_time IS NULL;

CREATE TABLE concert_opening_act (
	concert_event_id INT NOT NULL,
	artist_id INT NOT NULL,
	PRIMARY KEY (concert_event_id, artist_id),
	FOREIGN KEY (concert_event_id) REFERENCES concert_event(concert_event_id)
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
DECLARE
    event_year INT;
BEGIN

    SELECT EXTRACT(YEAR FROM event_date)::INT
    INTO event_year
    FROM concert_event
    WHERE concert_event_id = NEW.concert_event_id;

    UPDATE users
    SET total_concerts = total_concerts + 1
    WHERE user_id = NEW.user_id;

    INSERT INTO user_yearly_stats (user_id, year, concert_count)
    VALUES (NEW.user_id, event_year, 1)
    ON CONFLICT (user_id, year)
    DO UPDATE SET concert_count = user_yearly_stats.concert_count + 1;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION trg_concert_delete_func()
RETURNS TRIGGER AS $$
DECLARE
    event_year INT;
BEGIN
    SELECT EXTRACT(YEAR FROM event_date)::INT
    INTO event_year
    FROM concert_event
    WHERE concert_event_id = OLD.concert_event_id;

    UPDATE users
    SET total_concerts = GREATEST(total_concerts - 1, 0)
    WHERE user_id = OLD.user_id;

    UPDATE user_yearly_stats
    SET concert_count = GREATEST(concert_count - 1, 0)
    WHERE user_id = OLD.user_id AND year = event_year;

    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_user_concert_insert ON user_concert;
CREATE TRIGGER trg_user_concert_insert
AFTER INSERT ON user_concert
FOR EACH ROW
EXECUTE FUNCTION trg_concert_insert_func();

DROP TRIGGER IF EXISTS trg_user_concert_delete ON user_concert;
CREATE TRIGGER trg_user_concert_delete
AFTER DELETE ON user_concert
FOR EACH ROW
EXECUTE FUNCTION trg_concert_delete_func();

CREATE INDEX IF NOT EXISTS idx_user_concert_user
    ON user_concert (user_id);

CREATE INDEX IF NOT EXISTS idx_user_concert_event
ON user_concert (concert_event_id);
COMMIT;






