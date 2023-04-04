CREATE TABLE Shared_Transfers
(
    id BIGSERIAL PRIMARY KEY,
    transfer_time VARCHAR(256),
    transfer_date VARCHAR(256),
    adults_amount INTEGER,
    children_under5 INTEGER,
    children_above5 INTEGER,
    start_location VARCHAR(256),
    end_location VARCHAR(256),
    is_ended BOOLEAN,
    auto_type VARCHAR(256),
    is_deleted BOOLEAN,
    is_updated BOOLEAN,
    is_pick_up_from_airport BOOLEAN,
    is_shared BOOLEAN
)