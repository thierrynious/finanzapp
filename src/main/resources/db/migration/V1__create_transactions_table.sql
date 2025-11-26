CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              title VARCHAR(255),
                              amount DECIMAL(10,2),
                              date DATE,
                              is_income BOOLEAN
);
