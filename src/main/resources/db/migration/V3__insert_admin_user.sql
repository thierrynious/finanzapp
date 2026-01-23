INSERT INTO users (username, email, password, is_active, created_at)
VALUES (
           'admin',
           'admin@finanzapp.de',
           '$2a$10$1g9OGGeUXLEVp3G.yUpPOuTfkAXhJhi08b4KRSzWqkvREZyEed9tO', -- admin123
           TRUE,
           CURRENT_TIMESTAMP()
       );
