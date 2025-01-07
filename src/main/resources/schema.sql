CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_uuid VARCHAR(36) NOT NULL UNIQUE,
    name VARCHAR(200),
    email VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_login TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE phone (
    number BIGINT NOT NULL,
    city_code INT NOT NULL,
    country_code VARCHAR(10) NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (number, city_code, country_code),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);