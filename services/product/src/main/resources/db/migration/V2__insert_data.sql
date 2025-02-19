INSERT INTO category (id, description, name)
VALUES (nextval('category_seq'), 'Electronic devices, gadgets, and accessories', 'Electronics');

INSERT INTO category (id, description, name)
VALUES (nextval('category_seq'), 'Clothing, apparel, and fashion accessories', 'Clothing');

INSERT INTO category (id, description, name)
VALUES (nextval('category_seq'), 'Kitchen and home appliances for everyday use', 'Home Appliances');

INSERT INTO category (id, description, name)
VALUES (nextval('category_seq'), 'Fiction, non-fiction, and educational books', 'Books');

INSERT INTO category (id, description, name)
VALUES (nextval('category_seq'), 'Toys, games, and recreational items', 'Toys');

------------------------------------------------------------
-- Insert 5 realistic products, each linked to a category
------------------------------------------------------------
-- Electronics: Smartphone
INSERT INTO public.product (id, description, name, available_quantity, price, category_id)
VALUES
  (nextval('product_seq'),
   'Latest model smartphone with high-resolution display and advanced camera features',
   'Smartphone',
   50,
   699.99,
   (SELECT id FROM category WHERE name = 'Electronics')),
  (nextval('product_seq'),
   '100% cotton t-shirt available in various sizes and colors',
   'T-Shirt',
   200,
   19.99,
   (SELECT id FROM category WHERE name = 'Clothing')),
  (nextval('product_seq'),
   'Compact microwave oven with multiple power levels and defrost function',
   'Microwave Oven',
   30,
   89.99,
   (SELECT id FROM category WHERE name = 'Home Appliances')),
  (nextval('product_seq'),
   'A bestselling novel with engaging storytelling and memorable characters',
   'Bestseller Novel',
   100,
   14.99,
   (SELECT id FROM category WHERE name = 'Books')),
  (nextval('product_seq'),
   'Collectible action figure with detailed design and articulation',
   'Action Figure',
   150,
   29.99,
   (SELECT id FROM category WHERE name = 'Toys'));