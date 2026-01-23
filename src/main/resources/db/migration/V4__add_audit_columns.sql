ALTER TABLE transactions
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE transactions
    ADD COLUMN updated_at TIMESTAMP;

ALTER TABLE transactions
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE transactions
    ADD COLUMN updated_by VARCHAR(255);
