CREATE TABLE shared_Transfer_user
(
    id BIGSERIAL PRIMARY KEY,
    user_id_id BIGINT REFERENCES users(id),
    shared_Transfers_id_id BIGINT REFERENCES shared_transfers(id),
    user_identification_number BIGINT
)