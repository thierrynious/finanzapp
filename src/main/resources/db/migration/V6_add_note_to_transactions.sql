-- V6_add_note_to_transactions.sql
ALTER TABLE transactions
    ADD COLUMN note VARCHAR(1000);
