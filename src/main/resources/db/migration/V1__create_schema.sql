CREATE TABLE user_entity(
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    encrypted_password VARCHAR(60),
    fb_token VARCHAR(255),
    creation_timestamp TIMESTAMP DEFAULT NOW(),
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE parking_state(
    parking_state_id SERIAL PRIMARY KEY,
    parking_node_id BIGINT NOT NULL,
    creation_timestamp TIMESTAMP DEFAULT NOW(),
    parking_state INTEGER NOT NULL,
    user_id INTEGER NOT NULL REFERENCES user_entity(user_id)
);

CREATE TABLE new_parking_report(
    report_id SERIAL PRIMARY KEY,
    attitude DOUBLE PRECISION NOT NULL CHECK(attitude >= -90 and attitude <= 90),
    longitude DOUBLE PRECISION NOT NULL CHECK(attitude >= -180 and attitude <= 180),
    capacity INTEGER,
    stay_cost VARCHAR(10),
    other_information VARCHAR(1000),
    user_id INTEGER NOT NULL REFERENCES user_entity(user_id)
);


CREATE TABLE nonexistent_parking_report(
    report_id SERIAL PRIMARY KEY,
    parking_node_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL REFERENCES user_entity(user_id)
);

CREATE TABLE other_inconsistency_report(
    report_id SERIAL PRIMARY KEY,
    description VARCHAR(1000) NOT NULL,
    user_id INTEGER NOT NULL REFERENCES user_entity(user_id)
);