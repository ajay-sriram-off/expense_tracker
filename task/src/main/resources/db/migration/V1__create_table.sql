-- CATEGORY TABLE
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- EXPENSE TABLE
CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES category(id)
);