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