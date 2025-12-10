-- MySQL dump with NNH prefix
-- Database: ra1neestore
-- All tables and columns prefixed with 'nnh'

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Drop existing database and create new one
DROP DATABASE IF EXISTS `ra1neestore`;
CREATE DATABASE `ra1neestore` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `ra1neestore`;

--
-- Table structure for table `nnhcategories`
--

DROP TABLE IF EXISTS `nnhcategories`;
CREATE TABLE `nnhcategories` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhname` varchar(100) NOT NULL,
  `nnhdescription` text,
  `nnhcreated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nnhid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhcategories`
--

LOCK TABLES `nnhcategories` WRITE;
INSERT INTO `nnhcategories` VALUES 
(1,'CPU','Bộ vi xử lý - CPU Intel, AMD','2025-11-26 15:21:13'),
(2,'RAM','Bộ nhớ trong - RAM DDR4, DDR5','2025-11-26 15:21:13'),
(3,'Bàn phím','Bàn phím cơ, bàn phím gaming, bàn phím văn phòng','2025-11-26 15:21:13'),
(4,'Chuột','Chuột gaming, chuột văn phòng, chuột không dây','2025-11-26 15:21:13'),
(5,'Linh kiện máy tính','Mainboard, VGA, SSD, HDD và các linh kiện khác','2025-11-26 15:21:13'),
(6,'Màn hình','Màn hình máy tính','2025-12-01 14:58:36'),
(7,'Kho ga','','2025-12-03 00:22:31');
UNLOCK TABLES;

--
-- Table structure for table `nnhusers`
--

DROP TABLE IF EXISTS `nnhusers`;
CREATE TABLE `nnhusers` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhusername` varchar(50) NOT NULL,
  `nnhemail` varchar(100) NOT NULL,
  `nnhpassword` varchar(255) NOT NULL,
  `nnhfull_name` varchar(100) DEFAULT NULL,
  `nnhphone` varchar(20) DEFAULT NULL,
  `nnhaddress` text,
  `nnhrole` enum('CUSTOMER','ADMIN') DEFAULT 'CUSTOMER',
  `nnhstatus` enum('ACTIVE','INACTIVE','PENDING') DEFAULT 'ACTIVE',
  `nnhcreated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nnhid`),
  UNIQUE KEY `nnhusername` (`nnhusername`),
  UNIQUE KEY `nnhemail` (`nnhemail`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhusers`
--

LOCK TABLES `nnhusers` WRITE;
INSERT INTO `nnhusers` VALUES 
(1,'admin','admin@ra1neestore.com','123123','Ngọc Hiếu','0966140909','Thanh Trì, Hà Nội','ADMIN','ACTIVE','2025-11-26 15:21:13'),
(2,'adminhieu','hieu2005@ra1neestore.com','123456','Nguyễn Ngọc Hiếu','0912312312','Hà Tây','ADMIN','ACTIVE','2025-11-26 15:21:13'),
(3,'ra1neeadmin','ra1nee@ra1neestore.com','ra1nee','Ra1nee Admin','0945645645','Hà Nội','ADMIN','ACTIVE','2025-11-26 15:21:13'),
(4,'customer1','customer1@email.com','123123','Nguyễn Văn A','0987654321','TP.HCM','CUSTOMER','ACTIVE','2025-11-26 15:21:13'),
(5,'customer2','customer2@email.com','123123123','Trần Thị B','0912345678','Đà Nẵng','CUSTOMER','ACTIVE','2025-11-26 15:21:13'),
(6,'Nguyễn Ngọc Hiếu','hieu123@gmail.com','123456789','Nguyễn Ngọc Hiếu','0966141414','Hà Nội','CUSTOMER','ACTIVE','2025-11-27 15:21:31'),
(7,'sthieu2005','hieu12345@gmail.com','123456','Anh Hiếu','0966545454','Thanh Trì Hà Nội','CUSTOMER','ACTIVE','2025-11-30 08:25:22'),
(8,'hoainam123','hoainam123@gmail.com','123456','Nguyễn Hoài Nam','0678154832','Thạch Thất, Hà Nội','CUSTOMER','ACTIVE','2025-12-01 13:18:30');
UNLOCK TABLES;

--
-- Table structure for table `nnhproducts`
--

DROP TABLE IF EXISTS `nnhproducts`;
CREATE TABLE `nnhproducts` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhname` varchar(255) NOT NULL,
  `nnhdescription` text,
  `nnhprice` decimal(15,2) NOT NULL,
  `nnhsale_price` decimal(15,2) DEFAULT NULL,
  `nnhcategory_id` bigint NOT NULL,
  `nnhimage_url` varchar(255) DEFAULT NULL,
  `nnhstock_quantity` int DEFAULT '0',
  `nnhis_active` tinyint(1) DEFAULT '1',
  `nnhcreated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nnhid`),
  KEY `nnhcategory_id` (`nnhcategory_id`),
  CONSTRAINT `nnhproducts_ibfk_1` FOREIGN KEY (`nnhcategory_id`) REFERENCES `nnhcategories` (`nnhid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhproducts`
--

LOCK TABLES `nnhproducts` WRITE;
INSERT INTO `nnhproducts` VALUES 
(1,'CPU Intel Core i5-12400F','Thế hệ & Kiến trúc	Thế hệ thứ 12 (Alder Lake)\r\nSocket: FCLGA1700\r\nSố Lõi/Luồng: 6 Lõi Hiệu năng (P-core) / 12 Luồng (Không có Lõi Hiệu suất E-core)\r\nTần số Xung nhịp: Cơ bản: 2.50 GHz / Turbo Tối đa: 4.40 GHz\r\nBộ nhớ đệm (Cache): 18 MB Intel Smart Cache\r\nTiêu thụ Điện năng (TDP): Công suất cơ bản: 65W / Công suất Turbo Tối đa: 117W\r\nĐồ họa tích hợp: Không (Ký tự \"F\" yêu cầu Card đồ họa rời)\r\nHỗ trợ RAM: Tối đa 128 GB, hỗ trợ DDR4 3200 MT/s và DDR5 4800 MT/s\r\nPhiên bản PCIe: 5.0 và 4.0 (Hỗ trợ tối đa 20 làn PCIe)',4500000.00,4200000.00,1,'/images/products/cpu/efb6becb-4f1c-4c14-9a74-5809084f77c0.jpg',501,1,'2025-11-26 15:21:13'),
(2,'CPU AMD Ryzen 5 5600X','Sản Phẩm: AMD Ryzen 5 5600X\r\nThế hệ & Kiến trúc: Zen 3 (Vermeer)\r\nSocket: AM4\r\nSố Lõi/Luồng: 6 Lõi / 12 Luồng\r\nTần số Xung nhịp,Cơ bản: 3.7 GHz / Boost Tối đa: 4.6 GHz\r\nBộ nhớ đệm (Cache),L2 Cache: 3MB / L3 Cache: 32MB\r\nTiêu thụ Điện năng: (TDP),65W\r\nĐồ họa tích hợp,Không (Yêu cầu Card đồ họa rời)\r\nPhiên bản PCIe,PCIe 4.0',5200000.00,4900000.00,1,'/images/products/cpu/d8a691ca-3805-4313-8260-1b048d01a9cf.jpg',34,1,'2025-11-26 15:21:13'),
(3,'RAM Kingston Fury 8GB DDR4','RAM Kingston Fury Beast 8GB DDR4 3200MHz',800000.00,750000.00,2,'/images/products/ram/39a68ee7-b99e-4b2d-ba14-8f0b2b655386.jpg',98,1,'2025-11-26 15:21:13'),
(4,'Bàn phím cơ Logitech G913','Bàn phím cơ gaming không dây Logitech G913',3500000.00,3200000.00,3,'/images/products/banphim/7accc77f-297e-44fe-bc79-3d5d944949df.jpg',30,1,'2025-11-26 15:21:13'),
(5,'Chuột gaming Razer DeathAdder','Chuột gaming Razer DeathAdder Essential',900000.00,850000.00,4,'/images/products/chuot/19194a67-ece0-4435-bb23-44c624cbf9b8.jpg',40,1,'2025-11-26 15:21:13'),
(6,'Test Product CRUD w/ Imgaes','Test description for CRUD',1500000.00,NULL,5,'/images/products/linhkienmaytinh/39402c5d-f6d4-4c61-8f27-68830f420a7e.png',8,1,'2025-11-26 15:21:13'),
(9,'Arm','',299000.00,320000.00,5,'/images/products/linhkienmaytinh/3f8fa1d7-25af-4707-b633-1dbb1f38d55b.webp',15,1,'2025-11-27 00:19:11'),
(11,'Tai nghe Earbud MOONDROP Nice Buds!','Sản phẩm: MOONDROP – Nice Buds!\r\nThông số:\r\n- Earbud\r\n- Trở kháng: 32Ω ±15%(@1kHz)\r\n- Độ nhạy: 122dB/Vrms (@1kHz)\r\n- Dải tần đáp ứng: 20Hz – 20kHz (IEC60318-4, -3dB)\r\n- Độ méo hài tổng (THD): ≤ 0.1% (@1kHz, 94dB)\r\n- Kết nối:\r\nJack 3.5mm stereo',159000.00,110000.00,5,'/images/products/linhkienmaytinh/280106ce-52d1-4047-ac53-e2e8cf1de8ac.png',1000,1,'2025-11-28 00:33:40'),
(21,'Tuoshuo AL276QA','Sản Phẩm: Tuoshuo AL276QA\r\n\r\nKích thước 27inch, độ phân giải 2k\r\nTấm nền Nano IPS, tần số quét 180hz\r\nDải màu 100%sRGB, 98%DCI-P3, độ sáng 400nits\r\nThời gian phản hồi 1ms\r\nSupport adaptive sync, vesa 75*75',3300000.00,3000000.00,6,'/images/products/manhinh/35b7388b-7117-4a40-b540-6af5938a6797.webp',10,1,'2025-12-01 15:02:00'),
(23,'Khô gà ','',10000.00,NULL,7,NULL,0,1,'2025-12-03 00:22:58');
UNLOCK TABLES;

--
-- Table structure for table `nnhbanners`
--

DROP TABLE IF EXISTS `nnhbanners`;
CREATE TABLE `nnhbanners` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhname` varchar(255) NOT NULL,
  `nnhimage_url` varchar(500) NOT NULL,
  `nnhdisplay_order` int DEFAULT '0',
  `nnhis_active` tinyint(1) DEFAULT '1',
  `nnhcreated_at` timestamp NULL DEFAULT NULL,
  `nnhupdated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`nnhid`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhbanners`
--

LOCK TABLES `nnhbanners` WRITE;
INSERT INTO `nnhbanners` VALUES 
(1,'Slide QC1','/images/banners/b24f5c3f-6590-4c72-8fc8-ca0d4000eb39.webp',0,1,'2025-11-29 17:15:45','2025-11-30 12:11:40'),
(2,'Không có nội dung','/images/banners/95532353-da5f-4e66-98cd-68596d56742a.webp',99,1,'2025-11-29 17:15:57','2025-11-29 17:37:20'),
(3,'Slide QC2','/images/banners/1949e2f5-3c11-4c7e-a52e-fed7534c06b2.png',0,1,'2025-11-29 17:27:25','2025-11-29 17:41:24'),
(4,'Bộ PC','/images/banners/f5ac07a0-dfbf-4fed-9ce4-a18fbe129105.jpg',50,1,'2025-11-29 17:36:36','2025-11-29 17:40:39'),
(5,'Lý do build pc','/images/banners/572e412e-94ea-402c-9fc5-11cd47893351.webp',3,1,'2025-11-29 17:41:00','2025-11-29 17:41:30');
UNLOCK TABLES;

--
-- Table structure for table `nnhcart`
--

DROP TABLE IF EXISTS `nnhcart`;
CREATE TABLE `nnhcart` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhuser_id` bigint NOT NULL,
  `nnhproduct_id` bigint NOT NULL,
  `nnhquantity` int NOT NULL DEFAULT '1',
  `nnhadded_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nnhid`),
  KEY `nnhuser_id` (`nnhuser_id`),
  KEY `nnhproduct_id` (`nnhproduct_id`),
  CONSTRAINT `nnhcart_ibfk_1` FOREIGN KEY (`nnhuser_id`) REFERENCES `nnhusers` (`nnhid`) ON DELETE CASCADE,
  CONSTRAINT `nnhcart_ibfk_2` FOREIGN KEY (`nnhproduct_id`) REFERENCES `nnhproducts` (`nnhid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhcart`
--

LOCK TABLES `nnhcart` WRITE;
INSERT INTO `nnhcart` VALUES 
(3,5,1,1,'2025-11-26 15:21:13'),
(8,1,1,1,'2025-12-03 01:37:25');
UNLOCK TABLES;

--
-- Table structure for table `nnhorders`
--

DROP TABLE IF EXISTS `nnhorders`;
CREATE TABLE `nnhorders` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhuser_id` bigint NOT NULL,
  `nnhtotal_amount` decimal(10,2) NOT NULL,
  `nnhstatus` varchar(20) NOT NULL DEFAULT 'PENDING',
  `nnhcustomer_name` varchar(100) DEFAULT NULL,
  `nnhcustomer_phone` varchar(20) DEFAULT NULL,
  `nnhcustomer_address` text,
  `nnhcreated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nnhid`),
  KEY `nnhuser_id` (`nnhuser_id`),
  CONSTRAINT `nnhorders_ibfk_1` FOREIGN KEY (`nnhuser_id`) REFERENCES `nnhusers` (`nnhid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhorders`
--

LOCK TABLES `nnhorders` WRITE;
INSERT INTO `nnhorders` VALUES 
(1,4,5000000.00,'COMPLETED','Nguyễn Văn A','0987654321','123 Đường ABC, Quận 1, TP.HCM','2025-11-26 15:21:13'),
(2,5,4200000.00,'COMPLETED','Trần Thị B','0912345678','456 Đường XYZ, Quận Hải Châu, Đà Nẵng','2025-11-26 15:21:13'),
(3,4,10600000.00,'PENDING','Nguyễn Văn A','0987654321','số nhà 123, phường 456, TP.HCM','2025-12-03 02:28:28');
UNLOCK TABLES;

--
-- Table structure for table `nnhorder_items`
--

DROP TABLE IF EXISTS `nnhorder_items`;
CREATE TABLE `nnhorder_items` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhorder_id` bigint NOT NULL,
  `nnhproduct_id` bigint NOT NULL,
  `nnhquantity` int NOT NULL,
  `nnhunit_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`nnhid`),
  KEY `nnhorder_id` (`nnhorder_id`),
  KEY `nnhproduct_id` (`nnhproduct_id`),
  CONSTRAINT `nnhorder_items_ibfk_1` FOREIGN KEY (`nnhorder_id`) REFERENCES `nnhorders` (`nnhid`) ON DELETE CASCADE,
  CONSTRAINT `nnhorder_items_ibfk_2` FOREIGN KEY (`nnhproduct_id`) REFERENCES `nnhproducts` (`nnhid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhorder_items`
--

LOCK TABLES `nnhorder_items` WRITE;
INSERT INTO `nnhorder_items` VALUES 
(1,1,1,1,4200000.00),
(2,1,3,1,750000.00),
(3,2,4,1,3200000.00),
(4,2,5,1,850000.00),
(5,3,1,1,4200000.00),
(6,3,2,1,4900000.00),
(7,3,3,2,750000.00);
UNLOCK TABLES;

--
-- Table structure for table `nnhreviews`
--

DROP TABLE IF EXISTS `nnhreviews`;
CREATE TABLE `nnhreviews` (
  `nnhid` bigint NOT NULL AUTO_INCREMENT,
  `nnhuser_id` bigint NOT NULL,
  `nnhproduct_id` bigint NOT NULL,
  `nnhrating` int NOT NULL,
  `nnhcomment` text,
  `nnhcreated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`nnhid`),
  KEY `nnhuser_id` (`nnhuser_id`),
  KEY `nnhproduct_id` (`nnhproduct_id`),
  CONSTRAINT `nnhreviews_ibfk_1` FOREIGN KEY (`nnhuser_id`) REFERENCES `nnhusers` (`nnhid`) ON DELETE CASCADE,
  CONSTRAINT `nnhreviews_ibfk_2` FOREIGN KEY (`nnhproduct_id`) REFERENCES `nnhproducts` (`nnhid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nnhreviews`
--

LOCK TABLES `nnhreviews` WRITE;
INSERT INTO `nnhreviews` VALUES 
(1,1,1,5,'10 điểm có nhưng','2025-11-26 15:29:09'),
(2,4,1,4,'dùng ổn, đúng với giá tiền','2025-11-27 12:52:23'),
(3,6,1,5,'dùng siêu ngon nhaaaaaaa','2025-11-27 15:21:51'),
(4,6,2,3,'kém hơn Intel nha','2025-11-27 15:22:02'),
(5,6,9,4,'Arm dùng bị kẹt ','2025-11-27 15:22:16'),
(6,1,5,4,'dùng ngon','2025-11-29 17:50:17'),
(7,7,1,5,'ngon ','2025-11-30 08:25:40'),
(8,7,3,5,'ngon, giá rẻ so với thị trường','2025-11-30 08:25:56'),
(9,7,11,4,'chỉ dùng nghe nhạc, chơi game tệ ','2025-11-30 08:26:21'),
(10,5,1,3,'ngon nma ship hơi lâu','2025-11-30 12:38:29'),
(11,5,2,3,'ổn','2025-11-30 12:38:42'),
(12,8,9,5,'dùng ngon','2025-12-01 13:19:11'),
(13,1,1,3,'Hài lòng với dịch vụ','2025-12-04 15:32:12');
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-05 00:00:00
