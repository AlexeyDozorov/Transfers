CREATE TABLE transfer_user
(
    id BIGSERIAL PRIMARY KEY,
    user_id_id BIGINT REFERENCES users(id),
    transfer_id_id BIGINT REFERENCES transfers(id),
    user_identification_number BIGINT
)