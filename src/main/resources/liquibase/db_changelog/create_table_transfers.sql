CREATE TABLE Transfers
(
    id BIGSERIAL PRIMARY KEY,
    trip_date VARCHAR(256),
    adults_amount INTEGER,
    auto_type varchar(256),
    airport VARCHAR(256),
    location VARCHAR(256),
    children_under5 INTEGER,
    children_above5 INTEGER,
    direction VARCHAR(256),
    is_ended BOOLEAN
)