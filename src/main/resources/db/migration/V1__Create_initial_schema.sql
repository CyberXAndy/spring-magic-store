-- Create Products table
CREATE TABLE Products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    inv INTEGER NOT NULL CHECK (inv >= 0)
);

-- Create Parts table with inheritance strategy SINGLE_TABLE
CREATE TABLE Parts (
    id BIGSERIAL PRIMARY KEY,
    part_type INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    inv INTEGER NOT NULL CHECK (inv >= 0),
    min_inv INTEGER NOT NULL CHECK (min_inv >= 0),
    max_inv INTEGER NOT NULL CHECK (max_inv >= 0),
    -- InhousePart specific field
    part_id INTEGER,
    -- OutsourcedPart specific field
    company_name VARCHAR(255)
);

-- Create junction table for Many-to-Many relationship between Products and Parts
CREATE TABLE product_part (
    product_id BIGINT NOT NULL,
    part_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, part_id),
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE,
    FOREIGN KEY (part_id) REFERENCES Parts(id) ON DELETE CASCADE
);

-- Add indexes for better performance
CREATE INDEX idx_parts_part_type ON Parts(part_type);
CREATE INDEX idx_parts_name ON Parts(name);
CREATE INDEX idx_products_name ON Products(name);

-- Add constraints for discriminator values
-- part_type: 1 = InhousePart, 2 = OutsourcedPart
ALTER TABLE Parts ADD CONSTRAINT chk_part_type CHECK (part_type IN (1, 2));

-- Add conditional constraints for subclass fields
-- InhousePart (part_type = 1) should have part_id, company_name should be null
-- OutsourcedPart (part_type = 2) should have company_name, part_id should be null
ALTER TABLE Parts ADD CONSTRAINT chk_inhouse_part 
    CHECK ((part_type = 1 AND company_name IS NULL) OR part_type != 1);
    
ALTER TABLE Parts ADD CONSTRAINT chk_outsourced_part 
    CHECK ((part_type = 2 AND company_name IS NOT NULL) OR part_type != 2);

-- Create DatabaseResetTracker table for tracking automatic resets
CREATE TABLE database_reset_tracker (
    id BIGSERIAL PRIMARY KEY,
    last_reset_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add index for performance
CREATE INDEX idx_reset_tracker_time ON database_reset_tracker(last_reset_time);