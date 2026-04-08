-- src/main/resources/schema.sql

CREATE TABLE IF NOT EXISTS franchises (
                                          franchise_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name         VARCHAR(255) NOT NULL,
                                          description  VARCHAR(500)
    );

CREATE TABLE IF NOT EXISTS branches (
                                        branch_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name         VARCHAR(255) NOT NULL,
    franchise_id BIGINT NOT NULL,
    CONSTRAINT fk_branch_franchise
    FOREIGN KEY (franchise_id)
    REFERENCES franchises(franchise_id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS products (
                                        product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name       VARCHAR(255) NOT NULL,
    stock      INT NOT NULL DEFAULT 0,
    branch_id  BIGINT NOT NULL,
    CONSTRAINT fk_product_branch
    FOREIGN KEY (branch_id)
    REFERENCES branches(branch_id)
    ON DELETE CASCADE
    );