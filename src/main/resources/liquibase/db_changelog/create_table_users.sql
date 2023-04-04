CREATE TABLE Users
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(256) ,
    last_name VARCHAR(256),
    patronymic VARCHAR(256) ,
    arrival_date VARCHAR(256) ,
    flight_number VARCHAR(256) ,
    phone_number VARCHAR(256) ,
    email VARCHAR(256),
    telegram_login VARCHAR(256),
    trip_comment VARCHAR(512),
    passport VARCHAR(256),
    identification_number BIGINT
)