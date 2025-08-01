-- Insert sample OutsourcedParts (part_type = 2)
INSERT INTO Parts (part_type, name, price, inv, min_inv, max_inv, company_name) VALUES
(2, 'Magic Wand', 15.0, 10, 5, 20, 'Wand Co.'),
(2, 'Potion Bottle', 5.0, 20, 10, 50, 'Potion Co.'),
(2, 'Spell Book', 25.0, 5, 2, 15, 'Spell Book Co.'),
(2, 'Crystal Ball', 40.0, 8, 3, 12, 'Crystal Ball Co.'),
(2, 'Broomstick', 30.0, 12, 5, 25, 'Broomstick Co.');

-- Insert sample Products
INSERT INTO Products (name, price, inv) VALUES
('Beginner Wizard Kit', 100.0, 10),
('Advanced Wizard Kit', 250.0, 5),
('Potion Making Set', 50.0, 20),
('Fortune Telling Set', 70.0, 8),
('Flying Starter Pack', 200.0, 12);