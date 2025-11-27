-- =====================================================
-- DATABASE SCHEMA - RA1NEE STORE
-- Hệ thống thương mại điện tử bán linh kiện và thiết bị công nghệ
-- =====================================================

-- Tạo database
DROP DATABASE IF EXISTS ra1neestore;
CREATE DATABASE ra1neestore;
USE ra1neestore;

-- =====================================================
-- BẢNG NGƯỜI DÙNG
-- =====================================================
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    role ENUM('customer', 'admin') DEFAULT 'customer',
    status ENUM('active', 'inactive', 'pending') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- BẢNG DANH MỤC (KHÔNG CÓ PARENT - DANH MỤC PHẲNG)
-- =====================================================
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- BẢNG SẢN PHẨM
-- =====================================================
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(15,2) NOT NULL,
    sale_price DECIMAL(15,2),
    category_id INT NOT NULL,
    image_url VARCHAR(255),
    stock_quantity INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- =====================================================
-- BẢNG ĐơN HÀNG
-- =====================================================
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    customer_name VARCHAR(100),
    customer_phone VARCHAR(20),
    customer_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================================================
-- BẢNG CHI TIẾT ĐƠN HÀNG
-- =====================================================
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- =====================================================
-- BẢNG GIỎ HÀNG
-- =====================================================
CREATE TABLE cart (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- =====================================================
-- BẢNG ĐÁNH GIÁ (REVIEWS)
-- =====================================================
CREATE TABLE reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- =====================================================
-- DỮ LIỆU MẪU - DANH MỤC (KHÔNG CÓ PARENT)
-- =====================================================
INSERT INTO categories (name, description) VALUES 
('CPU', 'Bộ vi xử lý - CPU Intel, AMD'),
('RAM', 'Bộ nhớ trong - RAM DDR4, DDR5'),
('Bàn phím', 'Bàn phím cơ, bàn phím gaming, bàn phím văn phòng'),
('Chuột', 'Chuột gaming, chuột văn phòng, chuột không dây'),
('Linh kiện máy tính', 'Mainboard, VGA, SSD, HDD và các linh kiện khác');

-- =====================================================
-- DỮ LIỆU MẪU - NGƯỜI DÙNG
-- =====================================================
-- Password mặc định cho tất cả users: "123456" (không mã hóa)
INSERT INTO users (username, email, password, full_name, phone, address, role, status) VALUES 
('admin', 'admin@ra1neestore.com', '123456', 'Quản trị viên', '0123456789', 'Hà Nam', 'admin', 'active'),
('adminhieu', 'hieu2005@ra1neestore.com', '123456', 'Nguyễn Ngọc Hiếu', '0912312312', 'Hà Tây', 'admin', 'active'),
('ra1neeadmin', 'ra1nee@ra1neestore.com', 'ra1nee', 'Ra1nee Admin', '0945645645', 'Hà Nội', 'admin', 'active'),
('customer1', 'customer1@email.com', '123456', 'Nguyễn Văn A', '0987654321', 'TP.HCM', 'customer', 'active'),
('customer2', 'customer2@email.com', '123456', 'Trần Thị B', '0912345678', 'Đà Nẵng', 'customer', 'active');

-- =====================================================
-- DỮ LIỆU MẪU - SẢN PHẨM
-- =====================================================
INSERT INTO products (name, description, price, sale_price, category_id, stock_quantity, image_url, is_active) VALUES 
-- CPU
('CPU Intel Core i5-12400F', 'CPU Intel Core i5 thế hệ 12, 6 nhân 12 luồng', 4500000, 4200000, 1, 50, '/images/products/cpu/cpu-i5-12400f.jpg', TRUE),
('CPU AMD Ryzen 5 5600X', 'CPU AMD Ryzen 5 5600X, 6 nhân 12 luồng', 5200000, 4900000, 1, 35, '/images/products/cpu/cpu-amd-ryzen5.jpg', TRUE),

-- RAM
('RAM Kingston Fury 8GB DDR4', 'RAM Kingston Fury Beast 8GB DDR4 3200MHz', 800000, 750000, 2, 100, '/images/products/ram/ram-kingston-8gb.jpg', TRUE),

-- Bàn phím
('Bàn phím cơ Logitech G913', 'Bàn phím cơ gaming không dây Logitech G913', 3500000, 3200000, 3, 30, '/images/products/banphim/ban-phim-logitech-g913.jpg', TRUE),

-- Chuột
('Chuột gaming Razer DeathAdder', 'Chuột gaming Razer DeathAdder Essential', 900000, 850000, 4, 40, '/images/products/chuot/chuot-razer.jpg', TRUE),

-- Linh kiện máy tính
('Test Product CRUD', 'Test description for CRUD', 1500000, NULL, 5, 0, NULL, TRUE),
('Test Product Upload', 'Testing image upload', 1000000, NULL, 5, 0, NULL, TRUE);

-- =====================================================
-- DỮ LIỆU MẪU - ĐƠN HÀNG
-- =====================================================
INSERT INTO orders (user_id, total_amount, status, customer_name, customer_phone, customer_address) VALUES 
(4, 5000000, 'delivered', 'Nguyễn Văn A', '0987654321', '123 Đường ABC, Quận 1, TP.HCM'),
(5, 4200000, 'shipped', 'Trần Thị B', '0912345678', '456 Đường XYZ, Quận Hải Châu, Đà Nẵng');

-- =====================================================
-- DỮ LIỆU MẪU - CHI TIẾT ĐƠN HÀNG
-- =====================================================
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES 
(1, 1, 1, 4200000),
(1, 3, 1, 750000),
(2, 4, 1, 3200000),
(2, 5, 1, 850000);

-- =====================================================
-- DỮ LIỆU MẪU - GIỎ HÀNG
-- =====================================================
INSERT INTO cart (user_id, product_id, quantity) VALUES 
(4, 3, 2),
(4, 5, 1),
(5, 1, 1);
